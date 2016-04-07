package com.lu.momeymanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lidroid.xutils.ViewUtils;
import com.lu.momeymanager.util.Debug;
import com.lu.momeymanager.util.TopNoticeDialog;
import com.lu.momeymanager.view.dialog.DialogLoading;

public abstract class BaseFragmentActivity extends FragmentActivity {
	protected DialogLoading dialogLoading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		dialogLoading=new DialogLoading(this);
		initData();
		initWidget();
	}
	protected void d(String msg){
		Debug.d(this,"...................."+msg);
	}
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this,clazz));
	}
	protected abstract void initWidget();
	protected abstract void initData();
	protected void toast(String msg){
		TopNoticeDialog.showToast(this,msg);
	}
}
