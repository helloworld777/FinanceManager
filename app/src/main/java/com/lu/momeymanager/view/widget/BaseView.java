package com.lu.momeymanager.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lu.momeymanager.util.Debug;

/**
 * Created by lenovo on 2016/4/7.
 */
public class BaseView extends TextView{

    private int mTouchSlop;
    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Debug.d(BaseView.this,"onClickonClickonClickonClickonClickonClick");
            }
        });
    }

    int lastX,lastY;
    boolean isMove;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX= (int) event.getRawX();
                lastY= (int) event.getRawY();
                Debug.d(BaseView.this,"ACTION_DOWN");
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                int dx= (int) (lastX- event.getRawX());
                int dy= (int) (lastY- event.getRawY());
                Debug.d(BaseView.this,"dx:"+dx+",dy:"+dy);
                if(Math.abs(dx)>mTouchSlop||Math.abs(dy)>mTouchSlop){
                    isMove=true;
                }
                if(isMove){
                    FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) getLayoutParams();
                    params.leftMargin=params.leftMargin-dx;
                    params.topMargin=params.topMargin-dy;
                    requestLayout();
                }

                lastX= (int) event.getRawX();
                lastY= (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                Debug.d(BaseView.this,"ACTION_UP");
                if(!isMove){
                    isMove=false;
                    return super.onTouchEvent(event);
                }
                isMove=false;
                break;
        }
        return true;
    }
}
