package com.lu.momeymanager.app;

import android.app.Application;

import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.Constant;
import com.lu.momeymanager.util.SPUtils;
import com.lu.momeymanager.util.StringUtil;
import com.lu.momeymanager.view.widget.lockpatternview.LockPatternUtils;

import cn.bmob.v3.Bmob;

public class MomeyManagerApplication extends Application {
	private LockPatternUtils mLockPatternUtils;
	private static MomeyManagerApplication managerApplication;
	private String username;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		managerApplication=this;
		setmLockPatternUtils(new LockPatternUtils(this));
//		CrashHandler.getInstance().init(getApplicationContext());


		StringUtil.initString(getApplicationContext());
		InOutBeanManager.init(this);

		Bmob.initialize(this, Constant.Bmob_APPID);

//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//		username=TelephonyMgr.getDeviceId();
		username= (String) SPUtils.get(this,Constant.USERNAME,"");
	}

	public String getUsername(){
//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		return username;
	}
	public void setUsername(String s){
//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		 username=s;
	}
	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	public static MomeyManagerApplication getDefault(){
		return managerApplication;
	}
	public void setmLockPatternUtils(LockPatternUtils mLockPatternUtils) {
		this.mLockPatternUtils = mLockPatternUtils;
	}
	public static MomeyManagerApplication getInstance(){
		return managerApplication;
	}
}
