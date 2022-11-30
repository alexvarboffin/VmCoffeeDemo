package com.le.vmcoffeedemo.state;

import android.text.TextUtils;

import com.le.vmcoffeedemo.R;

import java.util.HashMap;

import static com.le.vmcoffeedemo.base.MyApplication.application;

/**
 * Created by sqq on 2021/9/25 0025
 */
public class CoffeeStateInfo {
    /**
     * author:sqq  date: 2019/4/11 0011
     * 当前运行动作
     */
    public static String getCurrentActionInfo(int currentRunAction) {
        String backValue;
        switch (currentRunAction) {
            case 0:
                backValue = application.getString(R.string.machine_idle);
                break;
            case 1:
                backValue = application.getString(R.string.move_the_mouth_in);
                break;
            case 2:
                backValue = application.getString(R.string.move_the_mouth_out);
                break;
            case 3:
                backValue = application.getString(R.string.fall_cup);
                break;
            case 4:
                backValue = application.getString(R.string.open_cup_door);
                break;
            case 5:
                backValue = application.getString(R.string.close_cup_door);
                break;
            case 6:
                backValue = application.getString(R.string.brewing_motor_up);
                break;
            case 7:
                backValue = application.getString(R.string.brewing_motor_down);
                break;
            case 8:
                backValue = application.getString(R.string.grinding_beans);
                break;
            case 9:
                backValue = application.getString(R.string.out_coffee_powder);
                break;
            case 10:
                backValue = application.getString(R.string.out_grinding_beans_coffee);
                break;
            case 11:
                backValue = application.getString(R.string.instant_path_out_drink, 1);
                break;
            case 12:
                backValue = application.getString(R.string.instant_path_out_drink, 2);
                break;
            case 13:
                backValue = application.getString(R.string.instant_path_out_drink, 3);
                break;
            case 14:
                backValue = application.getString(R.string.instant_path_out_drink, 4);
                break;
            case 15:
                backValue = application.getString(R.string.instant_path_out_drink, 5);
                break;
            case 16:
                backValue = application.getString(R.string.cleaning_instant_path, 1);
                break;
            case 17:
                backValue = application.getString(R.string.cleaning_instant_path, 2);
                break;
            case 18:
                backValue = application.getString(R.string.cleaning_instant_path, 3);
                break;
            case 19:
                backValue = application.getString(R.string.cleaning_instant_path, 4);
                break;
            case 20:
                backValue = application.getString(R.string.cleaning_brew_motor);
                break;
            case 21:
                backValue = application.getString(R.string.out_sugar);
                break;
            case 22:
                backValue = application.getString(R.string.down_stick);
                break;
            case 35:
                backValue = application.getString(R.string.down_lid);
                break;
            default:
                backValue = application.getString(R.string.other);
                break;
        }
        return backValue;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 当前饮料制作结果
     */
    public static String getMakeResultInfo(int makeResult) {
        String backValue;
        switch (makeResult) {
            case 0:
                backValue = application.getString(R.string.idle_waiting);
                break;
            case 1:
                backValue = application.getString(R.string.in_make_drink);
                break;
            case 2:
                backValue = application.getString(R.string.make_success);
                break;
            case 3:
                backValue = application.getString(R.string.make_failure);
                break;
            case 11:
                backValue = application.getString(R.string.in_down_cup);
                break;
            case 12:
                backValue = application.getString(R.string.down_cup_success_wait_out_drink);
                break;
            case 21:
                backValue = application.getString(R.string.in_cleaning);
                break;
            case 22:
                backValue = application.getString(R.string.clean_complete);
                break;
            case 23:
                backValue = application.getString(R.string.in_start_self_check);
                break;
            case 24:
                backValue = application.getString(R.string.start_self_check_complete);
                break;
            default:
                backValue = application.getString(R.string.other);
                break;
        }
        return backValue;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 有无杯详情
     */
    public static String getCupInfo(boolean isHaveCup) {
        return application.getString(isHaveCup ? R.string.has_cup : R.string.no_cup);
    }
    
    /**
     * author:sqq  date: 2019/7/16 0016
     * 杯门开关详情
     */
    public static String getCupDoorInfo(boolean isCupDoor) {
        return application.getString(isCupDoor ? R.string.cup_door_open : R.string.cup_door_close);
    }
    
    /**
     * author:sqq  date: 2019/7/16 0016
     * 大门开关详情
     */
    public static String getBigDoorInfo(boolean isBigDoor) {
        return application.getString(isBigDoor ? R.string.big_door_open : R.string.big_door_close);
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 有无红外线详情
     */
    public static String getInfraredInfo(boolean isHaveInfrared) {
        String backValue;
        if (isHaveInfrared) {
            backValue = application.getString(R.string.has_infrared_mode);
        } else {
            backValue = application.getString(R.string.no_infrared_mode);
        }
        return backValue;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 有无取杯门详情
     */
    public static String getDoorInfo(boolean isHaveAutoDoor) {
        String backValue;
        if (isHaveAutoDoor) {
            backValue = application.getString(R.string.has_cup_door_mode);
        } else {
            backValue = application.getString(R.string.no_cup_door_mode);
        }
        return backValue;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 当前错误信息
     */
    public static String getCurrentErrorInfo(int errorCode, int errorType, int errorNum) {
        String backValue;
        
        if (errorCode == 0) {
            backValue = application.getString(R.string.nothing);
        } else{
            switch (errorType) {
                case 1:
                    backValue = getOtherErrorInfo(errorNum);
                    break;
                case 2:
                    backValue = getBoilerErrorInfo(errorNum);
                    break;
                case 3:
                    backValue = getCupErrorInfo(errorNum);
                    break;
                case 4:
                    backValue = getGrindingBeansErrorInfo(errorNum);
                    break;
                case 5:
                    backValue = getDoorErrorInfo(errorNum);
                    break;
                default:
                    backValue = application.getString(R.string.other);
                    break;
            }
        }
        return backValue;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 其他故障信息
     */
    private static String getOtherErrorInfo(int currentErrorNum) {
        String backErrorInfo;
        switch (currentErrorNum) {
            case 0:
                backErrorInfo = application.getString(R.string.nothing);
                break;
            case 1:
                backErrorInfo = application.getString(R.string.waste_water_tank_filled_with);
                break;
            case 2:
                backErrorInfo = application.getString(R.string.on_heating);
                break;
            case 3:
                backErrorInfo = application.getString(R.string.tank_pumping_timeout);
                break;
            case 4:
                backErrorInfo = application.getString(R.string.move_mouth_move_timeout);
                break;
            case 5:
                backErrorInfo = application.getString(R.string.down_stick_timeout);
                break;
            case 6:
                backErrorInfo = application.getString(R.string.down_lid_timeout);
                break;
            default:
                backErrorInfo = application.getString(R.string.unknown);
                break;
        }
        return backErrorInfo;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 锅炉故障信息
     */
    private static String getBoilerErrorInfo(int currentErrorNum) {
        String backErrorInfo;
        switch (currentErrorNum) {
            case 0:
                backErrorInfo = application.getString(R.string.temperature_sensor_failure);
                break;
            case 1:
                backErrorInfo = application.getString(R.string.flow_meter_failure);
                break;
            case 2:
                backErrorInfo = application.getString(R.string.no_change_of_heating_temperature);
                break;
            default:
                backErrorInfo = application.getString(R.string.unknown);
                break;
        }
        return backErrorInfo;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 落杯器故障信息
     */
    private static String getCupErrorInfo(int currentErrorNum) {
        String backErrorInfo;
        switch (currentErrorNum) {
            case 0:
                backErrorInfo = application.getString(R.string.no_cup);
                break;
            case 1:
                backErrorInfo = application.getString(R.string.cup_not_taken);
                break;
            case 2:
                backErrorInfo = application.getString(R.string.down_cup_timeout);
                break;
            case 3:
                backErrorInfo = application.getString(R.string.change_cup_timeout);
                break;
            default:
                backErrorInfo = application.getString(R.string.unknown);
                break;
        }
        return backErrorInfo;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 磨豆故障信息
     */
    private static String getGrindingBeansErrorInfo(int currentErrorNum) {
        String backErrorInfo;
        switch (currentErrorNum) {
            case 0:
                backErrorInfo = application.getString(R.string.falling_powder_fault);
                break;
            case 1:
                backErrorInfo = application.getString(R.string.the_grinder_has_run_out_of_time);
                break;
            case 2:
                backErrorInfo = application.getString(R.string.breather_switch_not_working);
                break;
            case 3:
                backErrorInfo = application.getString(R.string.brewer_timeout);
                break;
            default:
                backErrorInfo = application.getString(R.string.unknown);
                break;
        }
        return backErrorInfo;
    }
    
    /**
     * author:sqq  date: 2019/4/11 0011
     * 取杯门故障信息
     */
    private static String getDoorErrorInfo(int currentErrorNum) {
        String backErrorInfo;
        switch (currentErrorNum) {
            case 0:
                backErrorInfo = application.getString(R.string.door_move_timeout);
                break;
            case 1:
                backErrorInfo = application.getString(R.string.lock_door_no_close);
                break;
            default:
                backErrorInfo = application.getString(R.string.unknown);
                break;
        }
        return backErrorInfo;
    }
    
    /**
     * author:sqq  date: 2020/5/11 0011
     * 获取所有故障
     */
    public static String getAllFaultInfo(HashMap<Integer, String> faults) {
        if (faults == null || faults.isEmpty()) return "";
        
        StringBuilder faultSb = new StringBuilder();
        
        for (int faultType : faults.keySet()) {
            faultSb.append("\n").append(getAllFault(faultType, faults.get(faultType)));
        }
        
        return faultSb.toString();
    }
    
    /**
     * author:sqq  date: 2020/5/11 0011
     * 所有故障详情硬件信息
     */
    private static String getAllFault(int faultType, String faultBit) {
        StringBuilder infoSb = new StringBuilder();
        
        infoSb.append("[").append(faultType).append("-").append(faultBit).append("]  ");
        
        switch (faultType) {
            case 1:
                infoSb.append(application.getString(R.string.take_cup_door)).append("：").append(getAllFaultInfo(faultBit, cupDoorAllFaults));
                break;
            case 2:
                infoSb.append(application.getString(R.string.grinding_beans)).append("：").append(getAllFaultInfo(faultBit, grindingBeansAllFaults));
                break;
            case 3:
                infoSb.append(application.getString(R.string.down_cup)).append("：").append(getAllFaultInfo(faultBit, cupAllFaults));
                break;
            case 4:
                infoSb.append(application.getString(R.string.boiler)).append("：").append(getAllFaultInfo(faultBit, boilerAllFaults));
                break;
            case 5:
                infoSb.append(application.getString(R.string.move_the_mouth)).append("：").append(getAllFaultInfo(faultBit, mouthAllFaults));
                break;
            case 6:
                infoSb.append(application.getString(R.string.other)).append("：").append(getAllFaultInfo(faultBit, otherAllFaults));
                break;
            case 7:
                infoSb.append(application.getString(R.string.comprehensive_function_board)).append("：").append(getAllFaultInfo(faultBit, compBoardAllFaults));
                break;
            default:
                infoSb.append(application.getString(R.string.unknown));
                break;
        }
        return infoSb.toString();
    }
    
    /**
     * author:sqq  date: 2020/5/12 0012
     * 杯门所有故障信息
     */
    private static String[] cupDoorAllFaults = new String[]
            {
                    application.getString(R.string.cup_door_move_timeout),
                    application.getString(R.string.cup_door_lock_no_close),
                    application.getString(R.string.cup_door_communication_failed),
                    application.getString(R.string.cup_door_motor_failure),
                    application.getString(R.string.cup_door_electromagnet_connection_timeout),
            };
    /**
     * author:sqq  date: 2020/5/12 0012
     * 磨豆所有故障信息
     */
    private static String[] grindingBeansAllFaults = new String[]
            {
                    application.getString(R.string.falling_powder_fault),
                    application.getString(R.string.the_grinder_has_run_out_of_time),
                    application.getString(R.string.breather_switch_not_working),
                    application.getString(R.string.brewer_timeout),
            };
    /**
     * author:sqq  date: 2020/5/12 0012
     * 落杯器所有故障信息
     */
    private static String[] cupAllFaults = new String[]
            {
                    application.getString(R.string.down_cup_timeout),
                    application.getString(R.string.change_cup_timeout),
            };
    
    /**
     * author:sqq  date: 2020/5/12 0012
     * 锅炉所有故障信息
     */
    private static String[] boilerAllFaults = new String[]
            {
                    application.getString(R.string.temperature_sensor_failure),
                    application.getString(R.string.flow_meter_failure),
                    application.getString(R.string.no_change_of_heating_temperature),
            };
    /**
     * author:sqq  date: 2020/5/12 0012
     * 移嘴所有故障信息
     */
    private static String[] mouthAllFaults = new String[]
            {
                    application.getString(R.string.mouth_connect_failure),
                    application.getString(R.string.mouth_out_timeout),
                    application.getString(R.string.mouth_in_timeout),
                    application.getString(R.string.mouth_switch_failure),
            };
    /**
     * author:sqq  date: 2020/5/12 0012
     * 其他所有故障信息
     */
    private static String[] otherAllFaults = new String[]
            {
                    application.getString(R.string.waste_water_tank_filled_with),
                    application.getString(R.string.tank_pumping_timeout),
                    application.getString(R.string.down_stick_timeout),
                    application.getString(R.string.down_lid_timeout),
            };
    
    /**
     * author:sqq  date: 2020/8/21 0021
     * 综合功能板所有故障
     */
    private static String[] compBoardAllFaults = new String[]
            {
                    application.getString(R.string.comprehensive_function_board_connect_failure),
            };
    
    /**
     * author:sqq  date: 2020/5/11 0011
     * 杯门所有故障信息
     */
    private static String getAllFaultInfo(String faultBit, String[] faults) {
        StringBuilder faultInfo = new StringBuilder();
        
        //反转bit，使之与故障数组(cupDoorAllFaults)位置对应
        StringBuilder faultBitSb = new StringBuilder(faultBit);
        faultBitSb.reverse();
        
        char[] faultChars = faultBitSb.toString().toCharArray();
        
        for (int i = 0; i < faultChars.length; i++) {
            
            if (faultChars[i] == '1' && i < faults.length) {
                faultInfo.append(TextUtils.isEmpty(faultInfo) ? "" : ", ")
                        .append(faults[i]);
                
            } else if (faultChars[i] == '1') {
                faultInfo.append(TextUtils.isEmpty(faultInfo) ? "" : ", ")
                        .append(application.getString(R.string.unknown));
                
            }
            
        }
        
        return faultInfo.toString();
    }
}
