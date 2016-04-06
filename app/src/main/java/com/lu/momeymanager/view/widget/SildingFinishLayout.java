package com.lu.momeymanager.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.lu.momeymanager.util.Debug;

/**
 */
public class SildingFinishLayout extends RelativeLayout {

    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    private Scroller mScroller;
    private ViewGroup mParentView;
    private int viewWidth;

    public SildingFinishLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        d("mTouchSlop:" + mTouchSlop);
        mScroller = new Scroller(context);
    }

    private int lastX;
    private boolean isSlop = false;

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

                d("dx:" + dx);
                if (dx < 0 && Math.abs(dx) > mTouchSlop) {
                    isSlop = true;
                }
                lastX = x;
                if (isSlop) {
                    mParentView.scrollBy(dx, 0);
                }
                break;
            case MotionEvent.ACTION_UP:

                d("ACTION_UP event.getX():" + event.getX() + ",event.getRawX():" + event.getRawX());
                if (isSlop) {
                    x = (int) event.getRawX();
                    int scrollx = mParentView.getScrollX();
                    d("scrollx:" + scrollx);
                    if (Math.abs(scrollx) > viewWidth / 3) {
                        final int delta = (viewWidth + mParentView.getScrollX());
                        mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0);
                        postInvalidate();
                    } else {
//                        final int delta = (viewWidth + mParentView.getScrollX());
                        mScroller.startScroll(mParentView.getScrollX(), 0, -mParentView.getScrollX(), 0);
                        postInvalidate();
                    }
                }
                isSlop = false;
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            d("computeScroll............mScroller.getCurrX():" + mScroller.getCurrX() + ",mScroller.getCurrY():" + mScroller.getCurrY());
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();

            if(mScroller.isFinished()){



            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mParentView = (ViewGroup) getParent();
        viewWidth = getWidth();
        Debug.d(this, "viewWidth:" + viewWidth);
        Debug.d(this, "mParentView:" + mParentView);
    }

    public void d(String msg) {
        Debug.d(this, "------>" + msg);
    }
}
