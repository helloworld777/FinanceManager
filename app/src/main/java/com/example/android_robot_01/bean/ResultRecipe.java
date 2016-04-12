package com.example.android_robot_01.bean;

/**
 * Created by lenovo on 2016/4/9.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 菜谱
 */
public class ResultRecipe extends Result{

    public List<Recipe> list=new ArrayList<>();
    class Recipe{
        public String nema,icon,info,detailurl;
    }
}
