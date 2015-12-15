package com.lu.momeymanager.fragment;

import com.lu.momeymanager.R;
import com.lu.momeymanager.activity.BalanceQueryActivity;
import com.lu.momeymanager.activity.SettingActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MenuFragment extends BaseFragment{
	private LinearLayout llSetting,llBalance;
	private ViewClick viewClick;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		viewClick=new ViewClick();
		View view=inflater.inflate(R.layout.fragment_menu, container, false);
		
		llSetting=findViewById(view, R.id.llSetting);
		llSetting.setOnClickListener(viewClick);
		llBalance=findViewById(view, R.id.llBalance);
		llBalance.setOnClickListener(viewClick);
		return view;
	}
	class ViewClick implements OnClickListener{

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.llSetting:
				startActivity(SettingActivity.class);
				break;
			case R.id.llBalance:
				startActivity(BalanceQueryActivity.class);
				break;
			default:
				break;
			}
		}
		
	}
}
