package com.lu.momeymanager.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;

public class BaseActivity extends Activity {


	protected  ImageView ivMore;
	protected ImageView ivBack;
	protected TextView tvTitle;


	@TargetApi(19) @Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this,clazz));
	}
}
