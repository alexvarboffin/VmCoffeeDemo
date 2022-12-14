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
        //??????5???
        recipes = new ArrayList<>();
        //?????????45ml(??????????????????9?????????)
        recipes.add(new Recipe(9, 0, 45, 0));
        //?????????1???2.5s??????50ml
        recipes.add(new Recipe(1, 2.5, 50, 0));
        //?????????2???2.0s??????40ml
        recipes.add(new Recipe(2, 2.0, 40, 0));
        //?????????3???3.0s??????60ml
        recipes.add(new Recipe(3, 3.0, 60, 0));
        //?????????4???1.5s??????30ml
        recipes.add(new Recipe(4, 1.5, 30, 0));

        materials = getResources().getStringArray(R.array.materialBox);

        refreshRecipeShow();
    }

    /**
     * author:sqq  date: 2021/9/24 0024
     * ??????
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
     * ????????????
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

                //????????????
                boolean isOpenCoffee = mPresenter.getCoffeePresenter().startConnect(binding.coffeeSerialPortEt.getText().toString());
                binding.connectResultTv.setText(getString(R.string.coffee_machine_serial_port_open_value, getString(isOpenCoffee ? R.string.success : R.string.failure)));

                //???????????????????????????
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
     * ????????????
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
     * ?????????
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
            case R.id.coffeeSetTempBt://???????????????????????????
                //???????????????75-105???
                CoffeeConfigTemperatureAction.getInstance().configTemperature((byte) Integer.parseInt(binding.coffeeMaxTempEt.getText().toString()),
                        (byte) Integer.parseInt(binding.coffeeMinTempEt.getText().toString()));
                break;
            case R.id.coffeeSetInfraredBt://??????????????????????????????
                CoffeeConfigInfraredAction.getInstance().configInfrared(binding.coffeeHasInfraredCb.isChecked());
                break;
            case R.id.coffeeSetCupDoorBt://???????????????????????????
                CoffeeConfigCupDoorAction.getInstance().configCupDoor(binding.coffeeHasCupDoorCb.isChecked());
                break;
            case R.id.coffeeRestartBt://??????????????????????????????
                CoffeeRestartBoardAction.getInstance().restartBoard();
                break;
            case R.id.coffeeSelfCheckBt://?????????????????????
                CoffeeSelfCheckAction.getInstance().selfCheck();
                break;
            case R.id.coffeeReadVersionBt://???????????????????????????
                CoffeeReadVersionAction.getInstance().readVersion();
                break;
            case R.id.coffeeClearResultBt://?????????????????????????????????
                CoffeeClearRunResultAction.getInstance().clearRunResult();
                break;
            case R.id.coffeeGetAllFaultBt://?????????????????????????????????
                CoffeeAllFaultAction.getInstance().readAllFault();
                break;
            case R.id.coffeeGrindingBeansBt://?????????????????????
                CoffeeGrindingBeansAction.getInstance().grindingBeans();
                break;
            case R.id.coffeeOutCoffeePowderBt://???????????????????????????
                CoffeeOutCoffeePowderAction.getInstance().outCoffeePowder();
                break;
            case R.id.coffeeBrewingMotorUpBt://??????????????????????????????
                CoffeeBrewingMotorAction.getInstance().brewingMotorUP();
                break;
            case R.id.coffeeBrewingMotorDownBt://??????????????????????????????
                CoffeeBrewingMotorAction.getInstance().brewingMotorDown();
                break;
            case R.id.coffeeMoveMouthOutBt://???????????????????????????
                CoffeeMoveMouthAction.getInstance().moveMouthOut();
                break;
            case R.id.coffeeMoveMouthInBt://???????????????????????????
                CoffeeMoveMouthAction.getInstance().moveMouthIn();
                break;
            case R.id.coffeeFallCupBt://?????????????????????
                CoffeeFallCupAction.getInstance().fallCup();
                break;
            case R.id.coffeeFallLidBt://?????????????????????
                CoffeeFallLidAction.getInstance().fallLid();
                break;
            case R.id.coffeeFallStickBt://?????????????????????
                CoffeeFallStickAction.getInstance().fallStick();
                break;
            case R.id.coffeeOpenCupDoorElectricBt://????????? ????????????????????????
                CoffeeCupDoorAction.getInstance().openAutoDoor();
                break;
            case R.id.coffeeCloseCupDoorElectricBt://????????? ????????????????????????
                CoffeeCupDoorAction.getInstance().closeAutoDoor();
                break;
            case R.id.coffeeOpenCupDoorElectromagnetBt://????????? ???????????????????????????
                //?????????????????????0-12s
                CoffeeCupDoorAction.getInstance().openElectDoor(Integer.parseInt(binding.coffeeOpenElectromagnetDoorTimeEt.getText().toString()) * 10);
                break;
            case R.id.coffeeOpenCabinetDoorBt://???????????????? ???????????? ?????????? (????????????????????)
                CoffeeCabinetDoorAction.getInstance().openEleLock();
                break;
            case R.id.coffeeCleanBt://?????????????????????
                //????????? 1:????????????1 2:????????????2 3:????????????3 4:????????????4 9:????????????????????? 10:??????????????????
                int channel = Integer.parseInt((String) findViewById(binding.coffeeCleanRG.getCheckedRadioButtonId()).getTag());
                CoffeeCleanMachineAction.getInstance().cleanMachine((byte) channel);
                break;
            case R.id.coffeeOutWaterBt://?????????????????????
                //????????? 1:???????????? 2:????????????1 3:????????????2 4:????????????3 5:????????????4
                int waterChannel = Integer.parseInt((String) findViewById(binding.coffeeOutWaterRG.getCheckedRadioButtonId()).getTag());
                CoffeeOutWaterAction.getInstance().outWater((byte) waterChannel, Short.parseShort(binding.coffeeOutWaterEt.getText().toString()));
                break;
            case R.id.iceRestartBt://????????????????????????/????????????
                IceTwoRestartBoardAction.getInstance().restartBoard();
                break;
            case R.id.iceReadVersionBt://?????????????????????/????????????
                IceTwoReadVersionAction.getInstance().readVersion();
                break;
            case R.id.iceClearResultBt://???????????????????????????/????????????
                IceTwoClearResultAction.getInstance().clearResult();
                break;
            case R.id.iceOutIceCubeBt://?????????????????????
                IceTwoOutIceAction.getInstance().outIce(Float.parseFloat(binding.iceOutIceCubeVolumeEt.getText().toString()), 20);
                break;
            case R.id.iceDrainIceBt://??????????????????
                IceTwoDrainIceAction.getInstance().drainIceTime(Integer.parseInt(binding.iceDrainIceTimeEt.getText().toString()));
                break;
            case R.id.iceRemovePeelBt://??????????????????
                IceTwoRemovePeelAction.getInstance().removePeel();
                break;
            case R.id.iceReviseBt://??????????????????
                IceTwoReviseAction.getInstance().revise();
                break;
            case R.id.iceOutIceWaterBt://????????????????????????
                IceTwoOutIceAction.getInstance().outIce(Float.parseFloat(binding.iceOutIceWaterVolumeEt.getText().toString()), 20);
                break;
            case R.id.iceSetTempBt://???????????????????????????
                int temp = Integer.parseInt(binding.iceSetTempValueEt.getText().toString());
                IceWaterSetTempAction.getInstance().setTemp((byte) temp, (byte) (temp + 2), (byte) temp);
                break;
            case R.id.iceReadTempBt://???????????????????????????
                IceWaterReadTempAction.getInstance().readTemp();
                break;
        }
    }

    /**
     * author:sqq  date: 2021/1/20 0023
     * ??????????????????
     * state 0 ?????? ???1 ??????
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
     * ???????????????
     */
    @Override
    public void requestResultTwo(final String state, String message, final Object data) {
        if (isFinishing() || mPresenter == null) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case "CoffeeState"://???????????????
                        CoffeeMachineState machineState = (CoffeeMachineState) data;

                        binding.coffeeRefreshTimeTv.setText(TimeUtil.getCurrentTime());
                        //????????????
                        binding.coffeeCurrentWaterTempTv.setText(MessageFormat.format("{0}???", machineState.getCurrentWaterTemp()));
                        //????????????
                        binding.coffeeMaxWaterTempTv.setText(MessageFormat.format("{0}???", machineState.getMaxWaterTemp()));
                        //????????????
                        binding.coffeeMinWaterTempTv.setText(MessageFormat.format("{0}???", machineState.getMinWaterTemp()));
                        //????????????
                        binding.coffeeSurplusWaterYieldTv.setText(MessageFormat.format("{0}ml", machineState.getRemainingWater()));
                        //????????????
                        binding.coffeeErrorCodeTv.setText(MessageFormat.format("{0}-{1}  ({2})", machineState.getErrorType(), machineState.getErrorNum(),
                                CoffeeStateInfo.getCurrentErrorInfo(machineState.getErrorCode(), machineState.getErrorType(), machineState.getErrorNum())));
                        //????????????
                        binding.coffeeActionTv.setText(MessageFormat.format("{0}  ({1})", machineState.getCurrentRunAction(),
                                CoffeeStateInfo.getCurrentActionInfo(machineState.getCurrentRunAction())));
                        //??????
                        binding.coffeeResultTv.setText(MessageFormat.format("{0}  ({1})", machineState.getRunResult(),
                                CoffeeStateInfo.getMakeResultInfo(machineState.getRunResult())));
                        //????????????:?????????????????????
                        binding.coffeeMachineTv.setText(MessageFormat.format("{0}+{1}+{2}  ({3}+{4}+{5})", machineState.isHasCup() ? "1" : "0",
                                machineState.isCupDoorOpen() ? "1" : "0", machineState.isCabinetOpen() ? "1" : "0",
                                CoffeeStateInfo.getCupInfo(machineState.isHasCup()), CoffeeStateInfo.getCupDoorInfo(machineState.isCupDoorOpen()),
                                CoffeeStateInfo.getBigDoorInfo(machineState.isCabinetOpen())));
                        //????????????(????????????+???????????????)
                        binding.coffeeSettingTv.setText(MessageFormat.format("{0}+{1}  ({2}+{3})", machineState.isHasInfraredMode() ? "0" : "1",
                                machineState.isHasCupDoorMode() ? "0" : "1", CoffeeStateInfo.getInfraredInfo(machineState.isHasInfraredMode()),
                                CoffeeStateInfo.getDoorInfo(machineState.isHasCupDoorMode())));

                        break;
                    case "IceState"://????????????
                        IceTwoState iceTwoState = (IceTwoState) data;
                        binding.iceRefreshTimeTv.setText(TimeUtil.getCurrentTime());

                        binding.iceTempTv.setText(MessageFormat.format("{0}???", iceTwoState.getOutletTemperature()));
                        binding.iceResultTv.setText(MessageFormat.format("{0}  {1}", String.valueOf(iceTwoState.getRunResult()), IceStateInfo.getRunResultInfo(iceTwoState.getRunResult())));

                        String stateCode = iceTwoState.getMachineErrorCode() + "+" + iceTwoState.getMachineIsFullIce();
                        binding.iceStateTv.setText(MessageFormat.format("{0}  {1}", stateCode, IceStateInfo.getErrorInfo(iceTwoState.getMachineErrorCode()) + "+" + IceStateInfo.getIceInfo(iceTwoState.getMachineIsFullIce())));
                        binding.iceWeightTv.setText(MessageFormat.format("{0}g", iceTwoState.getCurrentWeight() / 10f));

                        break;
                    case "IceMotorState"://??????????????????
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
     * ????????????
     */
    @Override
    public void requestResultThree(final String state, final String message, Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case "makeSuccess-waitTakeCup"://???????????? ????????????
                        textDialog.setText(getString(R.string.please_take_cup), Color.GREEN);
                        break;
                    case "waitCloseDoor"://????????? ????????????
                        textDialog.setText(getString(R.string.close_cup_door), Color.GREEN);
                        break;
                    case "makeFailure"://????????????
                        textDialog.setText(message, Color.RED);
                        mPresenter.getCoffeePresenter().endMakeDrink();
                        break;
                    case "makeSuccess-makeEnd"://??????(????????????)
                    case "otherException"://??????(???????????????)
                        textDialog.dismiss();
                        mPresenter.getCoffeePresenter().endMakeDrink();
                        break;
                }
            }
        });
    }

    /**
     * ????????????
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
