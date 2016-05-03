package com.lu.momeymanager.view.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lu.momeymanager.util.Debug;
import com.lu.momeymanager.util.TopNoticeDialog;
import com.lu.momeymanager.view.dialog.DialogLoading;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragmentActivity extends FragmentActivity {
	protected DialogLoading dialogLoading;
	protected Activity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		dialogLoading=new DialogLoading(this);
		mActivity=this;
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
	protected void viewClick(View view){};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	public void copeData(String string){
		ClipboardManager myClipboard;
		myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		ClipData myClip;
		myClip = ClipData.newPlainText("text", string);
		myClipboard.setPrimaryClip(myClip);
	}
	public String pasteData(){
		ClipboardManager myClipboard;
		myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		ClipData abc = myClipboard.getPrimaryClip();
		ClipData.Item item = abc.getItemAt(0);
		return item.getText().toString();
	}
}
