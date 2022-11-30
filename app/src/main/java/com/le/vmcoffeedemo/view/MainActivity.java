package com.le.vmcoffeedemo.view;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.le.vmcoffeedemo.BuildConfig;
import com.le.vmcoffeedemo.R;
import com.le.vmcoffeedemo.base.MyApplication;
import com.le.vmcoffeedemo.bean.Recipe;
import com.le.vmcoffeedemo.databinding.ActivityMainBinding;
import com.le.vmcoffeedemo.presenter.MainPresenter;
import com.le.vmcoffeedemo.state.CoffeeStateInfo;
import com.le.vmcoffeedemo.state.IceStateInfo;
import com.le.vmcoffeedemo.utils.LanguageUtil;
import com.le.vmcoffeedemo.utils.SPUtils;
import com.le.vmcoffeedemo.utils.TimeUtil;
import com.levending.ylcoffeelib.actions.CoffeeAllFaultAction;
import com.levending.ylcoffeelib.actions.CoffeeBrewingMotorAction;
import com.levending.ylcoffeelib.actions.CoffeeCabinetDoorAction;
import com.levending.ylcoffeelib.actions.CoffeeCleanMachineAction;
import com.levending.ylcoffeelib.actions.CoffeeClearRunResultAction;
import com.levending.ylcoffeelib.actions.CoffeeConfigCupDoorAction;
import com.levending.ylcoffeelib.actions.CoffeeConfigInfraredAction;
import com.levending.ylcoffeelib.actions.CoffeeConfigTemperatureAction;
import com.levending.ylcoffeelib.actions.CoffeeCupDoorAction;
import com.levending.ylcoffeelib.actions.CoffeeFallCupAction;
import com.levending.ylcoffeelib.actions.CoffeeFallLidAction;
import com.levending.ylcoffeelib.actions.CoffeeFallStickAction;
import com.levending.ylcoffeelib.actions.CoffeeGrindingBeansAction;
import com.levending.ylcoffeelib.actions.CoffeeMoveMouthAction;
import com.levending.ylcoffeelib.actions.CoffeeOutCoffeePowderAction;
import com.levending.ylcoffeelib.actions.CoffeeOutWaterAction;
import com.levending.ylcoffeelib.actions.CoffeeReadVersionAction;
import com.levending.ylcoffeelib.actions.CoffeeRestartBoardAction;
import com.levending.ylcoffeelib.actions.CoffeeSelfCheckAction;
import com.levending.ylcoffeelib.bean.CoffeeMachineState;
import com.yl.ylice2lib.actions.IIceTwoActionListener;
import com.yl.ylice2lib.actions.IceTwoClearResultAction;
import com.yl.ylice2lib.actions.IceTwoDrainIceAction;
import com.yl.ylice2lib.actions.IceTwoOutIceAction;
import com.yl.ylice2lib.actions.IceTwoReadVersionAction;
import com.yl.ylice2lib.actions.IceTwoRemovePeelAction;
import com.yl.ylice2lib.actions.IceTwoRestartBoardAction;
import com.yl.ylice2lib.actions.IceTwoReviseAction;
import com.yl.ylice2lib.actions.IceWaterReadTempAction;
import com.yl.ylice2lib.actions.IceWaterSetTempAction;
import com.yl.ylice2lib.protocol.IceTwoMotorState;
import com.yl.ylice2lib.protocol.IceTwoPackage;
import com.yl.ylice2lib.protocol.IceTwoState;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements IMainView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private final String TAG = "@@@";
    private ActivityMainBinding binding;
    private MainPresenter mPresenter;
    private List<Recipe> recipes;
    private String[] materials;
    private TextDialog textDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int languageType = SPUtils.getLanguage(this);
        if (languageType >= 1 && languageType <= 2)
            LanguageUtil.setLanguage(MyApplication.application, languageType);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPresenter = new MainPresenter(this);

        initView();
        initData();
    }

    private void initView() {
        binding.languageSp.setSelection(SPUtils.getLanguage(this));
        binding.languageSp.setOnItemSelectedListener(this);

        binding.coffeeClean1Rb.setText(getString(R.string.pipeline_value, 1));
        binding.coffeeClean2Rb.setText(getString(R.string.pipeline_value, 2));
        binding.coffeeClean3Rb.setText(getString(R.string.pipeline_value, 3));
        binding.coffeeClean4Rb.setText(getString(R.string.pipeline_value, 4));
        binding.coffeeOutWater1Rb.setText(getString(R.string.pipeline_value, 1));
        binding.coffeeOutWater2Rb.setText(getString(R.string.pipeline_value, 2));
        binding.coffeeOutWater3Rb.setText(getString(R.string.pipeline_value, 3));
        binding.coffeeOutWater4Rb.setText(getString(R.string.pipeline_value, 4));

        binding.coffeeSetTempBt.setOnClickListener(this);
        binding.coffeeSetInfraredBt.setOnClickListener(this);
        binding.coffeeSetCupDoorBt.setOnClickListener(this);
        binding.coffeeRestartBt.setOnClickListener(this);
        binding.coffeeSelfCheckBt.setOnClickListener(this);
        binding.coffeeReadVersionBt.setOnClickListener(this);
        binding.coffeeClearResultBt.setOnClickListener(this);
        binding.coffeeGetAllFaultBt.setOnClickListener(this);
        binding.coffeeGrindingBeansBt.setOnClickListener(this);
        binding.coffeeOutCoffeePowderBt.setOnClickListener(this);
        binding.coffeeBrewingMotorUpBt.setOnClickListener(this);
        binding.coffeeBrewingMotorDownBt.setOnClickListener(this);
        binding.coffeeMoveMouthOutBt.setOnClickListener(this);
        binding.coffeeMoveMouthInBt.setOnClickListener(this);
        binding.coffeeFallCupBt.setOnClickListener(this);
        binding.coffeeFallLidBt.setOnClickListener(this);
        binding.coffeeFallStickBt.setOnClickListener(this);
        binding.coffeeOpenCupDoorElectricBt.setOnClickListener(this);
        binding.coffeeCloseCupDoorElectricBt.setOnClickListener(this);
        binding.coffeeOpenCupDoorElectromagnetBt.setOnClickListener(this);
        binding.coffeeOpenCabinetDoorBt.setOnClickListener(this);
        binding.coffeeCleanBt.setOnClickListener(this);
        binding.coffeeOutWaterBt.setOnClickListener(this);
        binding.iceRestartBt.setOnClickListener(this);
        binding.iceReadVersionBt.setOnClickListener(this);
        binding.iceClearResultBt.setOnClickListener(this);
        binding.iceOutIceWaterBt.setOnClickListener(this);
        binding.iceOutIceCubeBt.setOnClickListener(this);
        binding.iceDrainIceBt.setOnClickListener(this);
        binding.iceRemovePeelBt.setOnClickListener(this);
        binding.iceReviseBt.setOnClickListener(this);
        binding.iceSetTempBt.setOnClickListener(this);
        binding.iceReadTempBt.setOnClickListener(this);

        binding.iceUseCb.setOnCheckedChangeListener(this);
        binding.iceWaterUseCb.setOnCheckedChangeListener(this);

        textDialog = new TextDialog(this);
    }

    private void initData() {
        //配方5步
        recipes = new ArrayList<>();
        //咖啡：45ml(咖啡只有料号9和水量)
        recipes.add(new Recipe(9, 0, 45, 0));
        //速溶料1：2.5s粉、50ml
        recipes.add(new Recipe(1, 2.5, 50, 0));
        //速溶料2：2.0s粉、40ml
        recipes.add(new Recipe(2, 2.0, 40, 0));
        //速溶料3：3.0s粉、60ml
        recipes.add(new Recipe(3, 3.0, 60, 0));
        //速溶料4：1.5s粉、30ml
        recipes.add(new Recipe(4, 1.5, 30, 0));

        materials = getResources().getStringArray(R.array.materialBox);

        refreshRecipeShow();
    }

    /**
     * author:sqq  date: 2021/9/24 0024
     * 退出
     */
    public void onExit(View view) {
        finish();
        System.exit(0);
    }

    public void onTest(View view) {
        Toast.makeText(this, "@@", Toast.LENGTH_SHORT).show();
        int temp = 44;
        IceWaterSetTempAction.getInstance().setListener(new IIceTwoActionListener() {
            @Override
            public void onActionStart() {
                Log.d(TAG, "onActionStart: @@");
            }

            @Override
            public void onCmdSendState(int i) {
                Log.d(TAG, "onCmdSendState: @@@ state: "+i);
            }

            @Override
            public void onReceiveResult(IceTwoPackage iceTwoPackage) {
                Log.d(TAG, "onReceiveResult: @@@ "+iceTwoPackage.toString());
            }

            @Override
            public void onActionOvertime(int i) {
                Log.d(TAG, "onActionOvertime: @@@ "+i);
            }

            @Override
            public void onActionEnd() {
                Log.d(TAG, "onActionEnd: @@@");
            }
        });
        IceWaterSetTempAction.getInstance().setTemp((byte) temp, (byte) (temp + 2), (byte) temp);

    }

    /**
     * author:sqq  date: 2021/9/24 0024
     * 开始通信
     */
    public void onStartConnect(View view) {
        long delay = 10;

        if (!TextUtils.isEmpty(binding.connectResultTv.getText())) {
            binding.connectResultTv.setText("");
            mPresenter.getCoffeePresenter().stopConnect();
            mPresenter.getIcePresenter().stopConnect();
            delay = 1000;
        }

        binding.connectResultTv.postDelayed(new Runnable() {
            @Override
            public void run() {

                //打开串口
                boolean isOpenCoffee = mPresenter.getCoffeePresenter().startConnect(binding.coffeeSerialPortEt.getText().toString());
                binding.connectResultTv.setText(getString(R.string.coffee_machine_serial_port_open_value, getString(isOpenCoffee ? R.string.success : R.string.failure)));

                //开启咖啡机状态检测
                if (isOpenCoffee) {
                    mPresenter.getCoffeePresenter().initAllEvent();
                    binding.connectResultTv.append(getString(R.string.machine_state_check_start));
                }

                if (binding.iceUseCb.isChecked() || binding.iceWaterUseCb.isChecked()) {
                    mPresenter.getIcePresenter().isIceMachine = binding.iceUseCb.isChecked();
                    boolean isOpenIce = mPresenter.getIcePresenter().startConnect(binding.iceSerialPortEt.getText().toString());

                    if (isOpenIce) {
                        mPresenter.getIcePresenter().initAllEvent();
                    }

                }

            }
        }, delay);

    }

    /**
     * author:sqq  date: 2021/9/24 0024
     * 制作饮料
     */
    public void onMakeDrink(View view) {
        Log.d(TAG, "onMakeDrink: @@@@");
        if (!BuildConfig.DEBUG && TextUtils.isEmpty(binding.connectResultTv.getText())) {
            Toast.makeText(this, R.string.please_first_click_start_communication, Toast.LENGTH_LONG).show();
        } else if (recipes == null || recipes.size() == 0) {
            Toast.makeText(this, R.string.please_select_recipe, Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "onMakeDrink: @@@@");
            textDialog.setText(getString(R.string.make_drink), Color.GREEN);
            int iceVolume = binding.coffeeOutIceVolumeEt.getVisibility() == View.VISIBLE ? Integer.parseInt(binding.coffeeOutIceVolumeEt.getText().toString()) : 0;
            mPresenter.getCoffeePresenter().startMakeDrink(recipes, binding.coffeeElectricDoorRb.isChecked(), iceVolume);
        }

    }

    /**
     * author:sqq  date: 2021/9/24 0024
     * 选配方
     */
    public void onSelectRecipe(View view) {
        SelectRecipeDialog selectRecipeDialog = new SelectRecipeDialog(this);
        selectRecipeDialog.initData(recipes)
                .setSelectRecipeListener(new SelectRecipeListener() {
                    @Override
                    public void selectRecipe(List<Recipe> recipes) {
                        if (recipes != null && recipes.size() == 5) {
                            MainActivity.this.recipes.clear();
                            MainActivity.this.recipes.addAll(recipes);
                            refreshRecipeShow();
                        }
                    }
                }).show();
    }

    private void refreshRecipeShow() {
        binding.coffeeMakeDrinkRecipeTv.setText("");
        for (Recipe recipe : recipes) {
            if (recipe.getMaterialCode() > 0) {
                binding.coffeeMakeDrinkRecipeTv.append(materials[recipe.getMaterialCode() == 9 ? 6 : recipe.getMaterialCode()]);
                binding.coffeeMakeDrinkRecipeTv.append("(" + getString(R.string.water_yield) + recipe.getOutWaterYield() + "ml,");
                binding.coffeeMakeDrinkRecipeTv.append(getString(R.string.out_material) + recipe.getOutMaterialTime() + "s,");
                binding.coffeeMakeDrinkRecipeTv.append(getString(R.string.continue_stir_time) + recipe.getStirTime() + "s)\n");
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        binding.coffeeOutIceTv.setText(binding.iceUseCb.isChecked() ? R.string.out_ice_cube_g : R.string.out_ice_water_ml);
        binding.coffeeOutIceTv.setVisibility(binding.iceUseCb.isChecked() || binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.INVISIBLE);
        binding.coffeeOutIceVolumeEt.setVisibility(binding.iceUseCb.isChecked() || binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.INVISIBLE);

        if (!isChecked) return;

        if (buttonView == binding.iceUseCb && binding.iceWaterUseCb.isChecked())
            binding.iceWaterUseCb.setChecked(false);
        else if (buttonView == binding.iceWaterUseCb && binding.iceUseCb.isChecked())
            binding.iceUseCb.setChecked(false);

        binding.iceOutIceCubeBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceOutIceCubeVolumeEt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceDrainIceBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceDrainIceTimeEt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceRemovePeelBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceReviseBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceOutIceWaterBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.GONE);
        binding.iceOutIceWaterVolumeEt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.GONE);
        binding.iceSetTempBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.GONE);
        binding.iceSetTempValueEt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.GONE);
        binding.iceReadTempBt.setVisibility(binding.iceWaterUseCb.isChecked() ? View.VISIBLE : View.GONE);

        binding.iceStateTextTv.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceStateTv.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceWeightTextTv.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceWeightTv.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceMotorTextTv.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
        binding.iceMotorTv.setVisibility(binding.iceWaterUseCb.isChecked() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coffeeSetTempBt://配置水温（咖啡机）
                //温度范围：75-105℃
                CoffeeConfigTemperatureAction.getInstance().configTemperature((byte) Integer.parseInt(binding.coffeeMaxTempEt.getText().toString()),
                        (byte) Integer.parseInt(binding.coffeeMinTempEt.getText().toString()));
                break;
            case R.id.coffeeSetInfraredBt://配置红外线（咖啡机）
                CoffeeConfigInfraredAction.getInstance().configInfrared(binding.coffeeHasInfraredCb.isChecked());
                break;
            case R.id.coffeeSetCupDoorBt://配置杯门（咖啡机）
                CoffeeConfigCupDoorAction.getInstance().configCupDoor(binding.coffeeHasCupDoorCb.isChecked());
                break;
            case R.id.coffeeRestartBt://重启驱动板（咖啡机）
                CoffeeRestartBoardAction.getInstance().restartBoard();
                break;
            case R.id.coffeeSelfCheckBt://自检（咖啡机）
                CoffeeSelfCheckAction.getInstance().selfCheck();
                break;
            case R.id.coffeeReadVersionBt://读版本号（咖啡机）
                CoffeeReadVersionAction.getInstance().readVersion();
                break;
            case R.id.coffeeClearResultBt://清除运行结果（咖啡机）
                CoffeeClearRunResultAction.getInstance().clearRunResult();
                break;
            case R.id.coffeeGetAllFaultBt://获取所有故障（咖啡机）
                CoffeeAllFaultAction.getInstance().readAllFault();
                break;
            case R.id.coffeeGrindingBeansBt://磨豆（咖啡机）
                CoffeeGrindingBeansAction.getInstance().grindingBeans();
                break;
            case R.id.coffeeOutCoffeePowderBt://出咖啡粉（咖啡机）
                CoffeeOutCoffeePowderAction.getInstance().outCoffeePowder();
                break;
            case R.id.coffeeBrewingMotorUpBt://冲泡器上置（咖啡机）
                CoffeeBrewingMotorAction.getInstance().brewingMotorUP();
                break;
            case R.id.coffeeBrewingMotorDownBt://冲泡器下置（咖啡机）
                CoffeeBrewingMotorAction.getInstance().brewingMotorDown();
                break;
            case R.id.coffeeMoveMouthOutBt://移嘴外置（咖啡机）
                CoffeeMoveMouthAction.getInstance().moveMouthOut();
                break;
            case R.id.coffeeMoveMouthInBt://移嘴内置（咖啡机）
                CoffeeMoveMouthAction.getInstance().moveMouthIn();
                break;
            case R.id.coffeeFallCupBt://落杯（咖啡机）
                CoffeeFallCupAction.getInstance().fallCup();
                break;
            case R.id.coffeeFallLidBt://落盖（咖啡机）
                CoffeeFallLidAction.getInstance().fallLid();
                break;
            case R.id.coffeeFallStickBt://落棒（咖啡机）
                CoffeeFallStickAction.getInstance().fallStick();
                break;
            case R.id.coffeeOpenCupDoorElectricBt://开杯门 电动门（咖啡机）
                CoffeeCupDoorAction.getInstance().openAutoDoor();
                break;
            case R.id.coffeeCloseCupDoorElectricBt://关杯门 电动门（咖啡机）
                CoffeeCupDoorAction.getInstance().closeAutoDoor();
                break;
            case R.id.coffeeOpenCupDoorElectromagnetBt://开杯门 电磁铁门（咖啡机）
                //开门时间范围：0-12s
                CoffeeCupDoorAction.getInstance().openElectDoor(Integer.parseInt(binding.coffeeOpenElectromagnetDoorTimeEt.getText().toString()) * 10);
                break;
            case R.id.coffeeOpenCabinetDoorBt://Открытая дверца шкафа (кофемашина)
                CoffeeCabinetDoorAction.getInstance().openEleLock();
                break;
            case R.id.coffeeCleanBt://清洗（咖啡机）
                //通道号 1:速溶通道1 2:速溶通道2 3:速溶通道3 4:速溶通道4 9:磨豆咖啡粉通道 10:清洗所有通道
                int channel = Integer.parseInt((String) findViewById(binding.coffeeCleanRG.getCheckedRadioButtonId()).getTag());
                CoffeeCleanMachineAction.getInstance().cleanMachine((byte) channel);
                break;
            case R.id.coffeeOutWaterBt://出水（咖啡机）
                //通道号 1:磨豆通道 2:速溶通道1 3:速溶通道2 4:速溶通道3 5:速溶通道4
                int waterChannel = Integer.parseInt((String) findViewById(binding.coffeeOutWaterRG.getCheckedRadioButtonId()).getTag());
                CoffeeOutWaterAction.getInstance().outWater((byte) waterChannel, Short.parseShort(binding.coffeeOutWaterEt.getText().toString()));
                break;
            case R.id.iceRestartBt://重启驱动板（冰机/冰水机）
                IceTwoRestartBoardAction.getInstance().restartBoard();
                break;
            case R.id.iceReadVersionBt://读版本号（冰机/冰水机）
                IceTwoReadVersionAction.getInstance().readVersion();
                break;
            case R.id.iceClearResultBt://清除运行结果（冰机/冰水机）
                IceTwoClearResultAction.getInstance().clearResult();
                break;
            case R.id.iceOutIceCubeBt://出冰块（冰机）
                IceTwoOutIceAction.getInstance().outIce(Float.parseFloat(binding.iceOutIceCubeVolumeEt.getText().toString()), 20);
                break;
            case R.id.iceDrainIceBt://排冰（冰机）
                IceTwoDrainIceAction.getInstance().drainIceTime(Integer.parseInt(binding.iceDrainIceTimeEt.getText().toString()));
                break;
            case R.id.iceRemovePeelBt://起皮（冰机）
                IceTwoRemovePeelAction.getInstance().removePeel();
                break;
            case R.id.iceReviseBt://校正（冰机）
                IceTwoReviseAction.getInstance().revise();
                break;
            case R.id.iceOutIceWaterBt://出冰水（冰水机）
                IceTwoOutIceAction.getInstance().outIce(Float.parseFloat(binding.iceOutIceWaterVolumeEt.getText().toString()), 20);
                break;
            case R.id.iceSetTempBt://设置温度（冰水机）
                int temp = Integer.parseInt(binding.iceSetTempValueEt.getText().toString());
                IceWaterSetTempAction.getInstance().setTemp((byte) temp, (byte) (temp + 2), (byte) temp);
                break;
            case R.id.iceReadTempBt://读取温度（冰水机）
                IceWaterReadTempAction.getInstance().readTemp();
                break;
        }
    }

    /**
     * author:sqq  date: 2021/1/20 0023
     * 发送指令结果
     * state 0 显示 ，1 清除
     */
    @Override
    public void requestResultOne(final String state, final String message, Object data) {
        if (isFinishing()) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state.equals("0")) {
                    Log.i(TAG, message);
                    binding.cmdSendStateTv.append(TimeUtil.getCurrentTime() + "  " + message + "\n");
                } else if (state.equals("1"))
                    binding.cmdSendStateTv.setText("");
            }
        });
    }

    /**
     * author:sqq  date: 2021/9/26 0026
     * 咖啡机状态
     */
    @Override
    public void requestResultTwo(final String state, String message, final Object data) {
        if (isFinishing() || mPresenter == null) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case "CoffeeState"://咖啡机状态
                        CoffeeMachineState machineState = (CoffeeMachineState) data;

                        binding.coffeeRefreshTimeTv.setText(TimeUtil.getCurrentTime());
                        //当前水温
                        binding.coffeeCurrentWaterTempTv.setText(MessageFormat.format("{0}℃", machineState.getCurrentWaterTemp()));
                        //最高水温
                        binding.coffeeMaxWaterTempTv.setText(MessageFormat.format("{0}℃", machineState.getMaxWaterTemp()));
                        //最低水温
                        binding.coffeeMinWaterTempTv.setText(MessageFormat.format("{0}℃", machineState.getMinWaterTemp()));
                        //剩余水量
                        binding.coffeeSurplusWaterYieldTv.setText(MessageFormat.format("{0}ml", machineState.getRemainingWater()));
                        //故障信息
                        binding.coffeeErrorCodeTv.setText(MessageFormat.format("{0}-{1}  ({2})", machineState.getErrorType(), machineState.getErrorNum(),
                                CoffeeStateInfo.getCurrentErrorInfo(machineState.getErrorCode(), machineState.getErrorType(), machineState.getErrorNum())));
                        //当前动作
                        binding.coffeeActionTv.setText(MessageFormat.format("{0}  ({1})", machineState.getCurrentRunAction(),
                                CoffeeStateInfo.getCurrentActionInfo(machineState.getCurrentRunAction())));
                        //结果
                        binding.coffeeResultTv.setText(MessageFormat.format("{0}  ({1})", machineState.getRunResult(),
                                CoffeeStateInfo.getMakeResultInfo(machineState.getRunResult())));
                        //机器状态:杯、杯门、大门
                        binding.coffeeMachineTv.setText(MessageFormat.format("{0}+{1}+{2}  ({3}+{4}+{5})", machineState.isHasCup() ? "1" : "0",
                                machineState.isCupDoorOpen() ? "1" : "0", machineState.isCabinetOpen() ? "1" : "0",
                                CoffeeStateInfo.getCupInfo(machineState.isHasCup()), CoffeeStateInfo.getCupDoorInfo(machineState.isCupDoorOpen()),
                                CoffeeStateInfo.getBigDoorInfo(machineState.isCabinetOpen())));
                        //当前配置(有无红外+有无电动门)
                        binding.coffeeSettingTv.setText(MessageFormat.format("{0}+{1}  ({2}+{3})", machineState.isHasInfraredMode() ? "0" : "1",
                                machineState.isHasCupDoorMode() ? "0" : "1", CoffeeStateInfo.getInfraredInfo(machineState.isHasInfraredMode()),
                                CoffeeStateInfo.getDoorInfo(machineState.isHasCupDoorMode())));

                        break;
                    case "IceState"://冰机状态
                        IceTwoState iceTwoState = (IceTwoState) data;
                        binding.iceRefreshTimeTv.setText(TimeUtil.getCurrentTime());

                        binding.iceTempTv.setText(MessageFormat.format("{0}℃", iceTwoState.getOutletTemperature()));
                        binding.iceResultTv.setText(MessageFormat.format("{0}  {1}", String.valueOf(iceTwoState.getRunResult()), IceStateInfo.getRunResultInfo(iceTwoState.getRunResult())));

                        String stateCode = iceTwoState.getMachineErrorCode() + "+" + iceTwoState.getMachineIsFullIce();
                        binding.iceStateTv.setText(MessageFormat.format("{0}  {1}", stateCode, IceStateInfo.getErrorInfo(iceTwoState.getMachineErrorCode()) + "+" + IceStateInfo.getIceInfo(iceTwoState.getMachineIsFullIce())));
                        binding.iceWeightTv.setText(MessageFormat.format("{0}g", iceTwoState.getCurrentWeight() / 10f));

                        break;
                    case "IceMotorState"://冰机电机状态
                        IceTwoMotorState motorState = (IceTwoMotorState) data;

                        String stateMsg = motorState.getMotorErrorCode() + IceStateInfo.getMotorCodeStr(motorState.getMotorErrorCode())
                                + "+" + motorState.getCompressorSwitchState() + IceStateInfo.getCompressorSwitchStr(motorState.getCompressorSwitchState())
                                + "+" + motorState.getMotorSwitchState() + IceStateInfo.getMotorSwitchStr(motorState.getMotorSwitchState())
                                + "+" + motorState.getWaterState() + IceStateInfo.getWaterStr(motorState.getWaterState())
                                + "+" + motorState.getOutIceState() + IceStateInfo.getOutStr(motorState.getOutIceState())
                                + "+" + motorState.getTempFault() + IceStateInfo.getMotorTempStr(motorState.getTempFault())
                                + "+" + motorState.getScaleParameters() + "(" + getString(R.string.scale_parameters) + ")"
                                + "+" + motorState.getMotorParameters() + "MA(" + getString(R.string.parameters_of_reduction_motor) + ")";
                        binding.iceMotorTv.setText(stateMsg);

                        break;
                }
            }
        });
    }

    /**
     * author:sqq  date: 2021/10/28 0028
     * 制作结果
     */
    @Override
    public void requestResultThree(final String state, final String message, Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case "makeSuccess-waitTakeCup"://制作完成 等待取杯
                        textDialog.setText(getString(R.string.please_take_cup), Color.GREEN);
                        break;
                    case "waitCloseDoor"://取走杯 等待关门
                        textDialog.setText(getString(R.string.close_cup_door), Color.GREEN);
                        break;
                    case "makeFailure"://制作失败
                        textDialog.setText(message, Color.RED);
                        mPresenter.getCoffeePresenter().endMakeDrink();
                        break;
                    case "makeSuccess-makeEnd"://结束(制作成功)
                    case "otherException"://超时(制作完成后)
                        textDialog.dismiss();
                        mPresenter.getCoffeePresenter().endMakeDrink();
                        break;
                }
            }
        });
    }

    /**
     * 语言选择
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != SPUtils.getLanguage(this)) {
            SPUtils.saveLanguage(this, position);

            if (position != 0) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        mPresenter.getCoffeePresenter().stopConnect();
        mPresenter.getIcePresenter().stopConnect();
        super.onDestroy();

    }

}
