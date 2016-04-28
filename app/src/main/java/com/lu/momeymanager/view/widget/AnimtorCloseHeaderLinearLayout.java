package com.lu.momeymanager.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
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
    private boolean isTopHidden=false;
    private int mTouchSlop;
    public AnimtorCloseHeaderLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        isCloseHeader=false;
        scroller=new Scroller(context);

        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        headerView=getChildAt(0);
        listView= (ListView) getChildAt(1);
//        headerH=headerView.getMeasuredHeight();
//        Debug.d(this,"onFinishInflate  headerH:"+headerH);
//        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                headerH=headerView.getMeasuredHeight();
//                Debug.d(AnimtorCloseHeaderLinearLayout.this,"onGlobalLayout  headerH:"+headerH);
//                dy=headerH/(float)dua;
//                Debug.d(AnimtorCloseHeaderLinearLayout.this,"onGlobalLayout  dy:"+dy);
//            }
//        });
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastY= (int) event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                int dy= (int) (lastY-event.getY());
//                d("onInterceptTouchEvent,dy:"+dy);
//                if(Math.abs(dy)>mTouchSlop){
//
//                    //上滑
//                    if(dy>0 && !isTopHidden){
//                        lastY= (int) event.getY();
//                        return true;
//                    }
//
//                }
//
////                else if(dy<0&&isCloseHeader){
////                    return true;
////                }
////                break;
//        }
//
//
//        return super.onInterceptTouchEvent(event);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastY= (int) event.getY();
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                int dy= (int) (event.getY()-lastY);
//                d("onTouchEvent,dy:"+dy);
//                //上滑
////                if(dy<0 && !isCloseHeader){
//                    if(dy<0 && !isTopHidden){
//                        isTopHidden=true;
//                        Animator animator= ObjectAnimator.ofFloat(this, "translationY", 0, headerH);
//                        animator.start();
//                    }
////                    isUp=true;
////                if(Math.abs(dy)>headerH){
////                    dy=-headerH;
////                    isTopHidden=true;
////                }
////
////                if(dy<0 && Math.abs(dy)<headerH){
////                    scrollTo(0,-dy);
////                }
//
////                lastY= (int) event.getY();
////                }else if(dy>0&&isCloseHeader){
////                    isUp=false;
//////                    scroller.startScroll(0,0,0,headerH,dua);
////                    scrollTo(0,dy);
////                }
////                lastY= (int) event.getY();
////                postInvalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

//    @Override
//    public void computeScroll() {
//        if(scroller.computeScrollOffset()){
//            d("computeScroll:scroller.getCurrY()");
//            if(isUp){
////                scrollBy(0, (int) -1);
//                scrollTo(0,scroller.getFinalY());
//            }else{
//                scrollBy(0,-scroller.getFinalY());
//            }
////            if(scroller.isFinished()){
////                if(isUp){
////                    isCloseHeader=true;
////                }else{
////                    isCloseHeader=false;
////                }
////            }
//            postInvalidate();
//        }
//
//    }
    private void d(String msg){
        Debug.d(this,"..............."+msg);
    }
}
