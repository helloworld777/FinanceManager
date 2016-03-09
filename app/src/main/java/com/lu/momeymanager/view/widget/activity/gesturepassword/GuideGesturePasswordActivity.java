package com.lu.momeymanager.view.widget.activity.gesturepassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lu.financemanager.R;
import com.lu.momeymanager.app.MomeyManagerApplication;

public class GuideGesturePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gesturepassword_guide);

		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						MomeyManagerApplication.getInstance().getLockPatternUtils().clearLock();
						Intent intent = new Intent(
								GuideGesturePasswordActivity.this,
								CreateGesturePasswordActivity.class);
						startActivity(intent);
						finish();

					}
				});
	}

}
