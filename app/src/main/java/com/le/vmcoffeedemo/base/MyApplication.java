package com.le.vmcoffeedemo.base;

import android.app.Application;

/**
 * Created by sqq on 2021/9/25 0025
 */
public class MyApplication extends Application {
    public static MyApplication application;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        application = this;
    }
}
