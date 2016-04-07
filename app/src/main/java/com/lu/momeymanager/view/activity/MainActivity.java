package com.lu.momeymanager.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.financemanager.R;
import com.lu.momeymanager.app.MomeyManagerApplication;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.model.ContactModel;
import com.lu.momeymanager.model.InOutModel;
import com.lu.momeymanager.model.UserModel;
import com.lu.momeymanager.util.Constant;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.util.SPUtils;
import com.lu.momeymanager.util.SaveDataUtil;
import com.lu.momeymanager.view.activity.gesturepassword.UnlockGesturePasswordActivity;
import com.lu.momeymanager.view.fragment.DetailMouthMoneyFragment;
import com.lu.momeymanager.view.fragment.MouthMoneyFragment;
import com.lu.momeymanager.view.popupwindow.PopupWindowUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


@ContentView(value = R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity {
    private static final String TAG = "MainActivity";
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    @ViewInject(value = R.id.tvTitle)
    private TextView tvTitle;

    @ViewInject(value = R.id.ivBack)
    private ImageView ivBack;
    @ViewInject(value = R.id.ivMore)
    private ImageView ivMore;

    @ViewInject(value = R.id.btnRefresh)
    private Button btnRefresh;
    private PopupWindowUtil pWindowUtil;

    @ViewInject(R.id.slidingpanellayout)
    private SlidingPaneLayout sliding;

    private int selectedPosition = -1;
    InOutBeanManager inoutManager;

    private Activity mContext;

    @Override
    protected void initData() {
//		InOutBeanManager.getDefault().getSmsFromPhone();
        inoutManager = InOutBeanManager.getDefault();
        EventBus.getDefault().register(this);
        mContext = this;
    }

    @TargetApi(19)
    @SuppressLint("Recycle")
    @Override
    protected void initWidget() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        fragment = new MouthMoneyFragment();
        fm = getSupportFragmentManager();
        replaceFragment(false);
        homeTitle();
        pWindowUtil = new PopupWindowUtil(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SaveDataUtil.getAppLockState(this) == 1 && SaveDataUtil.getAppToBack(getApplicationContext()) == 1) {
            startActivityForResult(new Intent(this, UnlockGesturePasswordActivity.class), 0);
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);


        if (arg0 == 0 && arg1 == 0) {

            finish();

        }
    }

    private void homeTitle() {
        tvTitle.setText(getString(R.string.all_consume));
        ivBack.setVisibility(View.GONE);
        ivMore.setVisibility(View.VISIBLE);
//        btnRefresh.setVisibility(View.VISIBLE);
        fragment = fm.findFragmentById(R.id.frame_main);
        selectedPosition = -1;
    }

    private void replaceFragment(boolean isHome) {
        transaction = fm.beginTransaction();
        if (isHome) {
            transaction.addToBackStack(null);
        }
        transaction.setCustomAnimations(R.anim.anim_enter_right, R.anim.anim_leave_left, R.anim.anim_enter_left, R.anim.anim_leave_right);
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    @OnClick({R.id.ivBack, R.id.ivMore, R.id.btnRefresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                fm.popBackStack();
                homeTitle();
                break;
            case R.id.ivMore:
                pWindowUtil.showWindow(ivMore);
                break;
            case R.id.btnRefresh:
//			EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_MAIN, SimilarDateMoneyBean.TYPE_MOUTH));
                backup();
                break;
            default:
                break;
        }
    }

    private int page = 1;


    public void backup() {
        DialogUtil.showBackupDialog(this, new DialogUtil.OkListener() {
            @Override
            public void onOkClick(String username) {
                MomeyManagerApplication.getDefault().setUsername(username);
                SPUtils.put(mContext, Constant.USERNAME, username);
                new UserModel().backup(mContext, username, new UserModel.IBackup() {
                    @Override
                    public void backup() {
                        realBackup();
                    }
                });
            }
        });
    }

    private void realBackup() {
        List<InOutBean> inouts = inoutManager.getAllMoney();
        int size = inouts.size();
        d("size:" + size);
//        int page = 1;
//        int page = size / 50 + 1;
        page = 1;
        int index = 0;
        dialogLoading.show();
        for (int i = 0; i < page; i++) {
            List<InOutBean> temp = new ArrayList<>();
            for (int j = 0; index < size && j < 5; j++, index++) {
                temp.add(inouts.get(index));
            }
            new InOutModel().setAddCallBack(new MyAddCallBack()).addInOut(MainActivity.this, temp);
        }
    }

    class MyAddCallBack implements InOutModel.AddCallBack {
        @Override
        public void addSuccess() {
            d("addSuccess");
            page--;
            if (page <= 0) {
                dialogLoading.dismiss();
                toast("backup success");
            }
        }

        @Override
        public void addFailure() {
            d("addFailure");
            page--;
            if (page <= 0) {
                dialogLoading.dismiss();
                toast("backup failure");
            }
        }
    }

    public void onEventMainThread(BaseEvent baseEvent) {
        d("onEventMainThread :" + baseEvent.getEventType());
        switch (baseEvent.getEventType()) {
            case BaseEvent.CHANGE_FRAGMENT:
                changeFragment(baseEvent);
                break;
            case BaseEvent.TYPE_CHART:
                if (fragment instanceof MouthMoneyFragment) {
                    ((MouthMoneyFragment) fragment).changeShowType(1);
                } else if (fragment instanceof DetailMouthMoneyFragment) {
                    ((DetailMouthMoneyFragment) fragment).changeShowType(1);
                }
                break;
            default:
                break;
        }

    }

    private void changeFragment(BaseEvent baseEvent) {
        selectedPosition = (Integer) baseEvent.getData();
        fragment = DetailMouthMoneyFragment.newInstance(selectedPosition);
        replaceFragment(true);
        tvTitle.setText(InOutBeanManager.getDefault().getSimilarDateMoneyBeans().get(selectedPosition).getDate());
        ivBack.setVisibility(View.VISIBLE);
        ivMore.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        homeTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtil.d(TAG, "********************onDestroy 111111111111111111111111");
        SaveDataUtil.setAppToBack(getApplicationContext(), 1);

    }

    public void backupContact() {
        DialogUtil.showBackupDialog(this, new DialogUtil.OkListener() {
            @Override
            public void onOkClick(String username) {
                MomeyManagerApplication.getDefault().setUsername(username);
                SPUtils.put(mContext, Constant.USERNAME, username);
                if (TextUtils.isEmpty(username)) {
                    toast("username is not null");
                    return;
                }
                new UserModel().backup(mContext, username, new UserModel.IBackup() {
                    @Override
                    public void backup() {
                        realBackupContact();
                    }
                });
            }
        });
    }

    private void realBackupContact() {

//        List<ContactBean> inouts = new ContactModel().backupAllContact();
//        int size = inouts.size();
//        d("size:" + size);
//        int page = 1;
//        int page = size / 50 + 1;
        page = 1;
        int index = 0;
        dialogLoading.show();
        new ContactModel().backupAllContact(this, new ContactModel.IBackupContact() {
            @Override
            public void backupSuccess() {
                dialogLoading.dismiss();
                toast("backupSuccess");
            }

            @Override
            public void backupFaild() {
                dialogLoading.dismiss();
                toast("backupFaild");
            }
        });
//        dialogLoading.show();
//        for (int i = 0; i < page; i++) {
//            List<InOutBean> temp = new ArrayList<>();
//            for (int j = 0; index < size && j < 5; j++, index++) {
//                temp.add(inouts.get(index));
//            }
//            new InOutModel().setAddCallBack(new MyAddCallBack ()).addInOut(MainActivity.this, temp);
//        }
    }
}
