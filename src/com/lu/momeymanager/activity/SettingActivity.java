package com.lu.momeymanager.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.momeymanager.R;
import com.lu.momeymanager.activity.gesturepassword.CreateGesturePasswordActivity;
import com.lu.momeymanager.manager.ExcelManager;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.util.SaveDataUtil;

@ContentView(R.layout.activity_setting)
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

	@Override
	@TargetApi(19)
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initWidget();
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
				DialogUtil.showToast(getBaseContext(), "导出成功,路径:"+path);
			}else{
				DialogUtil.showToast(getApplicationContext(), "导出失败");
			}
			break;
		default:
			break;
		}
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		ivMore.setVisibility(View.GONE);
		tvTitle.setText(getString(R.string.setting));
		btnRefresh.setVisibility(View.GONE);

		tvLockpasswordstate.setText(SaveDataUtil.getAppLockState(this) == 1 ? getString(R.string.already_setting) : getString(R.string.no_setting));

	}
}
