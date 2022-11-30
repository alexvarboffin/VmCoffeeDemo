package com.le.vmcoffeedemo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sqq on 2021/1/20 0012
 */
public class TimeUtil {
    /**
     * author:sqq  date: 2021/1/20 0012
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_YEAR, 0);
        return df.format(calendar.getTime());
    }
    
}
