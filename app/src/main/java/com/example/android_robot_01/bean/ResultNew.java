package com.example.android_robot_01.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/4/9.
 */
public class ResultNew extends Result{

    public List<New> list=new ArrayList<>();

    class New{
        public String aritle,source,icon,detailurl;
    }
}
