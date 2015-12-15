package com.lu.momeymanager.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lu.momeymanager.R;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;

import de.greenrobot.event.EventBus;

public class PopupWindowUtil {
	/**
	 * 弹出窗体
	 */
	private PopupWindow popupWindow;
	
	private Context mContext;
	
	private PopupWindowUI popupWindowUI;
	
	public PopupWindowUtil (Context mContext ){
		this.mContext=mContext;
	}
	
	/**
	 * 显示弹出窗体
	 * 
	 * @param parent
	 *            在该控件下显示弹出窗体
	 */
	@SuppressWarnings("deprecation")
	public void showWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.popupwindow, null);
			initPopupWindowUI(view);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			
		}
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击空白的地方关闭PopupWindow
		popupWindow.showAsDropDown(parent);
//		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.update();
	
	}
	
	/**
	 * 初始化弹出窗体的控件
	 * 
	 * @param view
	 */
	private void initPopupWindowUI(View view) {
		
		
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		
		popupWindowUI = new PopupWindowUI();
		popupWindowUI.llSortByTime = (LinearLayout) view
				.findViewById(R.id.llSortByTime);

		popupWindowUI.llSortByBank = (LinearLayout) view
				.findViewById(R.id.llSortByBank);
		
		popupWindowUI.ivSortByBank=(ImageView) view.findViewById(R.id.ivSortByBank);
		popupWindowUI.ivSortByTime=(ImageView) view.findViewById(R.id.ivSortByTime);
	
		
		popupWindowUI.llSortByTime.setOnClickListener(click);
		popupWindowUI.llSortByBank.setOnClickListener(click);
	}
	class PopupWindowUI {
		ImageView ivSortByTime;
		ImageView ivSortByBank;
		LinearLayout llSortByTime;
		LinearLayout llSortByBank;
	}
	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.llSortByTime:
				sort(true);
				EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_MAIN, SimilarDateMoneyBean.TYPE_MOUTH));
				break;
			case R.id.llSortByBank:
				sort(false);
				EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_MAIN, SimilarDateMoneyBean.TYPE_BANK));
				break;
			default:
				break;
			}
		}
	};
	private void sort(boolean time){
		popupWindowUI.ivSortByBank.setVisibility(time?View.INVISIBLE:View.VISIBLE);
		popupWindowUI.ivSortByTime.setVisibility(time?View.VISIBLE:View.INVISIBLE);	
	}
}
