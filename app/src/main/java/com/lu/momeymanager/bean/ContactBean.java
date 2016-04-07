package com.lu.momeymanager.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2016/4/7.
 */
public class ContactBean extends BmobObject{
    public String contactName,phoneNumber,username;
    public BmobFile contactPhoto;
    public ContactBean(){
        setTableName("contact");
    }
    public ContactBean(String contactName,String phoneNumber){
        this();
        this.contactName=contactName;
        this.phoneNumber=phoneNumber;
    }

}
