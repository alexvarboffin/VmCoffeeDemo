package com.le.vmcoffeedemo.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Created by sqq on 2022/9/26 0026
 */
public class LanguageUtil {
    /**
     * author:sqq  date: 2022/9/26 0026
     * 设置语言
     * 1 中文 ，2 英文
     */
    public static void setLanguage(Context context, int type) {
        Configuration config = context.getResources().getConfiguration();
        Locale locale = Locale.ENGLISH;
        switch (type) {
            case 1:
                locale = Locale.CHINESE;
                break;
            case 2:
                locale = Locale.ENGLISH;
                break;
        }
        
        config.setLocale(locale);
        
        //Android 7.1以上
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            config.setLocales(new LocaleList(locale));
            context.createConfigurationContext(config);
        }
        
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
