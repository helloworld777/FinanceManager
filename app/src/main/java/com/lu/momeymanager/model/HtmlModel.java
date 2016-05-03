package com.lu.momeymanager.model;

import com.lidroid.xutils.http.RequestParams;
import com.lu.momeymanager.util.AsyncTaskUtil;
import com.lu.momeymanager.util.ChangeCharset;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    /**
     * 将一个输入流转化为字符串
     */
    public  String getStreamString(String url){

        if (url != null){
            try{
                String encodeStr="";
                URL url1=new URL(url);
                boolean isEncode=false;
                boolean isSysEncode=false;
                HttpURLConnection connection= (HttpURLConnection) url1.openConnection();
                connection.connect();


                InputStream tempIn=copyInputStream(connection.getInputStream());
                tempIn.reset();
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tempIn));
                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine = new String("");

                while ((sTempOneLine = tBufferedReader.readLine()) != null){
                    tStringBuffer.append(sTempOneLine);

                    if(!isEncode){
                        if(sTempOneLine.contains("charset=")){
                            isEncode=true;
                            int encode=sTempOneLine.indexOf("charset=")+"charset=".length();
                            String encodeS=sTempOneLine.substring(encode,encode+6);
                            d("encodeS:"+encodeS);
                            if(encodeS.toLowerCase().contains(ChangeCharset.gbk)){
                                encodeStr=ChangeCharset.gbk;
                            }else if(encodeS.toLowerCase().contains(ChangeCharset.UTF)){
                                encodeStr=ChangeCharset.UTF_8;
                                isSysEncode=true;
                                d(".....isSysEncode");
                            }else if(encodeS.toLowerCase().contains(ChangeCharset.GB2312)){
                                encodeStr=ChangeCharset.GB2312;
                            }
                            d("encodeStr:"+encodeStr);

                            if(!isSysEncode){
                                break;
                            }


                        }

                    }


                }
                if(!isSysEncode){
                    tempIn.reset();
                    return encodeStream(tempIn,encodeStr);
                }
                return tStringBuffer.toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }
    private InputStream copyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream br = new BufferedInputStream(inputStream);
        byte[] b = new byte[1024];
        for (int c = 0; (c = br.read(b)) != -1;)
        {
            bos.write(b, 0, c);
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }
    private String encodeStream(InputStream in,String iencode) throws IOException {
        InputStreamReader reader;
        d(".....iencode:"+iencode);
        reader=new InputStreamReader(in,iencode);
        BufferedReader tBufferedReader = new BufferedReader(reader);
        StringBuffer tStringBuffer = new StringBuffer();
        String sTempOneLine = new String("");
        while ((sTempOneLine = tBufferedReader.readLine()) != null){
            tStringBuffer.append(sTempOneLine);

        }
        return tStringBuffer.toString();
    }
    public void getTitle(final String url){
        params=new RequestParams();
        new AsyncTaskUtil().setIAsyncTaskCallBack(new AsyncTaskUtil.IAsyncTaskCallBack(){

            @Override
            public Object doInBackground(String... arg0) {

                String content=getStreamString(arg0[0]);
                d("content:"+content);
                int start=content.indexOf("<title>")+"<title>".length();
                int end=content.indexOf("</title>");
                String title=content.substring(start,end);
                d("title:"+title);
                return title;
            }

            @Override
            public void onPostExecute(Object result) {
                if(iGetHtmlTitle!=null){
                    iGetHtmlTitle.getHtmlTitle(result.toString());
                }
            }
        }).execute(url);




//        postRequest(url,params,new RequestCallBack<String>(){
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                String content=responseInfo.result;
////                d("content:"+content);
//                int start=content.indexOf("<title>")+"<title>".length();
//                int end=content.indexOf("</title>");
//
//                int encode=content.indexOf("charset=")+"charset=".length();
//
//                String encodeS=content.substring(encode,encode+4);
//                d("encodeS1:"+encodeS);
//                if(encodeS.toLowerCase().contains("gbk")){
//                    encodeS="gbk";
//                }
//                d("encodeS2:"+encodeS);
//
//                String title=content.substring(start,end);
//                d("title:"+title);
//
//                try {
////                    String title1=ChangeCharset.changeCharset(title,ChangeCharset.ISO_8859_1,ChangeCharset.GBK);
////                    d("title1:"+title1);
////                    String title2=ChangeCharset.changeCharset(title,ChangeCharset.GBK,ChangeCharset.GBK);
////                    d("title2:"+title2);
////                    String title3=ChangeCharset.changeCharset(title,ChangeCharset.GBK,ChangeCharset.UTF_8);
////                    d("title3:"+title3);
////                    String title4=ChangeCharset.changeCharset(title,ChangeCharset.US_ASCII,ChangeCharset.GBK);
////                    d("title4:"+title4);
////                    String title5=ChangeCharset.changeCharset(title,ChangeCharset.US_ASCII,ChangeCharset.UTF_8);
////                    d("title5:"+title5);
//                    String title1=ChangeCharset.changeCharset(title,ChangeCharset.ISO_8859_1);
//                    d("title1:"+title1);
//                    String title2=ChangeCharset.changeCharset(title,ChangeCharset.GBK);
//                    d("title2:"+title2);
//                    String title3=ChangeCharset.changeCharset(title,ChangeCharset.UTF_8);
//                    d("title3:"+title3);
//                    String title4=ChangeCharset.changeCharset(title,ChangeCharset.US_ASCII);
//                    d("title4:"+title4);
//                    String title5=ChangeCharset.changeCharset(title,ChangeCharset.UTF_16);
//                    d("title5:"+title5);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                if(iGetHtmlTitle!=null){
//                    iGetHtmlTitle.getHtmlTitle(title);
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });
    }
}
