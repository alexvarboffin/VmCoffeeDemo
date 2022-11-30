package com.le.vmcoffeedemo.view;

/**
 * Created by sqq on 2021/9/25
 */
public interface IMainView<T> {
    
    void requestResultOne(String state, String message, T data);
    
    void requestResultTwo(String state, String message, T data);
    
    void requestResultThree(String state, String message, T data);
    
}
