package com.example.android_robot_01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
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
    @Override
    protected void initWidget() {
        ivMore.setVisibility(View.GONE);
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

                String msg,url;
                if(type==0){
                    msg=newList.get(i).article;
                    url=newList.get(i).detailurl;
                }else{
                    msg=recipeList.get(i).name;
                    url=recipeList.get(i).detailurl;
                }


                Intent intent=new Intent(view.getContext(),ShowHtmlActivity.class);
                intent.putExtra(ShowHtmlActivity.TEXT,msg);
                intent.putExtra(ShowHtmlActivity.URL,url);
                startActivity(intent);
            }
        });

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
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        NetworkImageView image;
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.header, R.drawable.header);
        imageLoader.get(url, listener);

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
}
