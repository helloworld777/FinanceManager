package com.lu.momeymanager.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2016/4/6.
 */
public class User extends BmobObject{
    public String username;
    public User(String username){
        setTableName("luser");
        this.username=username;
    }
}
