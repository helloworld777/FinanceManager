package com.lu.momeymanager.view.widget.activity.gesturepassword;

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

import com.lu.momeymanager.R;
import com.lu.momeymanager.app.MomeyManagerApplication;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.util.SaveDataUtil;
import com.lu.momeymanager.view.widget.lockpatternview.LockPatternUtils;
import com.lu.momeymanager.view.widget.lockpatternview.LockPatternView;
import com.lu.momeymanager.view.widget.lockpatternview.LockPatternView.Cell;

import java.util.List;

public class UnlockGesturePasswordActivity extends Activity {
	/** �м����ͼ�� **/
	private LockPatternView mLockPatternView;
	/** ����������� **/
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	/** ��ʱ�� **/
	private CountDownTimer mCountdownTimer = null;
	/** Handler **/
	private Handler mHandler = new Handler();
	/** �����ı� **/
	private TextView mHeadTextView;
	private Animation mShakeAnim;

	private Toast mToast;

	/**
	 * ������ʾ��Ϣ
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
		// ���ò���
		setContentView(R.layout.activity_gesturepassword_unlock);

		// ����id�ڲ������ҵ��ؼ�����
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

		// �ж��Ƿ���������������,���û����,��ת�����ý���
		// if (!App.getInstance().getLockPatternUtils().savedPatternExists()) {
		// startActivity(new Intent(this, GuideGesturePasswordActivity.class));
		// finish();
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �жϼ�ʱ�������Ƿ�Ϊ��
		if (mCountdownTimer != null)// ��Ϊ��
			mCountdownTimer.cancel();// ȡ����ʱ��
	}

	/**
	 * ������Ƶ�ͼ��,�ָ�����ʼ״̬
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

			if (pattern == null)// �ж�pattern�Ƿ�Ϊ��
				return;

			// �жϽ����Ƿ�ɹ�
			if (MomeyManagerApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {// �ɹ�

				// ���õ�ǰģʽΪ��ȷ��ģʽ
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);

				// ����Intent��תĿ��
				// Intent intent = new
				// Intent(UnlockGesturePasswordActivity.this,
				// GuideGesturePasswordActivity.class);
				// // ���µ�Activity
				// startActivity(intent);
				showToast("�����ɹ�");
				SaveDataUtil.setAppToBack(
						UnlockGesturePasswordActivity.this, 0);
				// ������ǰ��Activity
				setResult(1);
				finish();
//				Process process=new Process();
				

			} else {// δ�ɹ�

				// ���õ�ǰģʽΪ����ģʽ
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);

				// �ж����볤��
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {// ���볤�ȴﵽ���Ҫ��

					// ͳ������������
					mFailedPatternAttemptsSinceLastTimeout++;
					// ͳ��ʣ��Ľ�������
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					// �ж�ʣ��Ľ�������
					if (retry >= 0) {
						if (retry == 0)// ���ʣ���������0,֪ͨ�û�30�������
							showToast("����5��������룬��30�������");
						mHeadTextView.setText("������󣬻�����������" + retry + "��");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else {// ���볤��δ�ﵽҪ��
					showToast("���볤�Ȳ�����������");
				}

				// �ж�����������
				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {// ��������ﵽ�������
					// ֪ͨ���ý�������30��,30������û��5�ν�������
					mHandler.postDelayed(attemptLockout, 2000);

				} else {// �������δ�ﵽ�������
					// ֪ͨ������Ƶ�ͼ��,�ָ�����ͼ��״̬
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
			// ����Ѿ����Ƶ�ͼ��
			mLockPatternView.clearPattern();
			// �����м�ͼ������
			mLockPatternView.setEnabled(false);
			// ʹ�ü�ʱ�����ͼ�ʱ
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// �����ȥ������
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {// �������0
						// ÿ��һ����¶����ı���Ϣ
						mHeadTextView.setText(secondsRemaining + " �������");
					} else {// ����ʱ����
						mHeadTextView.setText("�������������");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					// �����м��������
					mLockPatternView.setEnabled(true);
					// ��������������
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
