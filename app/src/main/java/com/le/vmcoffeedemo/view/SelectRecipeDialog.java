package com.le.vmcoffeedemo.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.le.vmcoffeedemo.R;
import com.le.vmcoffeedemo.bean.Recipe;
import com.le.vmcoffeedemo.databinding.DialogSelectRecipeBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by sqq on 2021/9/27 0027
 */
public class SelectRecipeDialog extends Dialog implements View.OnClickListener {
    private final DialogSelectRecipeBinding binding;
    private List<Recipe> recipes;
    private SelectRecipeListener selectRecipeListener;
    
    public SelectRecipeDialog setSelectRecipeListener(SelectRecipeListener selectRecipeListener) {
        this.selectRecipeListener = selectRecipeListener;
        return this;
    }
    
    public SelectRecipeDialog(@NonNull Context context) {
        super(context);
        binding = DialogSelectRecipeBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        
        binding.selectRecipeCancelBt.setOnClickListener(this);
        binding.selectRecipeOKBt.setOnClickListener(this);
    }
    
    public SelectRecipeDialog initData(List<Recipe> recipeList) {
        recipes = new ArrayList<>();
        
        //配方5步
        if (recipeList.size() != 5) {
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
        } else
            this.recipes.addAll(recipeList);
        
        binding.recipeMaterial1Sp.setSelection(recipes.get(0).getMaterialCode() == 9 ? 6 : recipes.get(0).getMaterialCode());
        binding.recipeWaterYield1Et.setText(String.valueOf(recipes.get(0).getOutWaterYield()));
        binding.recipeOutMaterialTime1Et.setText(String.valueOf(recipes.get(0).getOutMaterialTime()));
        binding.recipeStirTime1Et.setText(String.valueOf(recipes.get(0).getStirTime()));
        
        binding.recipeMaterial2Sp.setSelection(recipes.get(1).getMaterialCode() == 9 ? 6 : recipes.get(1).getMaterialCode());
        binding.recipeWaterYield2Et.setText(String.valueOf(recipes.get(1).getOutWaterYield()));
        binding.recipeOutMaterialTime2Et.setText(String.valueOf(recipes.get(1).getOutMaterialTime()));
        binding.recipeStirTime2Et.setText(String.valueOf(recipes.get(1).getStirTime()));
        
        binding.recipeMaterial3Sp.setSelection(recipes.get(2).getMaterialCode() == 9 ? 6 : recipes.get(2).getMaterialCode());
        binding.recipeWaterYield3Et.setText(String.valueOf(recipes.get(2).getOutWaterYield()));
        binding.recipeOutMaterialTime3Et.setText(String.valueOf(recipes.get(2).getOutMaterialTime()));
        binding.recipeStirTime3Et.setText(String.valueOf(recipes.get(2).getStirTime()));
        
        binding.recipeMaterial4Sp.setSelection(recipes.get(3).getMaterialCode() == 9 ? 6 : recipes.get(3).getMaterialCode());
        binding.recipeWaterYield4Et.setText(String.valueOf(recipes.get(3).getOutWaterYield()));
        binding.recipeOutMaterialTime4Et.setText(String.valueOf(recipes.get(3).getOutMaterialTime()));
        binding.recipeStirTime4Et.setText(String.valueOf(recipes.get(3).getStirTime()));
        
        binding.recipeMaterial5Sp.setSelection(recipes.get(4).getMaterialCode() == 9 ? 6 : recipes.get(4).getMaterialCode());
        binding.recipeWaterYield5Et.setText(String.valueOf(recipes.get(4).getOutWaterYield()));
        binding.recipeOutMaterialTime5Et.setText(String.valueOf(recipes.get(4).getOutMaterialTime()));
        binding.recipeStirTime5Et.setText(String.valueOf(recipes.get(4).getStirTime()));
        
        return this;
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectRecipeCancelBt:
                dismiss();
                break;
            case R.id.selectRecipeOKBt:
                onSelectRecipeOK();
                break;
        }
    }
    
    public void onSelectRecipeOK() {
        recipes.clear();
        recipes.add(new Recipe(binding.recipeMaterial1Sp.getSelectedItemPosition() == 6 ? 9 : binding.recipeMaterial1Sp.getSelectedItemPosition(),
                stringToDouble(binding.recipeOutMaterialTime1Et.getText().toString()),
                stringToInteger(binding.recipeWaterYield1Et.getText().toString()),
                stringToDouble(binding.recipeStirTime1Et.getText().toString())));
        recipes.add(new Recipe(binding.recipeMaterial2Sp.getSelectedItemPosition() == 6 ? 9 : binding.recipeMaterial2Sp.getSelectedItemPosition(),
                stringToDouble(binding.recipeOutMaterialTime2Et.getText().toString()),
                stringToInteger(binding.recipeWaterYield2Et.getText().toString()),
                stringToDouble(binding.recipeStirTime2Et.getText().toString())));
        recipes.add(new Recipe(binding.recipeMaterial3Sp.getSelectedItemPosition() == 6 ? 9 : binding.recipeMaterial3Sp.getSelectedItemPosition(),
                stringToDouble(binding.recipeOutMaterialTime3Et.getText().toString()),
                stringToInteger(binding.recipeWaterYield3Et.getText().toString()),
                stringToDouble(binding.recipeStirTime3Et.getText().toString())));
        recipes.add(new Recipe(binding.recipeMaterial4Sp.getSelectedItemPosition() == 6 ? 9 : binding.recipeMaterial4Sp.getSelectedItemPosition(),
                stringToDouble(binding.recipeOutMaterialTime4Et.getText().toString()),
                stringToInteger(binding.recipeWaterYield4Et.getText().toString()),
                stringToDouble(binding.recipeStirTime4Et.getText().toString())));
        recipes.add(new Recipe(binding.recipeMaterial5Sp.getSelectedItemPosition() == 6 ? 9 : binding.recipeMaterial5Sp.getSelectedItemPosition(),
                stringToDouble(binding.recipeOutMaterialTime5Et.getText().toString()),
                stringToInteger(binding.recipeWaterYield5Et.getText().toString()),
                stringToDouble(binding.recipeStirTime5Et.getText().toString())));
        
        if (selectRecipeListener != null)
            selectRecipeListener.selectRecipe(recipes);
        
        dismiss();
    }
    
    private double stringToDouble(String s) {
        return TextUtils.isEmpty(s) ? 0 : Double.parseDouble(s);
    }
    
    private int stringToInteger(String s) {
        return TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);
    }
}
