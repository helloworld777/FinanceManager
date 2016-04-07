package com.lu.momeymanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lenovo on 2016/4/7.
 */
public class BaseViewGroup extends FrameLayout{

    public BaseViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private View scollView;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        scollView=getChildAt(0);
    }

    int lastX,lastY;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastX= (int) event.getRawX();
//                lastY= (int) event.getRawY();
//                Debug.d(this,"ACTION_DOWN");
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                int dx= (int) (lastX- event.getRawX());
//                int dy= (int) (lastY- event.getRawY());
//                Debug.d(this,"dx:"+dx+",dy:"+dy);
////                if(Math.abs(dx)>mTouchSlop||Math.abs(dy)>mTouchSlop){
//
//
//                ViewGroup.LayoutParams params=scollView.getLayoutParams();
//
////                scollView.setLeft(scollView.getLeft()-dx);
////                scollView.setTop(scollView.getTop()-dy);
//
////                requestLayout();
////                scollView.scrollBy(dx,dy);
////                }
//                lastX= (int) event.getRawX();
//                lastY= (int) event.getRawY();
//            case MotionEvent.ACTION_UP:
//                Debug.d(this,"ACTION_UP");
//
//        }
//        return super.onTouchEvent(event);
//    }


}
