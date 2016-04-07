package com.lu.momeymanager.view.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lu.financemanager.R;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.util.Constant;

import de.greenrobot.event.EventBus;

public class PopupWindowUtil implements Constant {
    private PopupWindow popupWindow;

    private Context mContext;

    private PopupWindowUI popupWindowUI;

    public PopupWindowUtil(Context mContext) {
        this.mContext = mContext;
    }

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
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //
        popupWindow.showAsDropDown(parent);
        popupWindow.update();

    }

    private void initPopupWindowUI(View view) {

        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        popupWindowUI = new PopupWindowUI();
        popupWindowUI.llSortByTime = (LinearLayout) view
                .findViewById(R.id.llSortByTime);

        popupWindowUI.llSortByBank = (LinearLayout) view
                .findViewById(R.id.llSortByBank);

        popupWindowUI.ivSortByBank = (ImageView) view.findViewById(R.id.ivSortByBank);
        popupWindowUI.ivSortByTime = (ImageView) view.findViewById(R.id.ivSortByTime);

        popupWindowUI.tvChart= (TextView) view.findViewById(R.id.tvChart);
        popupWindowUI.llSortByTime.setOnClickListener(click);
        popupWindowUI.llSortByBank.setOnClickListener(click);
        popupWindowUI.tvChart.setOnClickListener(click);
    }

    class PopupWindowUI {
        ImageView ivSortByTime;
        ImageView ivSortByBank;
        LinearLayout llSortByTime;
        LinearLayout llSortByBank;
        TextView tvChart;
    }

    private OnClickListener click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llSortByTime:
                    sort(true);
                    EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_MAIN, TYPE_MOUTH));
                    break;
                case R.id.llSortByBank:
                    sort(false);
                    EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_MAIN, TYPE_BANK));
                    break;
                case R.id.tvChart:
                    EventBus.getDefault().post(new BaseEvent(TYPE_CHART,TYPE_CHART));
                    break;
                default:
                    break;
            }
        }
    };

    private void sort(boolean time) {
        popupWindowUI.ivSortByBank.setVisibility(time ? View.INVISIBLE : View.VISIBLE);
        popupWindowUI.ivSortByTime.setVisibility(time ? View.VISIBLE : View.INVISIBLE);
    }
}
