package com.le.vmcoffeedemo.constant;

/**
 * Created by sqq on 2021/10/27 0027
 * 制作饮料步骤
 */
public class MakeStep {
    public static int FREE = 0;//空闲
    public static int CLEAR_RESULT = 1;//清除运行结果
    public static int MAKE_DRINK = 2;//制作饮料
    public static int OPEN_DOOR = 3;//开杯门
    public static int WAIT_TAKE_CUP = 4;//等待取杯
    public static int WAIT_CLOSE_DOOR = 5;//等待关杯门
    public static int CLOSE_DOOR = 6;//关杯门
    public static int MAKE_END = 10;//制作结束
    public static int FAILURE = 999;//制作失败
}
