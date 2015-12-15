package com.lu.momeymanager.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.momeymanager.R;
import com.lu.momeymanager.activity.gesturepassword.UnlockGesturePasswordActivity;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.fragment.DetailMouthMoneyFragment;
import com.lu.momeymanager.fragment.MouthMoneyFragment;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.util.SaveDataUtil;
import com.lu.momeymanager.widget.popupwindow.PopupWindowUtil;

import de.greenrobot.event.EventBus;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity {
	private static final String TAG = "MainActivity";
	private Fragment fragment;
	private FragmentManager fm;
	private FragmentTransaction transaction;
	@ViewInject(value = R.id.tvTitle)
	private TextView tvTitle;

	@ViewInject(value = R.id.ivBack)
	private ImageView ivBack;
	@ViewInject(value = R.id.ivMore)
	private ImageView ivMore;
	
	@ViewInject(value=R.id.btnRefresh)
	private Button btnRefresh;
	private PopupWindowUtil pWindowUtil;
	
	@ViewInject(R.id.slidingpanellayout)
	private SlidingPaneLayout sliding;
	@Override
	protected void initData() {
		InOutBeanManager.getDefault().getSmsFromPhone();
		EventBus.getDefault().register(this);
	}

	@TargetApi(19) 
	@SuppressLint("Recycle")
	@Override
	protected void initWidget() {
		
		// Í¸Ã÷×´Ì¬À¸
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		// //Í¸Ã÷µ¼º½À¸
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		
		fragment = new MouthMoneyFragment();
		
		fm = getSupportFragmentManager();

		replaceFragment(false);
		homeTitle();
		pWindowUtil=new PopupWindowUtil(this);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(SaveDataUtil.getAppLockState(this)==1 && SaveDataUtil.getAppToBack(getApplicationContext())==1){
			
//			startActivity(UnlockGesturePasswordActivity.class);
			startActivityForResult(new Intent(this,UnlockGesturePasswordActivity.class), 0);
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		
		
		if(arg0==0 && arg1==0){
			
			finish();
			
		}
	}
	private void homeTitle(){
		tvTitle.setText(getString(R.string.all_consume));
		ivBack.setVisibility(View.GONE);
		ivMore.setVisibility(View.VISIBLE);
		btnRefresh.setVisibility(View.VISIBLE);
		fragment=fm.findFragmentById(R.id.frame_main);
	}
	private void replaceFragment(boolean isHome) {
		transaction = fm.beginTransaction();
		if (isHome) {
			transaction.addToBackStack(null);
		}
		transaction.setCustomAnimations(R.anim.anim_enter_right, R.anim.anim_leave_left, R.anim.anim_enter_left, R.anim.anim_leave_right);
		transaction.replace(R.id.frame_main, fragment);
		transaction.commit();
		
//		LogUtil.d(TAG, "SIZE:");
	}

	@OnClick({ R.id.ivBack ,R.id.ivMore,R.id.btnRefresh})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ivBack:
			fm.popBackStack();
			homeTitle();
			break;
		case R.id.ivMore:
			pWindowUtil.showWindow(ivMore);
			break;
		case R.id.btnRefresh:
//			InOutBeanManager.getDefault().refresh();
			EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_MAIN, SimilarDateMoneyBean.TYPE_MOUTH));
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(BaseEvent baseEvent) {
		switch (baseEvent.getEventType()) {
		case BaseEvent.CHANGE_FRAGMENT:
			changeFragment(baseEvent);
			break;
		default:
			break;
		}
		
	}
	private void changeFragment(BaseEvent baseEvent){
		int position = (Integer) baseEvent.getData();
		fragment = DetailMouthMoneyFragment.newInstance(position);
		replaceFragment(true);
		tvTitle.setText(InOutBeanManager.getDefault().getSimilarDateMoneyBeans().get(position).getDate());
		ivBack.setVisibility(View.VISIBLE);
		ivMore.setVisibility(View.GONE);
		btnRefresh.setVisibility(View.GONE);
		
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		homeTitle();
//		fragment = new MouthMoneyFragment();
//		replaceFragment(false);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		LogUtil.d(TAG, "********************onDestroy 111111111111111111111111");
		SaveDataUtil.setAppToBack(getApplicationContext(), 1);
	
	}
}
