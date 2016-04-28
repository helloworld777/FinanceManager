package com.lu.momeymanager.model;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.UnsupportedEncodingException;

/**
 * Created by lenovo on 2016/4/28.
 */
public class HtmlModel extends BaseModel{

    public interface IGetHtmlTitle{
        void getHtmlTitle(String title);
    }
    private IGetHtmlTitle iGetHtmlTitle;
    public void setiGetHtmlTitle(IGetHtmlTitle i){
        iGetHtmlTitle=i;
    }

    public void getTitle(String url){
        params=new RequestParams();
        postRequest(url,params,new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String content=responseInfo.result;
//                d("content:"+content);
                int start=content.indexOf("<title>")+"<title>".length();
                int end=content.indexOf("</title>");

                int encode=content.indexOf("charset=")+"charset=".length();

                String encodeS=content.substring(encode,encode+4);
                d("encodeS1:"+encodeS);
                if(encodeS.toLowerCase().contains("gbk")){
                    encodeS="gbk";
                }
                d("encodeS2:"+encodeS);

                String title=content.substring(start,end);
                d("title:"+title);
                try {
                    title=new String(title.getBytes(),"gbk");
                    d("title:"+title);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(iGetHtmlTitle!=null){
                    iGetHtmlTitle.getHtmlTitle(title);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
