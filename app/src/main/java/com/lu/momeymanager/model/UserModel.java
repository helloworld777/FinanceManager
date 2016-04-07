package com.lu.momeymanager.model;

import android.content.Context;

import com.lu.momeymanager.bean.User;

import org.json.JSONArray;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lenovo on 2016/4/6.
 */
public class UserModel extends BaseModel{


    IUserExitView iUserExitView;
    public UserModel setIUserExitView(IUserExitView i){
        iUserExitView=i;
        return this;
    }
    public void backup(final Context context, final String username,final IBackup iBackup){

        IUserExitView iUserExitView=new IUserExitView() {
            @Override
            public void isExist(boolean isExist) {
                d("isExist-->isExist:"+isExist);
                if(isExist){
                    if(iBackup!=null){
                        iBackup.backup();
                    }
                }else{
                    save(context, username,new SaveListener() {
                        @Override
                        public void onSuccess() {
                            d("onSuccess-->:");
                            iBackup.backup();
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            d("onFailure-->i:"+i+",s:"+s);
                            toast("onFailure");
                        }
                    });
                }
            }
        };
        setIUserExitView(iUserExitView);
        exit(context,username);
    }
    public void exit(final Context context, final String username){
//        d("exit-->context:"+context);
//        new BmobQuery<User>().findObjects(context,new FindListener<User>(){
//            @Override
//            public void onSuccess(List<User> list) {
//                if(iUserExitView!=null){
//                    iUserExitView.isExist(!list.isEmpty());
//                }
//            }
//            @Override
//            public void onError(int i, String s) {
//                d("i:"+i+",s:"+s);
//                toast("onError s:"+s);
//            }
//        });
//
//        new BmobQuery<User>().findObjects(context, new FindCallback() {
//            @Override
//            public void onSuccess(JSONArray jsonArray) {
//                d("jsonArray:"+jsonArray);
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                d("i:"+i+",s:"+s);
//            }
//        });


        BmobQuery query = new BmobQuery("luser");
        query.addWhereEqualTo("username",username);
        query.findObjects(context, new FindCallback() {
            @Override
            public void onSuccess(JSONArray arg0) {
                //注意：查询的结果是JSONArray,需要自行解析
                d("查询成功:"+arg0);
                if(iUserExitView!=null){
                    iUserExitView.isExist(arg0.length()!=0);
                }
            }
            @Override
            public void onFailure(int arg0, String arg1) {
                d("查询失败:"+arg1+",arg1:"+arg1);
                toast("onFailure");
            }
        });
    }
    public void save(Context context,String username, SaveListener s){
        User bmobUser=new User(username);
//        BmobUser bmobUser=new BmobUser();
//        bmobUser.setUsername(username);
//        bmobUser.
        bmobUser.save(context,s);
    }
    public interface IUserExitView{
        void isExist(boolean isExist);
    }
    public interface  IBackup{
        void backup();
    }
}
