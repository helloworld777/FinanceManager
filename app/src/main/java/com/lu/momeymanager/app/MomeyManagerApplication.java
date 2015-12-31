package com.lu.momeymanager.app;

import android.app.Application;

import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.StringUtil;
import com.lu.momeymanager.widget.lockpatternview.LockPatternUtils;

public class MomeyManagerApplication extends Application {
	private LockPatternUtils mLockPatternUtils;
	private static MomeyManagerApplication managerApplication;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		managerApplication=this;
		setmLockPatternUtils(new LockPatternUtils(this));
//		CrashHandler.getInstance().init(getApplicationContext());


		StringUtil.initString(getApplicationContext());
		InOutBeanManager.init(this);
		

	}
	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	public void setmLockPatternUtils(LockPatternUtils mLockPatternUtils) {
		this.mLockPatternUtils = mLockPatternUtils;
	}
	public static MomeyManagerApplication getInstance(){
		return managerApplication;
	}
}
