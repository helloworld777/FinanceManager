package com.lu.momeymanager.model;

import android.content.Context;

import com.lu.momeymanager.bean.InOutBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lenovo on 2016/4/6.
 */
public class InOutModel extends BaseModel{

    AddCallBack addCallBack;
    public InOutModel setAddCallBack(AddCallBack addCallBack){
        this.addCallBack=addCallBack;
        return this;
    }

    public void addInOut(Context context,List<InOutBean> inOutBeanList){
        List<BmobObject> inouts = new ArrayList<>();
        inouts.addAll(inOutBeanList);
        new BmobObject().insertBatch(context,inouts,new SaveListener(){
            @Override
            public void onSuccess() {
                d("addInOut-->onsuccess");
                if(addCallBack!=null){
                    addCallBack.addSuccess();
                }
            }
            @Override
            public void onFailure(int i, String s) {
                d("addInOut-->onFailure i:"+i+",s:"+s);
                if(addCallBack!=null){
                    addCallBack.addFailure();
                }
            }
        });
    }
    public interface AddCallBack{
        void addSuccess();
        void addFailure();
    }
}
