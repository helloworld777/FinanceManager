package com.lu.momeymanager.model;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lu.momeymanager.app.MomeyManagerApplication;
import com.lu.momeymanager.util.Constant;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.util.TopNoticeDialog;


/**
 * Created by lenovo on 2016/3/21.
 */
public class BaseModel implements Constant {
    protected HttpUtils httpUtils;
    int TIME_OUT=5000;
    protected RequestParams params;
    public BaseModel(){
        httpUtils=new HttpUtils(TIME_OUT);
    }
    protected void postRequest(String url, RequestParams params , RequestCallBack<String> callBack){
        httpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                callBack);
    }
    protected void getRequest(String url, RequestParams params , RequestCallBack<String> callBack){
        httpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                callBack);
    }
    protected void d(String msg){
        LogUtil.d(this,"..............."+msg);
    }
    protected void toast(String msg){
        TopNoticeDialog.showToast(MomeyManagerApplication.getDefault(),msg);
    }
}
