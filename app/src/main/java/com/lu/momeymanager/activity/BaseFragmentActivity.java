package com.lu.momeymanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;

public abstract class BaseFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		
		initData();
		initWidget();
	}
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this,clazz));
	}
	protected abstract void initWidget();
	protected abstract void initData();
}
