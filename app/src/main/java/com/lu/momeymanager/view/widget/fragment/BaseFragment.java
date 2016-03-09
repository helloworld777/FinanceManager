package com.lu.momeymanager.view.widget.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;


public class BaseFragment extends Fragment{
	protected Context mContext;
	@SuppressWarnings("unchecked")
	protected <T> T findViewById(View view, int res) {
		return (T) view.findViewById(res);
	}
	
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(getActivity(),clazz));
	}
}
