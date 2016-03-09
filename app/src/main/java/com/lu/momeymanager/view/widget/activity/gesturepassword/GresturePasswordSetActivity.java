package com.lu.momeymanager.view.widget.activity.gesturepassword;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.financemanager.R;
import com.lu.momeymanager.view.widget.activity.BaseHeaderActivity;
import com.lu.momeymanager.util.SaveDataUtil;

@ContentView(value = R.layout.activity_gesturepassword_set)
public class GresturePasswordSetActivity extends BaseHeaderActivity {

	@ViewInject(value = R.id.cb_lock)
	private CheckBox cb_lock;

	@ViewInject(value = R.id.rl_updatelockpassword)
	private RelativeLayout rl_updatelockpassword;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initWidget();
	}
	protected void initWidget() {
		ivMore.setVisibility(View.GONE);
		tvTitle.setText("������������");
		cb_lock.setChecked(SaveDataUtil.getAppLockState(this)==1?true:false);
		
		rl_updatelockpassword.setVisibility(cb_lock.isChecked()?View.VISIBLE:View.INVISIBLE);
		
		cb_lock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				rl_updatelockpassword.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
				SaveDataUtil.setAppLock(GresturePasswordSetActivity.this, isChecked?1:2);
				SaveDataUtil.setAppToBack(GresturePasswordSetActivity.this, 0);
				
			}
		});
	}

	@OnClick({ R.id.rl_lock, R.id.rl_updatelockpassword })
	public void myOnclik(View view) {
		switch (view.getId()) {
		case R.id.rl_lock:
			cb_lock.setChecked(!cb_lock.isChecked());
			rl_updatelockpassword.setVisibility(cb_lock.isChecked()?View.VISIBLE:View.INVISIBLE);
			SaveDataUtil.setAppLock(this, cb_lock.isChecked()?1:2);
			SaveDataUtil.setAppToBack(this, 0);
			break;
		case R.id.rl_updatelockpassword:
			startActivity(new Intent(this,CreateGesturePasswordActivity.class));
			break;
		default:
			break;
		}
	}
}
