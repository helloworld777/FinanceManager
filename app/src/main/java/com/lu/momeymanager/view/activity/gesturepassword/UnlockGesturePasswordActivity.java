package com.lu.momeymanager.view.activity.gesturepassword;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.financemanager.R;
import com.lu.momeymanager.app.MomeyManagerApplication;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.util.SaveDataUtil;
import com.lu.momeymanager.view.lockpatternview.LockPatternUtils;
import com.lu.momeymanager.view.lockpatternview.LockPatternView;
import com.lu.momeymanager.view.lockpatternview.LockPatternView.Cell;

import java.util.List;

public class UnlockGesturePasswordActivity extends Activity {
	private LockPatternView mLockPatternView;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	private Handler mHandler = new Handler();
	private TextView mHeadTextView;
	private Animation mShakeAnim;

	private Toast mToast;

	/**
	 *
	 * @param message
	 */
	private void showToast(CharSequence message) {
		if (null == mToast) {
			mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			mToast.setText(message);
		}

		mToast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gesturepassword_unlock);

		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// if (!App.getInstance().getLockPatternUtils().savedPatternExists()) {
		// startActivity(new Intent(this, GuideGesturePasswordActivity.class));
		// finish();
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCountdownTimer != null)//
			mCountdownTimer.cancel();//
	}

	/**
	 */
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		public void onPatternDetected(List<LockPatternView.Cell> pattern) {

			if (pattern == null)//
				return;

			if (MomeyManagerApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {//

				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);

				showToast("0.0");
				SaveDataUtil.setAppToBack(
						UnlockGesturePasswordActivity.this, 0);
				setResult(1);
				finish();
//				Process process=new Process();
				

			} else {//

				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);

				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {//

					mFailedPatternAttemptsSinceLastTimeout++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					if (retry >= 0) {
						if (retry == 0)//
							showToast("0.0 30s 0.0");
						mHeadTextView.setText("" + retry + "");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else {
					showToast("v");
				}

				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {//
					mHandler.postDelayed(attemptLockout, 2000);

				} else {
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		private void patternInProgress() {
		}

		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub

		}
	};

	Runnable attemptLockout = new Runnable() {

		@Override
		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {
						mHeadTextView.setText(secondsRemaining + " ");
					} else {
						mHeadTextView.setText("v");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					mLockPatternView.setEnabled(true);
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};
	private long start=0;
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			long second=System.currentTimeMillis();
			
			if((second-start)<2000){
//				System.exit(0);
//				Process.killProcess(android.os.Process.myPid());
//				Intent intent=new Intent();
				setResult(0);
				finish();
				
			}else{
				DialogUtil.showToast(getApplicationContext(), getString(R.string.back_tips));
				start=System.currentTimeMillis();
			}
			LogUtil.d("onKeyDown", "**************back");
			return false;
		}
		return super.onKeyDown(keyCode, event);
		
	};
}
