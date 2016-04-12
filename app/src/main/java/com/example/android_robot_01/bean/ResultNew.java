package com.example.android_robot_01.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/4/9.
 */
public class ResultNew extends Result implements Serializable{

    public List<New> list=new ArrayList<>();

    public class New implements Serializable{
        public String article,source,icon,detailurl;
    }
}
