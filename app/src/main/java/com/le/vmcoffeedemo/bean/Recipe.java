package com.le.vmcoffeedemo.bean;

/**
 * Created by sqq on 2021/9/27 0027
 */
public class Recipe {
    
    private int materialCode;//料号：0无、1-5速溶料、9磨豆料
    private double outMaterialTime;//出料时间s
    private int outWaterYield;//出水量ml
    private double stirTime;//持续搅拌时间s：出完水后继续搅拌时间
    
    public Recipe(int materialCode, double outMaterialTime, int outWaterYield, double stirTime) {
        this.materialCode = materialCode;
        this.outMaterialTime = outMaterialTime;
        this.outWaterYield = outWaterYield;
        this.stirTime = stirTime;
    }
    
    public int getMaterialCode() {
        return materialCode;
    }
    
    public void setMaterialCode(int materialCode) {
        this.materialCode = materialCode;
    }
    
    public double getOutMaterialTime() {
        return outMaterialTime;
    }
    
    public void setOutMaterialTime(double outMaterialTime) {
        this.outMaterialTime = outMaterialTime;
    }
    
    public int getOutWaterYield() {
        return outWaterYield;
    }
    
    public void setOutWaterYield(int outWaterYield) {
        this.outWaterYield = outWaterYield;
    }
    
    public double getStirTime() {
        return stirTime;
    }
    
    public void setStirTime(double stirTime) {
        this.stirTime = stirTime;
    }
}
