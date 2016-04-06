package com.lu.momeymanager.view.widget.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lu.momeymanager.util.Debug;
import com.lu.momeymanager.util.TopNoticeDialog;
import com.lu.momeymanager.view.widget.dialog.DialogLoading;


public class BaseFragment extends Fragment{
	protected DialogLoading dialogLoading;
	protected Context mContext;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		dialogLoading=new DialogLoading(context);
	}

	@SuppressWarnings("unchecked")
	protected <T> T findViewById(View view, int res) {
		return (T) view.findViewById(res);
	}
	
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(getActivity(),clazz));
	}
	protected void showToast(String msg) {
		TopNoticeDialog.showToast(getActivity(), msg);
	}
	protected void d(String msg){
		Debug.d(this,".........msg:"+msg);
	}
}
