package com.lu.momeymanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lu.financemanager.R;
import com.lu.momeymanager.view.activity.BalanceQueryActivity;
import com.lu.momeymanager.view.activity.GameActivity;
import com.lu.momeymanager.view.activity.MainActivity;
import com.lu.momeymanager.view.activity.SettingActivity;

public class MenuFragment extends BaseFragment {
    private LinearLayout llSetting, llBalance;
    private ViewClick viewClick;
    private TextView tvBackup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewClick = new ViewClick();
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        llSetting = findViewById(view, R.id.llSetting);
        llSetting.setOnClickListener(viewClick);
        llBalance = findViewById(view, R.id.llBalance);
        llBalance.setOnClickListener(viewClick);

        tvBackup = findViewById(view, R.id.tvBackup);
        tvBackup.setOnClickListener(viewClick);
        TextView tvBackupContact = findViewById(view, R.id.tvBackupContact);
        tvBackupContact.setOnClickListener(viewClick);
        TextView tvCustom = findViewById(view, R.id.tvCustom);
        tvCustom.setOnClickListener(viewClick);
        return view;
    }

    class ViewClick implements OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llSetting:
                    startActivity(SettingActivity.class);
                    break;
                case R.id.llBalance:
                    startActivity(BalanceQueryActivity.class);
                    break;
                case R.id.tvBackup:

                    ((MainActivity) getActivity()).backup();
                    break;
                case R.id.tvBackupContact:

                    ((MainActivity) getActivity()).backupContact();
                    break;
                case R.id.tvCustom:
                    startActivity(GameActivity.class);
                    break;
                default:
                    break;
            }
        }

    }
}
