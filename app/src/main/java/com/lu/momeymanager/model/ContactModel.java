package com.lu.momeymanager.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lu.momeymanager.app.MomeyManagerApplication;
import com.lu.momeymanager.bean.ContactBean;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by lenovo on 2016/4/7.
 */
public class ContactModel extends BaseModel{

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    private List<ContactBean> serverContacts=new ArrayList<>();


    public interface  IBackupContact{
        void backupSuccess();
        void backupFaild();
    }

    public void getServerContacts(Context context,String username){




    }

    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};


    public void backupAllContact(final Context mContext,final IBackupContact backupContact){
       final String username=MomeyManagerApplication.getDefault().getUsername();
        BmobQuery query = new BmobQuery("contact");
        query.addWhereEqualTo("username",username);
        query.findObjects(mContext, new FindCallback() {
            @Override
            public void onSuccess(JSONArray arg0) {
                //注意：查询的结果是JSONArray,需要自行解析
                d("查询成功:"+arg0);
                List<ContactBean> contactBeanList1= JSON.parseObject(arg0.toString(),new TypeReference<List<ContactBean>>(){});

                d("contactBeanList1"+contactBeanList1);
                deleteBatch(mContext, contactBeanList1, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        d("deleteBatch :onSuccess");
                        List<ContactBean> contactBeanList=new ArrayList<>();
                        contactBeanList.addAll(getPhoneContacts(mContext));
                        contactBeanList.addAll(getSIMContacts(mContext));
                        for(ContactBean contactBean:contactBeanList){
                            contactBean.username=username;
                        }
                        d("local contactBeanList"+contactBeanList);
                        insertBatch(mContext,contactBeanList,new SaveListener(){
                            @Override
                            public void onSuccess() {
                                backupContact.backupSuccess();
                                d("insertBatch :onSuccess");
                            }
                            @Override
                            public void onFailure(int i, String s) {
                                d("insertBatch ..onFailure:"+i+",arg1:"+s);
                                backupContact.backupFaild();
                            }
                        });
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        d("deleteBatch ..onFailure:"+i+",arg1:"+s);
                        backupContact.backupFaild();
                    }
                });
            }
            @Override
            public void onFailure(int arg0, String arg1) {
                d("查询失败:"+arg1+",arg1:"+arg1);
                toast("onFailure");
                backupContact.backupFaild();
            }
        });
    }
    class MySaveListener extends SaveListener {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(int i, String s) {

        }
    }
    public void deleteBatch(Context context,List<ContactBean> contactBeen, DeleteListener s){
        List<BmobObject> inouts = new ArrayList<>();
        inouts.addAll(contactBeen);
        new BmobObject().deleteBatch(context,inouts,s);
    }
    public void insertBatch(Context context,List<ContactBean> contactBeen, SaveListener s){
        List<BmobObject> inouts = new ArrayList<>();
        inouts.addAll(contactBeen);
        new BmobObject().insertBatch(context,inouts,s);
    }
    /**
     * 得到手机通讯录联系人信息
     **/
    private List<ContactBean> getPhoneContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        List<ContactBean> contactBeanList=new ArrayList<>();
// 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                ContactBean contactBean=new ContactBean();
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                d("phoneNumber:"+phoneNumber);
                contactBean.phoneNumber=phoneNumber;
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                contactBean.contactName=contactName;
                d("contactName:"+contactName);
                contactBeanList.add(contactBean);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                    BmobFile bmobFile=new BmobFile();
//                    bmobFile.
                } else {
//                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);
                }
            }
            phoneCursor.close();
        }
        return contactBeanList;
    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private List<ContactBean> getSIMContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        List<ContactBean> contactBeanList=new ArrayList<>();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);
                //Sim卡中没有联系人头像
                ContactBean contactBean=new ContactBean(contactName,phoneNumber);
                contactBeanList.add(contactBean);
            }
            phoneCursor.close();
        }
        return contactBeanList;
    }

}
