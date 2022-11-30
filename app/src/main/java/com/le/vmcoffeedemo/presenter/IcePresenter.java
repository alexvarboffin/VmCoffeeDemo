package com.le.vmcoffeedemo.presenter;

import com.le.vmcoffeedemo.R;
import com.le.vmcoffeedemo.view.IMainView;
import com.yl.ylice2lib.actions.IIceTwoActionListener;
import com.yl.ylice2lib.actions.IIceTwoAllStateListener;
import com.yl.ylice2lib.actions.IIceTwoMotorStateListener;
import com.yl.ylice2lib.actions.IceTwoActionState;
import com.yl.ylice2lib.actions.IceTwoCheckStateAction;
import com.yl.ylice2lib.actions.IceTwoClearResultAction;
import com.yl.ylice2lib.actions.IceTwoDrainIceAction;
import com.yl.ylice2lib.actions.IceTwoMotorCheckStateAction;
import com.yl.ylice2lib.actions.IceTwoOutIceAction;
import com.yl.ylice2lib.actions.IceTwoReadVersionAction;
import com.yl.ylice2lib.actions.IceTwoRemovePeelAction;
import com.yl.ylice2lib.actions.IceTwoRestartBoardAction;
import com.yl.ylice2lib.actions.IceTwoReviseAction;
import com.yl.ylice2lib.actions.IceWaterReadTempAction;
import com.yl.ylice2lib.actions.IceWaterSetTempAction;
import com.yl.ylice2lib.protocol.IceTwoMotorState;
import com.yl.ylice2lib.protocol.IceTwoPackage;
import com.yl.ylice2lib.protocol.IceTwoProtocol;
import com.yl.ylice2lib.protocol.IceTwoState;
import com.yl.ylice2lib.serialport.IceTwoSerialPortManager;

import static com.le.vmcoffeedemo.base.MyApplication.application;

/**
 * Created by sqq on 2021/9/25 0025
 * 冰机Presenter
 */
public class IcePresenter {
    private IMainView iView;
    public boolean isIceMachine;//true冰机，false冰水机
    
    public IcePresenter(IMainView iView) {
        this.iView = iView;
    }
    
    /**
     * author:sqq  date: 2021/9/25 0025
     * 启动连接
     */
    public boolean startConnect(String serialPortName) {
        return IceTwoSerialPortManager.getInstance().initSerialPort(serialPortName, serialPortName, 38400, 0)
                && IceTwoSerialPortManager.getInstance().openSerialPort();
    }
    
    /**
     * author:sqq  date: 2021/9/25 0025
     * 停止连接
     */
    public void stopConnect() {
        IceTwoSerialPortManager.getInstance().closeSerialPort();
        
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
        IceTwoCheckStateAction.getInstance().setListener(new IIceTwoAllStateListener() {
            @Override
            public void onReceiveAllState(IceTwoState iceTwoState) {
                iView.requestResultTwo("IceState", "", iceTwoState);
            }
            
        });
        
        IceTwoCheckStateAction.getInstance().startCheckState(2000, 3000);
        
        //冰机电机状态，用于检修场景
        if (isIceMachine) {
            IceTwoMotorCheckStateAction.getInstance().setListener(new IIceTwoMotorStateListener() {
                @Override
                public void onReceiveMotorState(IceTwoMotorState motorState) {
                    iView.requestResultTwo("IceMotorState", "", motorState);
                }
            });
            
            IceTwoMotorCheckStateAction.getInstance().startCheckState(2000, 3500);//电机状态
        }
    }
    
    /**
     * author:sqq  date: 2021/9/25 0025
     * 释放 机器状态检测监听
     */
    private void releaseCheckState() {
        IceTwoCheckStateAction.getInstance().stopCheckState();
        IceTwoCheckStateAction.getInstance().removeListener();
        
        //冰机电机状态，用于检修场景
        if (isIceMachine) {
            IceTwoMotorCheckStateAction.getInstance().stopCheckState();
            IceTwoMotorCheckStateAction.getInstance().removeListener();
        }
    }
    
    /**
     * author:sqq  date: 2021/9/25 0025
     * 初始化 机器事件
     */
    private void initEvent() {
        IceTwoOutIceAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.out_ice) + "]"));
        IceTwoClearResultAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.clear_run_result) + "]"));
        IceTwoRestartBoardAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.restart_board) + "]"));
        IceTwoReadVersionAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.read_version) + "]"));
        if (isIceMachine) {
            IceTwoRemovePeelAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.remove_peel) + "]"));
            IceTwoDrainIceAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.drain_ice) + "]"));
            IceTwoReviseAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.revise) + "]"));
        } else {
            IceWaterSetTempAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.setting_temp) + "]"));
            IceWaterReadTempAction.getInstance().setListener(new IceActionListener("[" + application.getString(R.string.read_temp) + "]"));
        }
    }
    
    /**
     * author:sqq  date: 2021/9/25 0025
     * 释放 机器事件
     */
    private void releaseEvent() {
        IceTwoOutIceAction.getInstance().removeListener();
        IceTwoClearResultAction.getInstance().removeListener();
        IceTwoRestartBoardAction.getInstance().removeListener();
        IceTwoReadVersionAction.getInstance().removeListener();
        if (isIceMachine) {
            IceTwoRemovePeelAction.getInstance().removeListener();
            IceTwoDrainIceAction.getInstance().removeListener();
            IceTwoReviseAction.getInstance().removeListener();
        } else {
            IceWaterSetTempAction.getInstance().removeListener();
            IceWaterReadTempAction.getInstance().removeListener();
        }
    }
    
    //-------------------------------数据反馈监听---------------------------------------------
    private class IceActionListener implements IIceTwoActionListener {
        private String name;
        
        private IceActionListener(String name) {
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
         * @param sendState 取值于：IceTwoActionState.COMMAND_SEND_SUCCESS、IceTwoActionState.COMMAND_SEND_FAILURE、IceTwoActionState.COMMAND_SEND_HAS_COMMAND.
         */
        @Override
        public void onCmdSendState(int sendState) {
            String msg = application.getString(R.string.cmd_send_state);
            switch (sendState) {
                case IceTwoActionState.COMMAND_SEND_SUCCESS:
                    msg = name + "  " + application.getString(R.string.cmd_send_success);
                    break;
                case IceTwoActionState.COMMAND_SEND_FAILURE:
                    msg = name + "  " + application.getString(R.string.cmd_send_failure);
                    break;
                case IceTwoActionState.COMMAND_SEND_HAS_COMMAND:
                    msg = application.getString(R.string.current_has_cmd_send) + name;
                    break;
            }
            iView.requestResultOne("0", msg, null);
        }
        
        /**
         * 收到驱动板的返回结果
         *
         * @param iceTwoPackage 接收的数据包
         */
        @Override
        public void onReceiveResult(IceTwoPackage iceTwoPackage) {
            String receiveMsg = "";
            
            switch (iceTwoPackage.getCmdWord()) {
                case IceTwoProtocol.backCmdCode_A5://读取版本号
                    receiveMsg = IceTwoReadVersionAction.getInstance().getVersion();
                    break;
                case IceTwoProtocol.backCmdCode_B2://读配置温度(冰水机)
                    receiveMsg = "[" + application.getString(R.string.temperature) + ":" + IceWaterReadTempAction.getInstance().getTemp() + "℃, "
                            + application.getString(R.string.max) + IceWaterReadTempAction.getInstance().getMaxTemp() + "℃, "
                            + application.getString(R.string.min) + IceWaterReadTempAction.getInstance().getMinTemp() + "℃]";
                    break;
                case IceTwoProtocol.backCmdCode_A2://出冰发送成功
                    outIceSendCount = 0;
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
            
            //出冰发送失败，最多发三次
            if (outIceSendCount > 0 && outIceSendCount < 3) {
                outIce(iceValue, true);
            }
        }
        
    }
    
    //-------------------------------出冰/冰水---------------------------------------------
    private int outIceSendCount;//出冰指令当前发送次数 发送成功则置为0
    private int iceValue;
    
    /**
     * author:sqq  date: 2021/10/30 0030
     * 出冰/冰水
     */
    public void outIce(int value, boolean isRetry) {
        iceValue = value;
        outIceSendCount = isRetry ? (outIceSendCount + 1) : 1;
        IceTwoOutIceAction.getInstance().outIce(value, 20);
    }
}