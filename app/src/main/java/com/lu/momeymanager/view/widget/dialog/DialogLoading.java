package com.lu.momeymanager.view.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.lu.financemanager.R;


/**
 * @ClassName: DialogLoading
 * @Description: 得到自定义的progressDialog
 * @author zyx
 * @date 2014-7-8 上午9:46:36
 * 
 */
public class DialogLoading extends AlertDialog {
	private Context mContext;
	private TextView tvDialogLoading;
	private String message = null;
	private ProgressWheel progressWheel ;

	public DialogLoading(Context context) {
		super(context);
		message = "wait";
		this.mContext = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	}

	public DialogLoading(Context context, String message) {
		super(context);
		this.message = message;
		this.mContext = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	}

	public DialogLoading(Context context, int theme, String message) {
		super(context, theme);
		this.message = message;
		this.mContext = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_loading);
		progressWheel= (ProgressWheel) findViewById(R.id.progress_wheel);
		//自动旋转
        progressWheel.spin();
        //设置边缘Bar条颜色
        progressWheel.setBarColor(Color.RED);
//        progressWheel.setBarColor(getContext().getResources().getColor(R.color.main_color));
        
        //设置wheel颜色
        progressWheel.setRimColor(Color.LTGRAY);
        
		tvDialogLoading = (TextView) findViewById(R.id.tvDialogLoading);
		tvDialogLoading.setText(this.message);
	}

	@Override
	public void show() {
		if(mContext instanceof Activity){

			Activity activity= (Activity) mContext;
			if(!activity.isFinishing()){

				super.show();
			}

		}

	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if(mContext instanceof Activity){
			Activity activity= (Activity) mContext;
			if(!activity.isFinishing()){

				super.dismiss();
				if(progressWheel!=null){
					progressWheel.stopSpinning();
					progressWheel=null;
				}
			}
		}

	}

	public void setText(String message) {
		try {
			if (message != null && tvDialogLoading != null) {
				this.message = message;
				tvDialogLoading.setText(this.message);
			}
		} catch (Exception e) {
		}
	}

	public void setText(int resId) {
		setText(getContext().getResources().getString(resId));
	}

}
