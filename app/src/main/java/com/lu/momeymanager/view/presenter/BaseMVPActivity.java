package com.lu.momeymanager.view.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class BaseMVPActivity<V,P extends BasePresenter<V>> extends Activity {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter=createPresenter();
        if (presenter!=null){
            presenter.attachView((V)this);
        }
    }
    /**
     * 创建一个与之关联的Presenter
     * @return
     */
    protected  P  createPresenter(){
        return null;
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.detachView();
        }

    }
}
