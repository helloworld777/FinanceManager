package com.lu.momeymanager.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.lu.financemanager.R;
import com.lu.momeymanager.util.Debug;

/**
 */
public class SildingFinishLayout extends FrameLayout {

    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    private Scroller mScroller;
    //    private ViewGroup mParentView;
    private int viewWidth;

    public SildingFinishLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        d("mTouchSlop:" + mTouchSlop);
        Interpolator polator = new BounceInterpolator();
        mScroller = new Scroller(context,polator);
        mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);


        mActivity = (Activity) context;
        d("mActivity:" + mActivity);
    }

    private View mContentView;
    private int lastX;
    private boolean isSlop = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(ev.getRawX()<50){

            d("ev.getRawX():"+ev.getRawX()+"onInterceptTouchEvent");
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
//                lastX= (int) event.getX();
                d("event.getX():" + event.getX() + ",event.getRawX():" + event.getRawX());
                return true;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getRawX();
                int dx = lastX - x;

//                d("dx:" + dx);
                if (dx < 0 && Math.abs(dx) > mTouchSlop) {
                    isSlop = true;
                }
                lastX = x;
                if (isSlop) {
                    mContentView.scrollBy(dx, 0);
                }
                break;
            case MotionEvent.ACTION_UP:

                d("ACTION_UP event.getX():" + event.getX() + ",event.getRawX():" + event.getRawX());
                if (isSlop) {
                    x = (int) event.getRawX();
                    int scrollx = mContentView.getScrollX();
                    d("scrollx:" + scrollx);
                    if (Math.abs(scrollx) > viewWidth / 3) {
                        final int delta = (viewWidth + mContentView.getScrollX());

                        d("delta:" + delta);
                        mScroller.startScroll(mContentView.getScrollX(), 0, -delta+1, 0);
                        postInvalidate();
                        isFinished = true;
                    } else {
//                        d("scrollx:" + scrollx);
                        mScroller.startScroll(mContentView.getScrollX(), 0, -mContentView.getScrollX(), 0);
                        postInvalidate();
                        isFinished = false;
                    }
                }
                isSlop = false;
                break;
        }

        return super.onTouchEvent(event);
    }

    private Activity mActivity;

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            d("computeScroll............mScroller.getCurrX():" + mScroller.getCurrX() + ",mScroller.getCurrY():" + mScroller.getCurrY());
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            d("mScroller.isFinished():" + mScroller.isFinished());
            if (mScroller.isFinished() && isFinished) {
                d("mActivity.finished");
                mActivity.finish();
            }
        }
    }

    private boolean isFinished;

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        mContentView = (View) decorChild.getParent();
        decor.addView(this);


        Debug.d(this, "decor:" + decor);
        Debug.d(this, "decorChild:" + decorChild);
    }

    private Drawable mShadowDrawable;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowDrawable != null && mContentView != null) {

            int left = mContentView.getLeft()
                    - mShadowDrawable.getIntrinsicWidth();
            int right = left + mShadowDrawable.getIntrinsicWidth();
            int top = mContentView.getTop();
            int bottom = mContentView.getBottom();

            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(canvas);
        }

    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mContentView = (ViewGroup) getParent();
        viewWidth = getWidth();
        Debug.d(this, "viewWidth:" + viewWidth);
        Debug.d(this, "mParentView:" + mContentView);
    }

    public void d(String msg) {
        Debug.d(this, "------>" + msg);
    }
}
