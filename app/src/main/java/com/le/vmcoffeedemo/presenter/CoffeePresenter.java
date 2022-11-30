package com.le.vmcoffeedemo.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.le.vmcoffeedemo.R;
import com.le.vmcoffeedemo.bean.Recipe;
import com.le.vmcoffeedemo.constant.CmdState;
import com.le.vmcoffeedemo.constant.MakeStep;
import com.le.vmcoffeedemo.state.CoffeeStateInfo;
import com.le.vmcoffeedemo.view.IMainView;
import com.levending.ylcoffeelib.actions.CoffeeActionState;
import com.levending.ylcoffeelib.actions.CoffeeAllFaultAction;
import com.levending.ylcoffeelib.actions.CoffeeBrewingMotorAction;
import com.levending.ylcoffeelib.actions.CoffeeCabinetDoorAction;
import com.levending.ylcoffeelib.actions.CoffeeCheckStateAction;
import com.levending.ylcoffeelib.actions.CoffeeCleanMachineAction;
import com.levending.ylcoffeelib.actions.CoffeeClearRunResultAction;
import com.levending.ylcoffeelib.actions.CoffeeConfigCupDoorAction;
import com.levending.ylcoffeelib.actions.CoffeeConfigInfraredAction;
import com.levending.ylcoffeelib.actions.CoffeeConfigTemperatureAction;
import com.levending.ylcoffeelib.actions.CoffeeCupDoorAction;
import com.levending.ylcoffeelib.actions.CoffeeFallCupAction;
import com.levending.ylcoffeelib.actions.CoffeeFallLidAction;
import com.levending.ylcoffeelib.actions.CoffeeFallStickAction;
import com.levending.ylcoffeelib.actions.CoffeeGrindingBeansAction;
import com.levending.ylcoffeelib.actions.CoffeeMakeDrinkAction;
import com.levending.ylcoffeelib.actions.CoffeeMoveMouthAction;
import com.levending.ylcoffeelib.actions.CoffeeOutCoffeePowderAction;
import com.levending.ylcoffeelib.actions.CoffeeOutWaterAction;
import com.levending.ylcoffeelib.actions.CoffeeReadVersionAction;
import com.levending.ylcoffeelib.actions.CoffeeRestartBoardAction;
import com.levending.ylcoffeelib.actions.CoffeeSelfCheckAction;
import com.levending.ylcoffeelib.actions.ICoffeeActionListener;
import com.levending.ylcoffeelib.actions.ICoffeeMachineAllStateListener;
import com.levending.ylcoffeelib.bean.CoffeeMachineState;
import com.levending.ylcoffeelib.protocol.CoffeePackage;
import com.levending.ylcoffeelib.protocol.CoffeeProtocol;
import com.levending.ylcoffeelib.serialport.CoffeeSerialPortManager;

import java.util.List;

import static com.le.vmcoffeedemo.base.MyApplication.application;

/**
 * Created by sqq on 2021/9/25 0025
 * 咖啡机Presenter
 */
public class CoffeePresenter {
    private final String TAG = "@@@";

    private IMainView iView;
    private IcePresenter icePresenter;

    public IcePresenter getIcePresenter() {
        return icePresenter;
    }

    public CoffeePresenter(IMainView iView) {
        this.iView = iView;
        icePresenter = new IcePresenter(iView);
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * 启动连接
     */
    public boolean startConnect(String serialPortName) {
        return CoffeeSerialPortManager.getInstance().initSerialPort(serialPortName, serialPortName, 38400, 0)
                && CoffeeSerialPortManager.getInstance().openSerialPort();
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * 停止连接
     */
    public void stopConnect() {
        CoffeeSerialPortManager.getInstance().closeSerialPort();

        releaseCheckState();
        releaseEvent();
    }


    /**
     * author:sqq  date: 2021/9/25 0025
     * 初始化 所有事件
     */
    public void initAllEvent() {
        initCheckState();
        initEvent();
    }


    /**
     * author:sqq  date: 2021/9/25 0025
     * 初始化 机器状态检测事件
     */
    private void initCheckState() {

        CoffeeCheckStateAction.getInstance().setListener(new ICoffeeMachineAllStateListener() {
            @Override
            public void onReceiveAllState(CoffeeMachineState machineState) {
                iView.requestResultTwo("CoffeeState", "", machineState);
                if (recipes != null && recipes.size() > 0) coffeeState(machineState);
            }
        });

        CoffeeCheckStateAction.getInstance().startCheckState(2000, 2000);
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * 释放 机器状态检测监听
     */
    private void releaseCheckState() {
        CoffeeCheckStateAction.getInstance().stopCheckState();
        CoffeeCheckStateAction.getInstance().removeListener();
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * 初始化 机器事件
     */
    private void initEvent() {
        CoffeeConfigTemperatureAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.setting_water_temp) + "]"));
        CoffeeConfigInfraredAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.setting_infrared) + "]"));
        CoffeeConfigCupDoorAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.setting_cup_door) + "]"));
        CoffeeRestartBoardAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.restart_board) + "]"));
        CoffeeSelfCheckAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.self_check) + "]"));
        CoffeeReadVersionAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.read_version_number) + "]"));
        CoffeeClearRunResultAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.clear_run_result) + "]"));
        CoffeeMakeDrinkAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.out_drink) + "]"));
        CoffeeAllFaultAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.get_all_fault) + "]"));
        CoffeeGrindingBeansAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.grinding_beans) + "]"));
        CoffeeOutCoffeePowderAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.out_coffee_powder) + "]"));
        CoffeeBrewingMotorAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.brewing_motor) + "]"));
        CoffeeMoveMouthAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.move_the_mouth) + "]"));
        CoffeeFallCupAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.down_cup) + "]"));
        CoffeeFallLidAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.down_lid) + "]"));
        CoffeeFallStickAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.down_stick) + "]"));
        CoffeeCupDoorAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.cup_door) + "]"));
        CoffeeCabinetDoorAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.cabinet_door) + "]"));
        CoffeeCleanMachineAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.clean) + "]"));
        CoffeeOutWaterAction.getInstance().setListener(new CoffeeActionListener("[" + application.getString(R.string.out_water) + "]"));
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * 释放 机器事件
     */
    private void releaseEvent() {
        CoffeeConfigTemperatureAction.getInstance().removeListener();
        CoffeeConfigInfraredAction.getInstance().removeListener();
        CoffeeConfigCupDoorAction.getInstance().removeListener();
        CoffeeRestartBoardAction.getInstance().removeListener();
        CoffeeSelfCheckAction.getInstance().removeListener();
        CoffeeReadVersionAction.getInstance().removeListener();
        CoffeeClearRunResultAction.getInstance().removeListener();
        CoffeeMakeDrinkAction.getInstance().removeListener();
        CoffeeAllFaultAction.getInstance().removeListener();
        CoffeeGrindingBeansAction.getInstance().removeListener();
        CoffeeOutCoffeePowderAction.getInstance().removeListener();
        CoffeeBrewingMotorAction.getInstance().removeListener();
        CoffeeMoveMouthAction.getInstance().removeListener();
        CoffeeFallCupAction.getInstance().removeListener();
        CoffeeFallLidAction.getInstance().removeListener();
        CoffeeFallStickAction.getInstance().removeListener();
        CoffeeCupDoorAction.getInstance().removeListener();
        CoffeeCabinetDoorAction.getInstance().removeListener();
        CoffeeCleanMachineAction.getInstance().removeListener();
        CoffeeOutWaterAction.getInstance().removeListener();
    }

    //-------------------------------数据反馈监听---------------------------------------------

    private class CoffeeActionListener implements ICoffeeActionListener {

        private final String name;

        CoffeeActionListener(String name) {
            this.name = name;
        }

        /**
         * 任务开始
         */
        @Override
        public void onActionStart() {
            iView.requestResultOne("1", "", null);
            iView.requestResultOne("0", application.getString(R.string.start_send_cmd_point) + name, null);
        }

        /**
         * 指令发送的状态
         *
         * @param sendState 发送状态：成功、失败、已有指令在进行，取值于：CoffeeActionState.COMMAND_SEND_SUCCESS、CoffeeActionState.COMMAND_SEND_FAILURE、CoffeeActionState.COMMAND_SEND_HAS_COMMAND.
         */
        @Override
        public void onCmdSendState(int sendState) {
            String msg = application.getString(R.string.cmd_send_state);
            switch (sendState) {
                case CoffeeActionState.COMMAND_SEND_SUCCESS:
                    msg = name + "  " + application.getString(R.string.cmd_send_success);
                    break;
                case CoffeeActionState.COMMAND_SEND_FAILURE:
                    msg = name + "  " + application.getString(R.string.cmd_send_failure);
                    break;
                case CoffeeActionState.COMMAND_SEND_HAS_COMMAND:
                    msg = application.getString(R.string.current_has_cmd_send) + name;
                    break;
            }
            iView.requestResultOne("0", msg, null);
        }

        /**
         * 收到驱动板的返回结果
         *
         * @param coffeePackage 接收的数据包
         */
        @Override
        public void onReceiveResult(CoffeePackage coffeePackage) {
            String receiveMsg = "";

            switch (coffeePackage.getCmdWord()) {
                case CoffeeProtocol.backCmdCode_A7://读取版本号
                    receiveMsg = CoffeeReadVersionAction.getInstance().getVersion();
                    break;
                case CoffeeProtocol.backCmdCode_AB://所有故障
                    receiveMsg = CoffeeStateInfo.getAllFaultInfo(CoffeeAllFaultAction.getInstance().getFaultMap());
                    break;
            }

            iView.requestResultOne("0", name + "  " + application.getString(R.string.receive_correct_response) + receiveMsg, null);

        }


        /**
         * 超时：没有在有效时间里收到返回结果
         *
         * @param overtime 超时时间ms
         */
        @Override
        public void onActionOvertime(int overtime) {
            iView.requestResultOne("0", name + "  " + application.getString(R.string.cmd_send_no_response_wait_overtime_point) + overtime + "ms", null);
        }

        /**
         * 单次任务结束
         */
        @Override
        public void onActionEnd() {
            iView.requestResultOne("0", application.getString(R.string.end_send_cmd_point) + name, null);
        }

    }


    //-------------------------------制作饮料---------------------------------------------
    private int makeStepType;//制作饮料步骤
    private List<Recipe> recipes;//配方
    private boolean isElectricDoor;//true电动门
    private int iceVolume;//冰量
    private overtimeThread overtimeThread;//超时thread
    private int currentCmdSendCount;//当前指令发送次数
    private long makeDrinkTimeMillis;//制作饮料开始时间戳
    private long openDoorTimeMillis;//开杯门开始时间戳


    /**
     * author:sqq  date: 2021/10/27 0027
     * 开始制作(①清除运行结果—>②制作饮料—>③开杯门(自动门)—>⑤关杯门(自动门)，每步的结果都用[状态检测]结果来判断)
     *
     * @param recipes        配方
     * @param isElectricDoor true电动门
     * @param iceVolume      冰量
     */
    public void startMakeDrink(List<Recipe> recipes, boolean isElectricDoor, int iceVolume) {
        this.recipes = recipes;
        this.isElectricDoor = isElectricDoor;
        this.iceVolume = iceVolume;
        makeStepType = MakeStep.FREE;

        overtimeThread = new overtimeThread();
        overtimeThread.start();
    }

    /**
     * author:sqq  date: 2018/11/13 0013
     * 结束制作
     */
    public void endMakeDrink() {
        if (overtimeThread != null)
            overtimeThread.interrupt();
        if (recipes != null) {
            recipes = null;
            makeStepType = MakeStep.FREE;
            makeDrinkTimeMillis = 0;
            openDoorTimeMillis = 0;
        }
    }

    /**
     * author:sqq  date: 2021/10/28 0028
     *
     * @param type    指令类型
     * @param isRetry true 重发指令
     */
    private void sendCmd(int type, boolean isRetry) {
        Log.d(TAG, "sendCmd: @@@@@" + type + " @@" + isRetry);

        currentCmdSendCount = isRetry ? (currentCmdSendCount + 1) : 1;

        switch (type) {
            case CmdState.COFFEE_CMD_MAKE_DRINK://饮料制作:
                makeDrink();
                break;

            case CmdState.COFFEE_CMD_CLEAR_RESULT://清除运行结果
                CoffeeClearRunResultAction.getInstance().clearRunResult();
                break;

            case CmdState.COFFEE_CMD_CUP_DOOR_OPEN://取杯门开(电动门)
                CoffeeCupDoorAction.getInstance().openAutoDoor();
                break;

            case CmdState.COFFEE_CMD_CUP_DOOR_CLOSE://取杯门关(电动门)
                CoffeeCupDoorAction.getInstance().closeAutoDoor();
                break;
        }
    }

    /**
     * author:sqq  date: 2021/10/27 0027
     * 制作饮料
     */
    public void makeDrink() {
        CoffeeMakeDrinkAction.getInstance().clearDrinkList();
        Log.d(TAG, "makeDrink: ");
        for (Recipe recipe : recipes) {
            if (recipe.getMaterialCode() > 0 && recipe.getOutWaterYield() > 0) {
                CoffeeMakeDrinkAction.getInstance().addDrink(
                        (byte) recipe.getMaterialCode(),
                        (byte) (int) (recipe.getOutMaterialTime() * 10),
                        (short) recipe.getOutWaterYield(),
                        (byte) (int) (recipe.getStirTime() * 10));

            }
            Log.d("@@@@", "" + (byte) recipe.getMaterialCode() +
                    (byte) (int) (recipe.getOutMaterialTime() * 10) +
                    (short) recipe.getOutWaterYield() +
                    (byte) (int) (recipe.getStirTime() * 10));
        }

        CoffeeMakeDrinkAction.getInstance().makeDrink();
    }

    /**
     * author:sqq  date: 2018/8/23 0023
     * 咖啡机状态
     */
    private void coffeeState(CoffeeMachineState deviceStatus) {
        Log.d(TAG, "coffeeState: @@@@");

        if (makeStepType == MakeStep.FREE && deviceStatus.getRunResult() > 0) {//清除运行结果
            Log.i(TAG, "---------------清除运行结果----------------");
            makeStepType = MakeStep.CLEAR_RESULT;
            sendCmd(CmdState.COFFEE_CMD_CLEAR_RESULT, false);

        } else if (makeStepType == MakeStep.CLEAR_RESULT && deviceStatus.getRunResult() != 0 && currentCmdSendCount < 3) {//清除运行结果失败--再次发送(发送三次均失败则报清除失败)
            Log.i(TAG, "---------------清除运行结果失败--再次发送-----currentCmdSendCount：" + currentCmdSendCount);
            sendCmd(CmdState.COFFEE_CMD_CLEAR_RESULT, true);

        } else if (makeStepType < MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 0) {//清除运行结果成功--开始制作饮料
            Log.i(TAG, "---------------清除运行结果成功--开始制作饮料----------------");
            makeStepType = MakeStep.MAKE_DRINK;
            sendCmd(CmdState.COFFEE_CMD_MAKE_DRINK, false);
            makeDrinkTimeMillis = System.currentTimeMillis();

        } else if (iceVolume > 0 && deviceStatus.isHasCup()) {//有杯 出冰(冰水机要等移嘴移出才能出冰水，故延迟3s出冰水)
            Log.i(TAG, "---------------有杯 出冰--------------");
            icePresenter.outIce(iceVolume, false);
            iceVolume = 0;

        } else if (makeStepType == MakeStep.MAKE_DRINK && (deviceStatus.getRunResult() == 0 || deviceStatus.getRunResult() == 12 || deviceStatus.getRunResult() == 22 || deviceStatus.getRunResult() == 24)
                && currentCmdSendCount < 3) {//制作指令发送失败--再次发送(发送三次均失败则报制作失败)
            Log.i(TAG, "---------------制作指令发送失败--再次发送-----currentCmdSendCount:" + currentCmdSendCount);
            sendCmd(CmdState.COFFEE_CMD_MAKE_DRINK, true);

        } else if (makeStepType == MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 2 && !isElectricDoor) {//制作成功 等待取杯(非电动门)
            Log.i(TAG, "---------------制作成功 等待取杯(非电动门)------------");
            makeDrinkTimeMillis = 0;
            makeStepType = MakeStep.WAIT_TAKE_CUP;
            iView.requestResultThree("makeSuccess-waitTakeCup", "", null);

        } else if (makeStepType == MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 2 && isElectricDoor) {//制作成功 开杯门(电动门)
            Log.i(TAG, "---------------制作成功 开杯门(电动门)------------");
            makeDrinkTimeMillis = 0;
            openDoorTimeMillis = System.currentTimeMillis();
            makeStepType = MakeStep.OPEN_DOOR;
            sendCmd(CmdState.COFFEE_CMD_CUP_DOOR_OPEN, false);

        } else if (makeStepType == MakeStep.OPEN_DOOR && !deviceStatus.isCupDoorOpen() && System.currentTimeMillis() - openDoorTimeMillis > 10000 && currentCmdSendCount < 3) {//开杯门失败--再次发送(发送三次均失败则报制作失败)
            Log.i(TAG, "---------------开杯门失败--再次发送-----currentCmdSendCount:" + currentCmdSendCount);
            openDoorTimeMillis = System.currentTimeMillis();
            sendCmd(CmdState.COFFEE_CMD_CUP_DOOR_OPEN, true);

        } else if (makeStepType == MakeStep.OPEN_DOOR && deviceStatus.isCupDoorOpen()) {//开门成功(电动门)
            Log.i(TAG, "---------------开门成功(电动门)--------------");
            makeStepType = MakeStep.WAIT_TAKE_CUP;
            openDoorTimeMillis = 0;
            iView.requestResultThree("makeSuccess-waitTakeCup", "", deviceStatus);

        } else if (makeStepType == MakeStep.WAIT_TAKE_CUP && !deviceStatus.isHasCup()) {//取走饮料 等待关门
            Log.i(TAG, "---------------取走饮料 等待关门--------------");
            makeStepType = MakeStep.WAIT_CLOSE_DOOR;
            iView.requestResultThree("waitCloseDoor", "", deviceStatus);

        } else if (makeStepType == MakeStep.WAIT_CLOSE_DOOR && !isElectricDoor) {//制作结束(非电动门 取走饮料会自动关门)
            Log.i(TAG, "---------------制作结束(非电动门 取走饮料会自动关门)-----------");
            makeStepType = MakeStep.MAKE_END;
            iView.requestResultThree("makeSuccess-makeEnd", "", deviceStatus);

        } else if (makeStepType == MakeStep.WAIT_CLOSE_DOOR && isElectricDoor) {//取走饮料 开始关门(电动门)
            Log.i(TAG, "----------------取走饮料 开始关门(电动门)---------------");
            makeStepType = MakeStep.CLOSE_DOOR;
            sendCmd(CmdState.COFFEE_CMD_CUP_DOOR_CLOSE, false);

        } else if (makeStepType == MakeStep.CLOSE_DOOR && !deviceStatus.isCupDoorOpen()) {//制作结束(电动门 关门成功)
            Log.i(TAG, "----------------制作结束(电动门 关门成功)---------------");
            makeStepType = MakeStep.MAKE_END;
            iView.requestResultThree("makeSuccess-makeEnd", "", deviceStatus);

        } else {//制作失败
            makeFailure(deviceStatus);
        }
    }

    /**
     * author:sqq  date: 2019/7/16 0016
     * 制作失败
     */
    private void makeFailure(CoffeeMachineState deviceStatus) {
        if (((makeStepType < MakeStep.MAKE_DRINK || makeStepType == MakeStep.OPEN_DOOR) && deviceStatus.getErrorCode() > 0)
                || (makeStepType == MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 3)) {//制作失败(驱动上报) 制作饮料指令发送后，即使报故障也不停止，需驱动板上报制作失败;开杯门之后报故障不算制作失败
            String currentErrorInfo = CoffeeStateInfo.getCurrentErrorInfo(deviceStatus.getErrorCode(), deviceStatus.getErrorType(), deviceStatus.getErrorNum());
            Log.i(TAG, "---------------制作失败(驱动上报)----------------" + currentErrorInfo);
            makeDrinkTimeMillis = 0;
            makeStepType = MakeStep.FAILURE;
            String err = deviceStatus.getErrorCode() > 0 ? currentErrorInfo : application.getString(R.string.make_failure) + "(Drive feedback make Failure)";
            iView.requestResultThree("makeFailure", err, deviceStatus);

        } else if (makeStepType == MakeStep.CLEAR_RESULT && deviceStatus.getRunResult() != 0 && currentCmdSendCount >= 3) {//多次清除运算结果失败--制作失败
            Log.i(TAG, "---------------多次--清除运算结果失败--制作失败--------------");
            makeStepType = MakeStep.FAILURE;
            iView.requestResultThree("makeFailure", application.getString(R.string.clear_run_result_failure) + "(Clear run result failure)", null);

        } else if (makeStepType == MakeStep.MAKE_DRINK && ((currentCmdSendCount >= 3 && deviceStatus.getRunResult() != 1 && System.currentTimeMillis() - makeDrinkTimeMillis > 15000)
                || (System.currentTimeMillis() - makeDrinkTimeMillis > 260000))) {//制作饮料超时(260s，该时长可自定义)
            String currentErrorInfo = CoffeeStateInfo.getCurrentErrorInfo(deviceStatus.getErrorCode(), deviceStatus.getErrorType(), deviceStatus.getErrorNum());
            Log.i(TAG, "---------------制作饮料超时----------------" + currentErrorInfo);
            makeDrinkTimeMillis = 0;
            makeStepType = MakeStep.FAILURE;
            String err = deviceStatus.getErrorCode() > 0 ? currentErrorInfo : application.getString(R.string.make_failure) + "(Make overtime: drinkDoResult=" + deviceStatus.getRunResult() + ")";
            iView.requestResultThree("makeFailure", err, deviceStatus);

        } else if (makeStepType == MakeStep.OPEN_DOOR && !deviceStatus.isCupDoorOpen() && System.currentTimeMillis() - openDoorTimeMillis > 10000 && currentCmdSendCount >= 3) {//多次开门失败
            Log.i(TAG, "---------------多次--开门失败---------------");
            openDoorTimeMillis = 0;
            makeStepType = MakeStep.FAILURE;
            iView.requestResultThree("makeFailure", "Open cup door failure!", deviceStatus);

        }
    }


    /**
     * author:sqq  date: 2021/10/27 0027
     * 超时检测
     */
    private class overtimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(15000);
                if (makeStepType != MakeStep.FREE)//通讯正常
                    sleep(360000);//该时间可自定义
                handler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (makeStepType < MakeStep.WAIT_TAKE_CUP) {//超时(制作完成前)
                Log.i(TAG, "----------------Тайм-аут (до завершения производства)--------------");
                makeStepType = MakeStep.FAILURE;
                iView.requestResultThree("makeFailure", "Overtime", null);

            } else if (makeStepType >= MakeStep.WAIT_TAKE_CUP) {//超时(制作完成后)--不算异常
                Log.i(TAG, "----------------тайм-аут (после изготовления)--------------");
                iView.requestResultThree("otherException", "", null);
            }
        }
    };
}
