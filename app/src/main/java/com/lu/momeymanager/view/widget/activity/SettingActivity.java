package com.lu.momeymanager.view.widget.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.financemanager.R;
import com.lu.momeymanager.manager.ExcelManager;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.view.widget.SildingFinishLayout;
import com.lu.momeymanager.view.widget.activity.gesturepassword.CreateGesturePasswordActivity;

//@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseHeaderActivity {
	@ViewInject(value = R.id.ivMore)
	protected ImageView ivMore;
	@ViewInject(value = R.id.ivBack)
	protected ImageView ivBack;

	@ViewInject(value = R.id.tvTitle)
	protected TextView tvTitle;

	@ViewInject(value = R.id.tvLockpasswordstate)
	protected TextView tvLockpasswordstate;
	@ViewInject(value = R.id.btnRefresh)
	protected Button btnRefresh;

	@ViewInject(value = R.id.rlLockpasswordstate)
	protected RelativeLayout rlLockpasswordstate;
	@ViewInject(value = R.id.rlToExcel)
	protected RelativeLayout rlToExcel;

//	@ViewInject(R.id.sildingFinishLayout)
//	private SildingFinishLayout sildingFinishLayout;
	@Override
	@TargetApi(19)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SildingFinishLayout sildingFinishLayout = (SildingFinishLayout) LayoutInflater.from(this).inflate(
				R.layout.base, null);
		sildingFinishLayout.attachToActivity(this);

		setContentView(R.layout.activity_setting);
//		initWidget();
	}

	@com.lidroid.xutils.view.annotation.event.OnClick({ R.id.rlLockpasswordstate ,R.id.ivBack,R.id.rlToExcel})
	public void viewClick(View view) {
		switch (view.getId()) {
		case R.id.rlLockpasswordstate:
			startActivity(CreateGesturePasswordActivity.class);
			break;
		case R.id.ivBack:
			finish();
			break;
		case R.id.rlToExcel:
			String path=ExcelManager.excelToDisk(InOutBeanManager.getDefault().getSimilarDateMoneyBeans());
			if(path!=null){
				DialogUtil.showToast(getBaseContext(), "save success path:"+path);
			}else{
				DialogUtil.showToast(getApplicationContext(), "save faild");
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		tvLockpasswordstate.setText(SaveDataUtil.getAppLockState(this) == 1 ? getString(R.string.already_setting) : getString(R.string.no_setting));
	}

	private void initWidget() {
		ivMore.setVisibility(View.GONE);
		tvTitle.setText(getString(R.string.setting));
		btnRefresh.setVisibility(View.GONE);
	}
}
