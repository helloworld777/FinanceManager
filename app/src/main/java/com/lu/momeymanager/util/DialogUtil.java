package com.lu.momeymanager.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.lu.financemanager.R;

public class DialogUtil {
	private static AlertDialog dialog=null;
	public static void showToast(Context context,String msg){
		TopNoticeDialog.showToast(context,msg);
	}

	public static void showBackupDialog(Context context,final OkListener oilPriceClickOk){
		showCustomDialog(context,R.layout.popup_oil_price,oilPriceClickOk);
	}
//	public static void showBackupDialog(Context context,final OkListener oilPriceClickOk){
//		showCustomDialog(context,R.layout.popup_oil_price,oilPriceClickOk);
//	}
	public static void showCustomDialog(Context context,int layoutRes,final OkListener oilPriceClickOk){
		if(!isActivityNotFinished(context)){
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog=builder.show();
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.getWindow().setContentView(layoutRes);
		dialog.getWindow().findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeAlertDialog();
			}
		});
		final EditText editText= (EditText) dialog.getWindow().findViewById(R.id.etOilPrice);
		String username= (String) SPUtils.get(context,Constant.USERNAME,"");
		if(!TextUtils.isEmpty(username)){
			editText.setText(username);
		}
		dialog.getWindow().findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				oilPriceClickOk.onOkClick(editText.getText().toString());
				closeAlertDialog();
			}
		});
	}
	public interface OkListener{
		void onOkClick(String oilPrice);
	}
	private static boolean isActivityNotFinished(Context context){

		if(context==null) return false;

		if(context instanceof Activity){
			Activity activity= (Activity) context;
			return !activity.isFinishing();
		}
		return false;
	}
	public static  void showAlertDialog(Context context,String title,String[] items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}

	public static  void showAlertDialog(Context context,String title,int items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}

	public static void closeAlertDialog(){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
			dialog=null;
		}
	}

	public static void showExitAlertDialog(Context context,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("exit").setMessage("Are you sure?")
		.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				closeAlertDialog();
			}
		})
		.setPositiveButton("ok", dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	public static void showMsgAlertDialog(Context context,String title,String msg,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(msg)
		.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				closeAlertDialog();
			}
		})
		.setPositiveButton("ok", dialogInterface);
		dialog=builder.create();
		dialog.show();
	}

	
	public static void showWaitDialog(Context context){
		dialog=ProgressDialog.show(context, "wait", "please....");
		dialog.show();
	}
	public static void showWaitDialog(Context context,String title,String message){
		dialog=ProgressDialog.show(context, title, message);
		dialog.show();
	}

}
