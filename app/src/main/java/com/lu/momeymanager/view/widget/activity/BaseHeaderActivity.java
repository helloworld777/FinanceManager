package com.lu.momeymanager.view.widget.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.financemanager.R;

/**
 * @author Administrator
 *
 */
public class BaseHeaderActivity extends BaseActivity{
	
	
	@ViewInject(value = R.id.ivMore)
	protected ImageView ivMore;
	@ViewInject(value = R.id.ivBack)
	protected ImageView ivBack;

	@ViewInject(value = R.id.tvTitle)
	protected TextView tvTitle;
	
	@com.lidroid.xutils.view.annotation.event.OnClick({R.id.ivBack})
	public void OnClick(View view){
		if(view.getId()==R.id.ivBack){
			finish();
		}
	}
}
