package com.example.android_robot_01;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.android_robot_01.bean.ResultNew;
import com.example.android_robot_01.bean.ResultRecipe;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.financemanager.R;
import com.lu.momeymanager.view.activity.BaseFragmentActivity;
import com.lu.momeymanager.view.adapter.LuAdapter;
import com.lu.momeymanager.view.adapter.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2016/4/12.
 */
@ContentView(R.layout.activity_detail)
public class DetailActivity extends BaseFragmentActivity{
    @ViewInject(value = R.id.ivMore)
    protected ImageView ivMore;
    @ViewInject(value = R.id.ivBack)
    protected ImageView ivBack;

    @ViewInject(value = R.id.tvTitle)
    protected TextView tvTitle;

    @ViewInject(value = R.id.listview)
    private ListView listView;
    private LuAdapter<String> luAdapter;

    private String[] resultList;
    private List<ResultNew.New> newList=new ArrayList<>();
    private List<ResultRecipe.Recipe> recipeList=new ArrayList<>();
    private int type=-1;
    public static final String RESULT="result";


    @ViewInject(R.id.rlHeader)
    private RelativeLayout rlHeader;
    private boolean isCloseHeader=false;
    private int origenHeaderH;
    private boolean isFirst=true;
    boolean animatorRunning=false;
    private int mTouchMove;

    @Override
    protected void initWidget() {
        ivMore.setVisibility(View.GONE);
        mTouchMove= ViewConfiguration.get(this).getScaledTouchSlop();
        tvTitle.setText(getString(R.string.detail_title));
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            Serializable result= getIntent().getExtras().getSerializable("result");
            if(result instanceof ResultNew){
                ResultNew resultNew= (ResultNew) result;
                resultList=new String[resultNew.list.size()];
                newList=resultNew.list;
                type=0;
            }else if(result instanceof ResultRecipe){
                ResultRecipe resultRecipe= (ResultRecipe) result;
                resultList=new String[resultRecipe.list.size()];
                recipeList=resultRecipe.list;
                type=1;
            }
        }

        luAdapter=new LuAdapter<String>(this, Arrays.asList(resultList),R.layout.item_listview_detail) {
            @Override
            public void convert(ViewHolder helper, int position) {
                setWidgetData(helper,position);
            }
        };
        listView.setAdapter(luAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToShowHtmlActivity(i);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            int lastY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        lastY= (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dy= (int) (motionEvent.getY()-lastY);

                        if(Math.abs(dy)>mTouchMove){
                            //往下滑
                            if(dy>0 && isCloseHeader && !animatorRunning){
                                isCloseHeader=!isCloseHeader;
                                animatorRunning=true;
                                animatorOpenHeader();
                            }else if(dy<0&&!isCloseHeader && !animatorRunning){
                                animatorRunning=true;
                                isCloseHeader=!isCloseHeader;
                                animatorCloseHeader();
                            }
                        }

//                        lastY= (int) motionEvent.getY();
                        break;
                }

                return false;
            }
        });

        rlHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                d("onGlobalLayout...origenHeaderH:"+origenHeaderH);
                if(isFirst){
                    isFirst=false;
                    origenHeaderH=rlHeader.getMeasuredHeight();
                }

            }
        });
    }


    private void animatorCloseHeader() {
        ObjectAnimator animator=ObjectAnimator.ofFloat(rlHeader,"translationY",0,-origenHeaderH);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f= Math.abs((float) valueAnimator.getAnimatedValue());
                int translationY=((int) f);
                float a=f/origenHeaderH;
                ViewGroup.LayoutParams params=rlHeader.getLayoutParams();
                params.height=(int)(origenHeaderH*(1-a));
                d("animatorCloseHeader ...translationY:"+translationY+",rat:"+(1-a)+",params.height:"+params.height);
                rlHeader.requestLayout();
                if(f==origenHeaderH){
                    animatorRunning=false;
                }
            }
        });
        animator.start();
        isCloseHeader=true;
    }
    private void animatorOpenHeader() {
        d("animatorOpenHeader.......origenHeaderH:"+origenHeaderH);
        ObjectAnimator animator=ObjectAnimator.ofFloat(rlHeader,"translationY",-origenHeaderH,0);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f= Math.abs((float) valueAnimator.getAnimatedValue());
                ViewGroup.LayoutParams params=rlHeader.getLayoutParams();
                float a=f/origenHeaderH;
                params.height=(int)(origenHeaderH*(1-a));
                d("animatorOpenHeader ..."+"rat:"+(1-a)+",params.height:"+params.height);
                rlHeader.requestLayout();
                if(f==0){
                    animatorRunning=false;
                }
            }

        });
        animator.start();
        isCloseHeader=false;
    }

    private void goToShowHtmlActivity(int i) {
        String msg,url;
        if(type==0){
            msg=newList.get(i).article;
            url=newList.get(i).detailurl;
        }else{
            msg=recipeList.get(i).name;
            url=recipeList.get(i).detailurl;
        }
        Intent intent=new Intent(this,ShowHtmlActivity.class);
        intent.putExtra(ShowHtmlActivity.TEXT,msg);
        intent.putExtra(ShowHtmlActivity.URL,url);
        startActivity(intent);
    }
    @OnClick({R.id.ivBack})
    protected void viewClick(View view) {
       finish();
    }

    private void setWidgetData(ViewHolder helper, int position) {
        ImageView img=helper.getView(R.id.img);
        switch (type){
            case 0:
                ResultNew.New n=newList.get(position);
                helper.setString(R.id.tvTitle,n.article);
                helper.setString(R.id.tvSubTitle,n.source);
                displayImg(img,n.icon);
                break;
            case 1:
                ResultRecipe.Recipe r=recipeList.get(position);
                helper.setString(R.id.tvTitle,r.name);
                helper.setString(R.id.tvSubTitle,r.info);
                displayImg(img,r.icon);
                break;
        }
    }
    public void displayImg(ImageView imageView,String url){
        if(TextUtils.isEmpty(url)){
            return;
        }
        d("displayImg url:"+url);
//        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
//        NetworkImageView image;
//        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.header, R.drawable.header);
//        imageLoader.get(url, listener);

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url,imageView);

        //指定图片允许的最大宽度和高度
        //imageLoader.get("http://developer.android.com/images/home/aw_dac.png",listener, 200, 200);
    }
    @Override
    protected void initData() {

    }
    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }

            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
