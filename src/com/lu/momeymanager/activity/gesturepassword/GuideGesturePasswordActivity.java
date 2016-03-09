package com.lu.momeymanager.view.widget.activity.gesturepassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lu.momeymanager.R;
import com.lu.momeymanager.app.MomeyManagerApplication;

/**
 * �û�����������������
 * 
 * @author jgduan
 * 
 *         ���û����ν���Ӧ��ʱ��ʾ
 * 
 */
public class GuideGesturePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���ò���
		setContentView(R.layout.activity_gesturepassword_guide);

		// �ڲ������ҵ������������밴ť��Ϊ��󶨵���¼�������
		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						// ��������������
						MomeyManagerApplication.getInstance().getLockPatternUtils().clearLock();
						// ָ��Intent��תĿ��
						Intent intent = new Intent(
								GuideGesturePasswordActivity.this,
								CreateGesturePasswordActivity.class);
						// ���µ�Activity
						startActivity(intent);
						// ������ǰActivity
						finish();

					}
				});
	}

}
