package com.lu.momeymanager.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lu.momeymanager.util.Debug;
import com.lu.momeymanager.util.ScreenUtils;

/**
 * Created by lenovo on 2016/4/28.
 */
public class ProgressView extends View {
    private int h;
    private float precent=0;
    private Paint paint;
    private int w;
    private Context mContext;
    public ProgressView(Context context){
        super(context);
        init(context);
    }
    public void setPrencent(int progress){

        if(getVisibility()!=View.VISIBLE){
            setVisibility(View.VISIBLE);
        }
        float p=progress/100.0f;
        this.precent=p;
        Debug.d(this,"p:%s",p);
        if(p==1){
            setVisibility(View.GONE);
        }
        postInvalidate();
    }
    private void init(Context context) {
        mContext=context;
        h= ScreenUtils.dp2px(context,5);
        paint=new Paint();
        paint.setColor(Color.GREEN);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void apptoTarget(View view){


        ViewGroup parent= (ViewGroup) view.getParent();
        parent.removeView(view);
        LinearLayout l=new LinearLayout(mContext);
        l.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams p=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.addView(this);
        l.addView(view);
        parent.addView(l,p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        w=getMeasuredWidth();
        setMeasuredDimension(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF r=new RectF(0,0,precent*w,h);
        canvas.drawRect(r,paint);
    }
}
