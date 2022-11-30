package com.le.vmcoffeedemo.presenter;

import com.le.vmcoffeedemo.view.IMainView;

/**
 * Created by sqq on 2021/9/25 0025
 */
public class MainPresenter {
    
    private IMainView iView;
    private CoffeePresenter coffeePresenter;
    
    
    public CoffeePresenter getCoffeePresenter() {
        return coffeePresenter;
    }
    
    public IcePresenter getIcePresenter() {
        return coffeePresenter.getIcePresenter();
    }
    
    public MainPresenter(IMainView iView) {
        this.iView = iView;
        coffeePresenter = new CoffeePresenter(iView);
    }
    
}
