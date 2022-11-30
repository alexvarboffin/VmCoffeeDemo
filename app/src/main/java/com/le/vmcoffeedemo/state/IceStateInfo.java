package com.le.vmcoffeedemo.state;

import com.le.vmcoffeedemo.R;

import static com.le.vmcoffeedemo.base.MyApplication.application;

/**
 * Created by sqq on 2019/4/12 0012
 */
public class IceStateInfo {
    /**
     * 获取错误状态
     * 0：机器正常
     * 1：缺水故障
     * 2：缺冰故障
     * 3：出冰超时
     * 4: 称重传感器故障
     * 5：称重电机正转超时
     * 6：称重电机反转超时
     * 7：排冰电机正转超时
     * 8：排冰电机反转超时
     * 9：减速电机堵转
     * 10：制冷故障
     * 11：温度计故障
     */
    public static String getErrorInfo(byte errorCode) {
        String backValue;
        switch (errorCode) {
            case 0:
                backValue = application.getString(R.string.normal);
                break;
            case 1:
                backValue = application.getString(R.string.water_shortage_fault);
                break;
            case 2:
                backValue = application.getString(R.string.lack_of_ice_failure);
                break;
            case 3:
                backValue = application.getString(R.string.out_ice_timeout);
                break;
            case 4:
                backValue = application.getString(R.string.weighing_sensor_failure);
                break;
            case 5:
                backValue = application.getString(R.string.weighing_motor_forward_timeout);
                break;
            case 6:
                backValue = application.getString(R.string.weighing_motor_reversal_timeout);
                break;
            case 7:
                backValue = application.getString(R.string.out_ice_motor_forward_timeout);
                break;
            case 8:
                backValue = application.getString(R.string.out_ice_motor_reversal_timeout);
                break;
            case 9:
                backValue = application.getString(R.string.gear_motor_stall);
                break;
            case 10:
                backValue = application.getString(R.string.cooling_failure);
                break;
            case 11:
                backValue = application.getString(R.string.thermometer_failure);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
            
        }
        return backValue + "(" + errorCode + ")";
    }
    
    /**
     * 运行结果
     * 0：空闲等待
     * 1：正在出冰
     * 2：出冰成功
     * 3：出冰失败
     * 4: 正在排冰
     * 5：排冰成功
     * 6：排冰失败
     * 7：正在校正系数
     * 8：校正成功
     */
    public static String getRunResultInfo(byte runResult) {
        String backValue;
        switch (runResult) {
            case 0:
                backValue = application.getString(R.string.idle_waiting);
                break;
            case 1:
                backValue = application.getString(R.string.in_out_ice);
                break;
            case 2:
                backValue = application.getString(R.string.out_ice_success);
                break;
            case 3:
                backValue = application.getString(R.string.out_ice_failure);
                break;
            case 4:
                backValue = application.getString(R.string.in_drain_ice);
                break;
            case 5:
                backValue = application.getString(R.string.drain_ice_success);
                break;
            case 6:
                backValue = application.getString(R.string.drain_ice_failure);
                break;
            case 7:
                backValue = application.getString(R.string.in_revise_coefficient);
                break;
            case 8:
                backValue = application.getString(R.string.revise_coefficient_success);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
            
        }
        return backValue;
    }
    
    /**
     * 获取满冰状态
     * 0 未满
     * 1 满冰
     */
    public static String getIceInfo(byte isFullIce) {
        String backValue;
        switch (isFullIce) {
            case 0:
                backValue = application.getString(R.string.no_full_ice);
                break;
            case 1:
                backValue = application.getString(R.string.full_ice);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + isFullIce + ")";
    }
    
    
    /**
     * 获取二代冰机减速电机状态
     * 0：正常
     * 1：过流
     * 2：堵转
     */
    public static String getMotorCodeStr(byte motorCode) {
        String backValue;
        switch (motorCode) {
            case 0:
                backValue = application.getString(R.string.normal);
                break;
            case 1:
                backValue = application.getString(R.string.over_current);
                break;
            case 2:
                backValue = application.getString(R.string.stall);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + application.getString(R.string.gear_motor) + ")";
    }
    
    /**
     * 获取压缩机开关状态
     * 0 关闭
     * 1 开启
     */
    public static String getCompressorSwitchStr(byte switchState) {
        String backValue;
        switch (switchState) {
            case 0:
                backValue = application.getString(R.string.close);
                break;
            case 1:
                backValue = application.getString(R.string.open);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + application.getString(R.string.compressor) + ")";
    }
    
    /**
     * 获取减速电机开关状态
     * 0 关闭
     * 1 开启
     */
    public static String getMotorSwitchStr(byte switchState) {
        String backValue;
        switch (switchState) {
            case 0:
                backValue = application.getString(R.string.close);
                break;
            case 1:
                backValue = application.getString(R.string.open);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + application.getString(R.string.gear_motor) + ")";
    }
    
    /**
     * 获取缺水状态
     * 0 正常
     * 1 缺水故障
     */
    public static String getWaterStr(byte state) {
        String backValue;
        switch (state) {
            case 0:
                backValue = application.getString(R.string.normal);
                break;
            case 1:
                backValue = application.getString(R.string.water_shortage_fault);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + application.getString(R.string.water_yield) + ")";
    }
    
    /**
     * 获取出冰状态
     * 0 正常
     * 1 出冰超时
     */
    public static String getOutStr(byte state) {
        String backValue;
        switch (state) {
            case 0:
                backValue = application.getString(R.string.normal);
                break;
            case 1:
                backValue = application.getString(R.string.out_ice_timeout);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + application.getString(R.string.out_ice) + ")";
    }
    
    /**
     * 获取温度状态
     * 0 正常
     * 1 温度过低
     */
    public static String getMotorTempStr(byte state) {
        String backValue;
        switch (state) {
            case 0:
                backValue = application.getString(R.string.normal);
                break;
            case 1:
                backValue = application.getString(R.string.low_temperature);
                break;
            default:
                backValue = application.getString(R.string.unknown);
                break;
        }
        return backValue + "(" + application.getString(R.string.temperature) + ")";
    }
}
