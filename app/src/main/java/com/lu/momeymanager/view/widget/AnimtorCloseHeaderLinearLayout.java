package com.lu.momeymanager.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

import com.lu.momeymanager.util.Debug;

/**
 * Created by lenovo on 2016/4/15.
 */
public class AnimtorCloseHeaderLinearLayout extends LinearLayout{


    private View headerView;
    private ListView listView;
    private int headerH;
    private boolean isCloseHeader;
    private int lastY;
    private Scroller scroller;
    private boolean isUp;
    private int dua=500;
    private float dy;
    public AnimtorCloseHeaderLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        isCloseHeader=false;
        scroller=new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        headerView=getChildAt(0);
        listView= (ListView) getChildAt(1);
//        headerH=headerView.getMeasuredHeight();
//        Debug.d(this,"onFinishInflate  headerH:"+headerH);
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerH=headerView.getMeasuredHeight();
                Debug.d(AnimtorCloseHeaderLinearLayout.this,"onGlobalLayout  headerH:"+headerH);
                dy=headerH/(float)dua;
                Debug.d(AnimtorCloseHeaderLinearLayout.this,"onGlobalLayout  dy:"+dy);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int dy= (int) (lastY-event.getY());
                d("onInterceptTouchEvent,dy:"+dy);
                //上滑
                if(dy>0 && !isCloseHeader){
                    return true;
                }else if(dy<0&&isCloseHeader){
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY= (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                int dy= (int) (lastY-event.getY());
                d("onTouchEvent,dy:"+dy);
                //上滑
                if(dy>0 && !isCloseHeader){
                    isCloseHeader=!isCloseHeader;
                    isUp=true;
                    scroller.startScroll(0,0,0,headerH,dua);
                }else if(dy<0&&isCloseHeader){
                    isUp=false;
                    isCloseHeader=!isCloseHeader;
                    scroller.startScroll(0,0,0,-headerH,dua);
                }
                lastY= (int) event.getY();
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            d("computeScroll:scroller.getCurrY()");
            if(isUp){
//                scrollBy(0, (int) -1);
                scrollTo(0,scroller.getCurrY());
            }else{
                scrollBy(0,-scroller.getCurrY());
            }
//            if(scroller.isFinished()){
//                if(isUp){
//                    isCloseHeader=true;
//                }else{
//                    isCloseHeader=false;
//                }
//            }
            postInvalidate();
        }

    }
    private void d(String msg){
        Debug.d(this,"..............."+msg);
    }
}
