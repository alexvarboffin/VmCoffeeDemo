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
 * ?????????Presenter
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
     * ????????????
     */
    public boolean startConnect(String serialPortName) {
        return CoffeeSerialPortManager.getInstance().initSerialPort(serialPortName, serialPortName, 38400, 0)
                && CoffeeSerialPortManager.getInstance().openSerialPort();
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * ????????????
     */
    public void stopConnect() {
        CoffeeSerialPortManager.getInstance().closeSerialPort();

        releaseCheckState();
        releaseEvent();
    }


    /**
     * author:sqq  date: 2021/9/25 0025
     * ????????? ????????????
     */
    public void initAllEvent() {
        initCheckState();
        initEvent();
    }


    /**
     * author:sqq  date: 2021/9/25 0025
     * ????????? ????????????????????????
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
     * ?????? ????????????????????????
     */
    private void releaseCheckState() {
        CoffeeCheckStateAction.getInstance().stopCheckState();
        CoffeeCheckStateAction.getInstance().removeListener();
    }

    /**
     * author:sqq  date: 2021/9/25 0025
     * ????????? ????????????
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
     * ?????? ????????????
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

    //-------------------------------??????????????????---------------------------------------------

    private class CoffeeActionListener implements ICoffeeActionListener {

        private final String name;

        CoffeeActionListener(String name) {
            this.name = name;
        }

        /**
         * ????????????
         */
        @Override
        public void onActionStart() {
            iView.requestResultOne("1", "", null);
            iView.requestResultOne("0", application.getString(R.string.start_send_cmd_point) + name, null);
        }

        /**
         * ?????????????????????
         *
         * @param sendState ?????????????????????????????????????????????????????????????????????CoffeeActionState.COMMAND_SEND_SUCCESS???CoffeeActionState.COMMAND_SEND_FAILURE???CoffeeActionState.COMMAND_SEND_HAS_COMMAND.
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
         * ??????????????????????????????
         *
         * @param coffeePackage ??????????????????
         */
        @Override
        public void onReceiveResult(CoffeePackage coffeePackage) {
            String receiveMsg = "";

            switch (coffeePackage.getCmdWord()) {
                case CoffeeProtocol.backCmdCode_A7://???????????????
                    receiveMsg = CoffeeReadVersionAction.getInstance().getVersion();
                    break;
                case CoffeeProtocol.backCmdCode_AB://????????????
                    receiveMsg = CoffeeStateInfo.getAllFaultInfo(CoffeeAllFaultAction.getInstance().getFaultMap());
                    break;
            }

            iView.requestResultOne("0", name + "  " + application.getString(R.string.receive_correct_response) + receiveMsg, null);

        }


        /**
         * ???????????????????????????????????????????????????
         *
         * @param overtime ????????????ms
         */
        @Override
        public void onActionOvertime(int overtime) {
            iView.requestResultOne("0", name + "  " + application.getString(R.string.cmd_send_no_response_wait_overtime_point) + overtime + "ms", null);
        }

        /**
         * ??????????????????
         */
        @Override
        public void onActionEnd() {
            iView.requestResultOne("0", application.getString(R.string.end_send_cmd_point) + name, null);
        }

    }


    //-------------------------------????????????---------------------------------------------
    private int makeStepType;//??????????????????
    private List<Recipe> recipes;//??????
    private boolean isElectricDoor;//true?????????
    private int iceVolume;//??????
    private overtimeThread overtimeThread;//??????thread
    private int currentCmdSendCount;//????????????????????????
    private long makeDrinkTimeMillis;//???????????????????????????
    private long openDoorTimeMillis;//????????????????????????


    /**
     * author:sqq  date: 2021/10/27 0027
     * ????????????(????????????????????????>??????????????????>????????????(?????????)???>????????????(?????????)????????????????????????[????????????]???????????????)
     *
     * @param recipes        ??????
     * @param isElectricDoor true?????????
     * @param iceVolume      ??????
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
     * ????????????
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
     * @param type    ????????????
     * @param isRetry true ????????????
     */
    private void sendCmd(int type, boolean isRetry) {
        Log.d(TAG, "sendCmd: @@@@@" + type + " @@" + isRetry);

        currentCmdSendCount = isRetry ? (currentCmdSendCount + 1) : 1;

        switch (type) {
            case CmdState.COFFEE_CMD_MAKE_DRINK://????????????:
                makeDrink();
                break;

            case CmdState.COFFEE_CMD_CLEAR_RESULT://??????????????????
                CoffeeClearRunResultAction.getInstance().clearRunResult();
                break;

            case CmdState.COFFEE_CMD_CUP_DOOR_OPEN://????????????(?????????)
                CoffeeCupDoorAction.getInstance().openAutoDoor();
                break;

            case CmdState.COFFEE_CMD_CUP_DOOR_CLOSE://????????????(?????????)
                CoffeeCupDoorAction.getInstance().closeAutoDoor();
                break;
        }
    }

    /**
     * author:sqq  date: 2021/10/27 0027
     * ????????????
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
     * ???????????????
     */
    private void coffeeState(CoffeeMachineState deviceStatus) {
        Log.d(TAG, "coffeeState: @@@@");

        if (makeStepType == MakeStep.FREE && deviceStatus.getRunResult() > 0) {//??????????????????
            Log.i(TAG, "---------------??????????????????----------------");
            makeStepType = MakeStep.CLEAR_RESULT;
            sendCmd(CmdState.COFFEE_CMD_CLEAR_RESULT, false);

        } else if (makeStepType == MakeStep.CLEAR_RESULT && deviceStatus.getRunResult() != 0 && currentCmdSendCount < 3) {//????????????????????????--????????????(???????????????????????????????????????)
            Log.i(TAG, "---------------????????????????????????--????????????-----currentCmdSendCount???" + currentCmdSendCount);
            sendCmd(CmdState.COFFEE_CMD_CLEAR_RESULT, true);

        } else if (makeStepType < MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 0) {//????????????????????????--??????????????????
            Log.i(TAG, "---------------????????????????????????--??????????????????----------------");
            makeStepType = MakeStep.MAKE_DRINK;
            sendCmd(CmdState.COFFEE_CMD_MAKE_DRINK, false);
            makeDrinkTimeMillis = System.currentTimeMillis();

        } else if (iceVolume > 0 && deviceStatus.isHasCup()) {//?????? ??????(??????????????????????????????????????????????????????3s?????????)
            Log.i(TAG, "---------------?????? ??????--------------");
            icePresenter.outIce(iceVolume, false);
            iceVolume = 0;

        } else if (makeStepType == MakeStep.MAKE_DRINK && (deviceStatus.getRunResult() == 0 || deviceStatus.getRunResult() == 12 || deviceStatus.getRunResult() == 22 || deviceStatus.getRunResult() == 24)
                && currentCmdSendCount < 3) {//????????????????????????--????????????(???????????????????????????????????????)
            Log.i(TAG, "---------------????????????????????????--????????????-----currentCmdSendCount:" + currentCmdSendCount);
            sendCmd(CmdState.COFFEE_CMD_MAKE_DRINK, true);

        } else if (makeStepType == MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 2 && !isElectricDoor) {//???????????? ????????????(????????????)
            Log.i(TAG, "---------------???????????? ????????????(????????????)------------");
            makeDrinkTimeMillis = 0;
            makeStepType = MakeStep.WAIT_TAKE_CUP;
            iView.requestResultThree("makeSuccess-waitTakeCup", "", null);

        } else if (makeStepType == MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 2 && isElectricDoor) {//???????????? ?????????(?????????)
            Log.i(TAG, "---------------???????????? ?????????(?????????)------------");
            makeDrinkTimeMillis = 0;
            openDoorTimeMillis = System.currentTimeMillis();
            makeStepType = MakeStep.OPEN_DOOR;
            sendCmd(CmdState.COFFEE_CMD_CUP_DOOR_OPEN, false);

        } else if (makeStepType == MakeStep.OPEN_DOOR && !deviceStatus.isCupDoorOpen() && System.currentTimeMillis() - openDoorTimeMillis > 10000 && currentCmdSendCount < 3) {//???????????????--????????????(???????????????????????????????????????)
            Log.i(TAG, "---------------???????????????--????????????-----currentCmdSendCount:" + currentCmdSendCount);
            openDoorTimeMillis = System.currentTimeMillis();
            sendCmd(CmdState.COFFEE_CMD_CUP_DOOR_OPEN, true);

        } else if (makeStepType == MakeStep.OPEN_DOOR && deviceStatus.isCupDoorOpen()) {//????????????(?????????)
            Log.i(TAG, "---------------????????????(?????????)--------------");
            makeStepType = MakeStep.WAIT_TAKE_CUP;
            openDoorTimeMillis = 0;
            iView.requestResultThree("makeSuccess-waitTakeCup", "", deviceStatus);

        } else if (makeStepType == MakeStep.WAIT_TAKE_CUP && !deviceStatus.isHasCup()) {//???????????? ????????????
            Log.i(TAG, "---------------???????????? ????????????--------------");
            makeStepType = MakeStep.WAIT_CLOSE_DOOR;
            iView.requestResultThree("waitCloseDoor", "", deviceStatus);

        } else if (makeStepType == MakeStep.WAIT_CLOSE_DOOR && !isElectricDoor) {//????????????(???????????? ???????????????????????????)
            Log.i(TAG, "---------------????????????(???????????? ???????????????????????????)-----------");
            makeStepType = MakeStep.MAKE_END;
            iView.requestResultThree("makeSuccess-makeEnd", "", deviceStatus);

        } else if (makeStepType == MakeStep.WAIT_CLOSE_DOOR && isElectricDoor) {//???????????? ????????????(?????????)
            Log.i(TAG, "----------------???????????? ????????????(?????????)---------------");
            makeStepType = MakeStep.CLOSE_DOOR;
            sendCmd(CmdState.COFFEE_CMD_CUP_DOOR_CLOSE, false);

        } else if (makeStepType == MakeStep.CLOSE_DOOR && !deviceStatus.isCupDoorOpen()) {//????????????(????????? ????????????)
            Log.i(TAG, "----------------????????????(????????? ????????????)---------------");
            makeStepType = MakeStep.MAKE_END;
            iView.requestResultThree("makeSuccess-makeEnd", "", deviceStatus);

        } else {//????????????
            makeFailure(deviceStatus);
        }
    }

    /**
     * author:sqq  date: 2019/7/16 0016
     * ????????????
     */
    private void makeFailure(CoffeeMachineState deviceStatus) {
        if (((makeStepType < MakeStep.MAKE_DRINK || makeStepType == MakeStep.OPEN_DOOR) && deviceStatus.getErrorCode() > 0)
                || (makeStepType == MakeStep.MAKE_DRINK && deviceStatus.getRunResult() == 3)) {//????????????(????????????) ??????????????????????????????????????????????????????????????????????????????????????????;??????????????????????????????????????????
            String currentErrorInfo = CoffeeStateInfo.getCurrentErrorInfo(deviceStatus.getErrorCode(), deviceStatus.getErrorType(), deviceStatus.getErrorNum());
            Log.i(TAG, "---------------????????????(????????????)----------------" + currentErrorInfo);
            makeDrinkTimeMillis = 0;
            makeStepType = MakeStep.FAILURE;
            String err = deviceStatus.getErrorCode() > 0 ? currentErrorInfo : application.getString(R.string.make_failure) + "(Drive feedback make Failure)";
            iView.requestResultThree("makeFailure", err, deviceStatus);

        } else if (makeStepType == MakeStep.CLEAR_RESULT && deviceStatus.getRunResult() != 0 && currentCmdSendCount >= 3) {//??????????????????????????????--????????????
            Log.i(TAG, "---------------??????--????????????????????????--????????????--------------");
            makeStepType = MakeStep.FAILURE;
            iView.requestResultThree("makeFailure", application.getString(R.string.clear_run_result_failure) + "(Clear run result failure)", null);

        } else if (makeStepType == MakeStep.MAKE_DRINK && ((currentCmdSendCount >= 3 && deviceStatus.getRunResult() != 1 && System.currentTimeMillis() - makeDrinkTimeMillis > 15000)
                || (System.currentTimeMillis() - makeDrinkTimeMillis > 260000))) {//??????????????????(260s????????????????????????)
            String currentErrorInfo = CoffeeStateInfo.getCurrentErrorInfo(deviceStatus.getErrorCode(), deviceStatus.getErrorType(), deviceStatus.getErrorNum());
            Log.i(TAG, "---------------??????????????????----------------" + currentErrorInfo);
            makeDrinkTimeMillis = 0;
            makeStepType = MakeStep.FAILURE;
            String err = deviceStatus.getErrorCode() > 0 ? currentErrorInfo : application.getString(R.string.make_failure) + "(Make overtime: drinkDoResult=" + deviceStatus.getRunResult() + ")";
            iView.requestResultThree("makeFailure", err, deviceStatus);

        } else if (makeStepType == MakeStep.OPEN_DOOR && !deviceStatus.isCupDoorOpen() && System.currentTimeMillis() - openDoorTimeMillis > 10000 && currentCmdSendCount >= 3) {//??????????????????
            Log.i(TAG, "---------------??????--????????????---------------");
            openDoorTimeMillis = 0;
            makeStepType = MakeStep.FAILURE;
            iView.requestResultThree("makeFailure", "Open cup door failure!", deviceStatus);

        }
    }


    /**
     * author:sqq  date: 2021/10/27 0027
     * ????????????
     */
    private class overtimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(15000);
                if (makeStepType != MakeStep.FREE)//????????????
                    sleep(360000);//?????????????????????
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
            if (makeStepType < MakeStep.WAIT_TAKE_CUP) {//??????(???????????????)
                Log.i(TAG, "----------------????????-?????? (???? ???????????????????? ????????????????????????)--------------");
                makeStepType = MakeStep.FAILURE;
                iView.requestResultThree("makeFailure", "Overtime", null);

            } else if (makeStepType >= MakeStep.WAIT_TAKE_CUP) {//??????(???????????????)--????????????
                Log.i(TAG, "----------------????????-?????? (?????????? ????????????????????????)--------------");
                iView.requestResultThree("otherException", "", null);
            }
        }
    };
}
