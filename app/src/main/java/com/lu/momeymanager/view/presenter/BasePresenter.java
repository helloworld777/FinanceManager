package com.lu.momeymanager.view.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class BasePresenter<V> {

    protected WeakReference<V>  mReference;
    public BasePresenter(){

    }
    public void attachView(V baseView){
        this.mReference=  new WeakReference<V>(baseView);
    }
    public void detachView(){
        if (mReference!=null){
            mReference.clear();
        }
    }
    protected V getView(){
        return mReference.get();
    }
}
