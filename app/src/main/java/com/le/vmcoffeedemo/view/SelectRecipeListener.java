package com.le.vmcoffeedemo.view;

import com.le.vmcoffeedemo.bean.Recipe;

import java.util.List;

/**
 * Created by sqq on 2021/9/27 0027
 */
public interface SelectRecipeListener {
    void selectRecipe(List<Recipe> recipes);
}
