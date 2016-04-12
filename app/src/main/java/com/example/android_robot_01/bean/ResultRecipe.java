package com.example.android_robot_01.bean;

/**
 * Created by lenovo on 2016/4/9.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜谱
 */
public class ResultRecipe extends Result implements Serializable {

    public List<Recipe> list=new ArrayList<>();
    public class Recipe implements Serializable{
        public String name,icon,info,detailurl;
    }
}
