package com.lu.momeymanager.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;


public class BaseFragment extends Fragment{
	@SuppressWarnings("unchecked")
	protected <T> T findViewById(View view, int res) {
		return (T) view.findViewById(res);
	}
	
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(getActivity(),clazz));
	}
}
