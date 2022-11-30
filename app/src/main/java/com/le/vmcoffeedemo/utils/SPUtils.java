package com.le.vmcoffeedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sqq on 2022/9/8 0008
 * sp存储
 */
public class SPUtils {
    /**
     * author:sqq  date: 2018/9/18 0018
     * 语言 0 跟随系统 ，1 中文 ，2 英文
     * 默认 0
     */
    public static void saveLanguage(Context context, int language) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("vm", Activity.MODE_PRIVATE);
        sharedPreferences.edit().putInt("language", language).apply();
    }
    
    /**
     * author:sqq  date: 2018/9/18 0018
     * 语言 0 跟随系统 ，1 中文 ，2 英文
     * 默认 0
     */
    public static int getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("vm", Activity.MODE_PRIVATE);
        return sharedPreferences.getInt("language", 0);
    }
    
}
