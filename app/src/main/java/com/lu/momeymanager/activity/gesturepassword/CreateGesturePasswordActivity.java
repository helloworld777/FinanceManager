package com.lu.momeymanager.activity.gesturepassword;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.financemanager.R;
import com.lu.momeymanager.app.MomeyManagerApplication;
import com.lu.momeymanager.util.SaveDataUtil;
import com.lu.momeymanager.widget.lockpatternview.LockPatternUtils;
import com.lu.momeymanager.widget.lockpatternview.LockPatternView;
import com.lu.momeymanager.widget.lockpatternview.LockPatternView.Cell;
import com.lu.momeymanager.widget.lockpatternview.LockPatternView.DisplayMode;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������������
 * 
 * @author jgduan �������봴��
 * 
 */
public class CreateGesturePasswordActivity extends Activity implements
		OnClickListener {
	private static final int ID_EMPTY_MESSAGE = -1;
	private static final String KEY_UI_STAGE = "uiStage";
	private static final String KEY_PATTERN_CHOICE = "chosenPattern";
	/** �м�Բ�����ͼ�� **/
	private LockPatternView mLockPatternView;
	/** �ײ��Ҳఴť **/
	private Button mFooterRightButton;
	/** �ײ���ఴť **/
	private Button mFooterLeftButton;
	/** �����ı� **/
	protected TextView mHeaderText;
	/** ������ʾ��λ��ƽ���ͼ�� **/
	private final List<LockPatternView.Cell> mAnimatePattern = new ArrayList<LockPatternView.Cell>();
	/** ������ͼ,��������õ�����ͼ�� **/
	private View mPreviewViews[][] = new View[3][3];
	protected List<LockPatternView.Cell> mChosenPattern = null;
	private Toast mToast;
	private Stage mUiStage = Stage.Introduction;

	/**
	 * �ײ���ఴť
	 */
	enum LeftButtonMode {

		// �ڲ�ͬ״̬��Ϊ��ť���ò�ͬ������
		// <Cancel-ȡ��;CancelDisabled-����ȡ��;Retry-����;RetryDisabled-��������;Gone-��ȥ��>
		Cancel(android.R.string.cancel, true), CancelDisabled(
				android.R.string.cancel, false), Retry(
				R.string.lockpattern_retry_button_text, true), RetryDisabled(
				R.string.lockpattern_retry_button_text, false), Gone(
				ID_EMPTY_MESSAGE, false);

		/**
		 * @param text
		 *            ָ����ʾ�ı�����ʽ
		 * @param enabled
		 *            �Ƿ�����
		 */
		LeftButtonMode(int text, boolean enabled) {
			this.text = text;
			this.enabled = enabled;
		}

		final int text;
		final boolean enabled;
	}

	/**
	 * �ײ��Ҳఴť
	 */
	enum RightButtonMode {

		// �ڲ�ͬ״̬��Ϊ��ť���ò�ͬ������
		// <Continue-����;ContinueDisabled-���ü���;Confirm-ȷ��;ConfirmDisabled-����ȷ��;Ok-�㶨>
		Continue(R.string.lockpattern_continue_button_text, true), 
		ContinueDisabled(
				R.string.lockpattern_continue_button_text, false), 
		Confirm(
				R.string.lockpattern_confirm_button_text, true), 
		ConfirmDisabled(
				R.string.lockpattern_confirm_button_text, false), 
		Ok(
				android.R.string.ok, true);

		/**
		 * @param text
		 *            ָ����ʾ�ı�����ʽ
		 * @param enabled
		 *            �Ƿ�����
		 */
		RightButtonMode(int text, boolean enabled) {
			this.text = text;
			this.enabled = enabled;
		}

		final int text;
		final boolean enabled;
	}

	/**
	 * �û������Ҫѡ���Ӧ״̬ �ܱ�����ö����(���÷�Χ-��ǰ����,��������)
	 */
	protected enum Stage {

		// ���״̬��ͬ���ð�ť
		// <Introduction-��������;HelpScreen-������Ļ;ChoiceTooShort-ѡ��̫��;FirstChoiceValid-�״�ѡ����Ч;
		// NeedToConfirm-�ٴ�ȷ��;ChoiceConfirmed-ѡ��ȷ��>
		Introduction(R.string.lockpattern_recording_intro_header,
				LeftButtonMode.Cancel, RightButtonMode.ContinueDisabled,
				ID_EMPTY_MESSAGE, true),
		HelpScreen(
				R.string.lockpattern_settings_help_how_to_record,
				LeftButtonMode.Gone, RightButtonMode.Ok, ID_EMPTY_MESSAGE,
				false), 
		ChoiceTooShort(
				R.string.lockpattern_recording_incorrect_too_short,
				LeftButtonMode.Retry, RightButtonMode.ContinueDisabled,
				ID_EMPTY_MESSAGE, true), 
		FirstChoiceValid(
				R.string.lockpattern_pattern_entered_header,
				LeftButtonMode.Retry, RightButtonMode.Continue,
				ID_EMPTY_MESSAGE, false), 
		NeedToConfirm(
				R.string.lockpattern_need_to_confirm, LeftButtonMode.Cancel,
				RightButtonMode.ConfirmDisabled, ID_EMPTY_MESSAGE, true), 
		ConfirmWrong(
				R.string.lockpattern_need_to_unlock_wrong,
				LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled,
				ID_EMPTY_MESSAGE, true), 
		ChoiceConfirmed(
				R.string.lockpattern_pattern_confirmed_header,
				LeftButtonMode.Cancel, RightButtonMode.Confirm,
				ID_EMPTY_MESSAGE, false);

		/**
		 * @param headerMessage
		 *            ��ʾ�ڶ���
		 * @param leftMode
		 *            ��ఴť��ʽ
		 * @param rightMode
		 *            �Ҳఴť��ʽ
		 * @param footerMessage
		 *            ��ʾ�ڵײ�
		 * @param patternEnabled
		 *            �Ƿ�����
		 */
		Stage(int headerMessage, LeftButtonMode leftMode,
				RightButtonMode rightMode, int footerMessage,
				boolean patternEnabled) {
			this.headerMessage = headerMessage;
			this.leftMode = leftMode;
			this.rightMode = rightMode;
			this.footerMessage = footerMessage;
			this.patternEnabled = patternEnabled;
		}

		final int headerMessage;
		final LeftButtonMode leftMode;
		final RightButtonMode rightMode;
		final int footerMessage;
		final boolean patternEnabled;
	}

	/**
	 * ������ʾ��Ϣ
	 * 
	 * @param message
	 */
	private void showToast(CharSequence message) {

		if (null == mToast) {
			mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(message);
		}

		mToast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesturepassword_create);
		// ��ʼ����ʾ����--���ƽ���ͼ��
		mAnimatePattern.add(LockPatternView.Cell.of(0, 0));
		mAnimatePattern.add(LockPatternView.Cell.of(0, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(1, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 2));

		// ��ʼ���м��������
		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_create_lockview);
		mHeaderText = (TextView) findViewById(R.id.gesturepwd_create_text);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);

		// ��ʼ���ײ���ť
		mFooterRightButton = (Button) this.findViewById(R.id.right_btn);
		mFooterLeftButton = (Button) this.findViewById(R.id.reset_btn);
		mFooterRightButton.setOnClickListener(this);
		mFooterLeftButton.setOnClickListener(this);

		// ��ʼ������ͼ��
		initPreviewViews();

		// �жϱ���ʵ���״̬�Ƿ�Ϊ��
		if (savedInstanceState == null) {// ���Ϊ��
			
			
			if(getIntent().getIntExtra("flag", 1)==1){
				
				updateStage(Stage.Introduction);
			}else{
				
				// ���ð���ģʽ
				updateStage(Stage.Introduction);
				updateStage(Stage.HelpScreen);
			}
			
		} else {// ������ʵ��
			// ��ȡ�����״̬
			final String patternString = savedInstanceState
					.getString(KEY_PATTERN_CHOICE);
			if (patternString != null) {// ����ȡ���Ϊ��
				// ֱ�ӽ����Ӧ״̬-����
				mChosenPattern = LockPatternUtils
						.stringToPattern(patternString);
			}
			updateStage(Stage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
		}

	}

	/**
	 * ��ʼ��������ͼͼ��
	 */
	private void initPreviewViews() {
		mPreviewViews = new View[3][3];
		mPreviewViews[0][0] = findViewById(R.id.gesturepwd_setting_preview_0);
		mPreviewViews[0][1] = findViewById(R.id.gesturepwd_setting_preview_1);
		mPreviewViews[0][2] = findViewById(R.id.gesturepwd_setting_preview_2);
		mPreviewViews[1][0] = findViewById(R.id.gesturepwd_setting_preview_3);
		mPreviewViews[1][1] = findViewById(R.id.gesturepwd_setting_preview_4);
		mPreviewViews[1][2] = findViewById(R.id.gesturepwd_setting_preview_5);
		mPreviewViews[2][0] = findViewById(R.id.gesturepwd_setting_preview_6);
		mPreviewViews[2][1] = findViewById(R.id.gesturepwd_setting_preview_7);
		mPreviewViews[2][2] = findViewById(R.id.gesturepwd_setting_preview_8);
	}

	/**
	 * �޸Ķ�����ͼͼ��
	 */
	private void updatePreviewViews() {
		if (mChosenPattern == null)
			return;
		Log.i("way", "result = " + mChosenPattern.toString());
		for (LockPatternView.Cell cell : mChosenPattern) {
			Log.i("way", "cell.getRow() = " + cell.getRow()
					+ ", cell.getColumn() = " + cell.getColumn());
			mPreviewViews[cell.getRow()][cell.getColumn()]
					.setBackgroundResource(R.drawable.gesture_create_grid_selected);

		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
		if (mChosenPattern != null) {// ���mChosenPattern��Ϊ��
			// ��ǵ�ǰ״̬
			outState.putString(KEY_PATTERN_CHOICE,
					LockPatternUtils.patternToString(mChosenPattern));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mUiStage == Stage.HelpScreen) {// ����ڰ���׶�
				updateStage(Stage.Introduction);// ���뵽��׶�
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU && mUiStage == Stage.Introduction) {
			updateStage(Stage.HelpScreen);// ���뵽����׶�
			return true;
		}
		return false;
	}

	/**
	 * ֪ͨ����м�����ͼ��
	 */
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	/**
	 * �����в�������ģʽ,������ض���
	 */
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		/**
		 * ��ʼģʽ
		 */
		public void onPatternStart() {
			// ɾ��ص�
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();// ��ʼ��������׶���ʾ�û�
		}

		/**
		 * ���ģʽ
		 */
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		/**
		 * ���ģʽ
		 */
		public void onPatternDetected(List<LockPatternView.Cell> pattern) {
			if (pattern == null)
				return;
			// ��ݲ�ͬ����������Ӧ
			if (mUiStage == Stage.NeedToConfirm
					|| mUiStage == Stage.ConfirmWrong) {
				if (mChosenPattern == null)
					throw new IllegalStateException(
							"null chosen pattern in stage 'need to confirm");
				if (mChosenPattern.equals(pattern)) {
					updateStage(Stage.ChoiceConfirmed);
				} else {
					updateStage(Stage.ConfirmWrong);
				}
			} else if (mUiStage == Stage.Introduction
					|| mUiStage == Stage.ChoiceTooShort) {
				if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
					updateStage(Stage.ChoiceTooShort);
				} else {
					mChosenPattern = new ArrayList<LockPatternView.Cell>(
							pattern);
					updateStage(Stage.FirstChoiceValid);
				}
			} else {
				throw new IllegalStateException("Unexpected stage " + mUiStage
						+ " when " + "entering the pattern.");
			}
		}

		public void onPatternCellAdded(List<Cell> pattern) {

		}

		/**
		 * ��������׶���ʾ�û�<ͬʱ���õײ���ť>
		 */
		private void patternInProgress() {
			mHeaderText.setText(R.string.lockpattern_recording_inprogress);
			mFooterLeftButton.setEnabled(false);
			mFooterRightButton.setEnabled(false);
		}

	};

	/**
	 * ���½׶�
	 * 
	 * @param stage
	 */
	private void updateStage(Stage stage) {
		mUiStage = stage;
		if (stage == Stage.ChoiceTooShort) {
			mHeaderText.setText(getResources().getString(stage.headerMessage,
					LockPatternUtils.MIN_LOCK_PATTERN_SIZE));
		} else {
			mHeaderText.setText(stage.headerMessage);
		}

		if (stage.leftMode == LeftButtonMode.Gone) {
			mFooterLeftButton.setVisibility(View.GONE);
		} else {
			mFooterLeftButton.setVisibility(View.VISIBLE);
			mFooterLeftButton.setText(stage.leftMode.text);
			mFooterLeftButton.setEnabled(stage.leftMode.enabled);
		}

		mFooterRightButton.setText(stage.rightMode.text);
		mFooterRightButton.setEnabled(stage.rightMode.enabled);

		// same for whether the patten is enabled
		if (stage.patternEnabled) {
			mLockPatternView.enableInput();
		} else {
			mLockPatternView.disableInput();
		}

		mLockPatternView.setDisplayMode(DisplayMode.Correct);

		switch (mUiStage) {
		case Introduction:// ���ý׶�
			mLockPatternView.clearPattern();
			break;
		case HelpScreen:// ����׶�
			mLockPatternView.setPattern(DisplayMode.Animate, mAnimatePattern);
			break;
		case ChoiceTooShort:// ѡ�񳤶ȹ�̽׶�
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case FirstChoiceValid:// ��һ�����ý׶�
			break;
		case NeedToConfirm:// �ڶ������ý׶�
			mLockPatternView.clearPattern();
			updatePreviewViews();
			break;
		case ConfirmWrong:// �ڶ��γ���׶�
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case ChoiceConfirmed:// ȷ�Ͻ׶�
			break;
		}

	}

	/**
	 * ������Ļ���,����Ѿ���ʼ�����µ�ͼ��
	 */
	private void postClearPatternRunnable() {
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reset_btn:
			if (mUiStage.leftMode == LeftButtonMode.Retry) {
				mChosenPattern = null;
				mLockPatternView.clearPattern();
				updateStage(Stage.Introduction);
			} else if (mUiStage.leftMode == LeftButtonMode.Cancel) {
				// They are canceling the entire wizard
				finish();
			} else {
				throw new IllegalStateException(
						"left footer button pressed, but stage of " + mUiStage
								+ " doesn't make sense");
			}

			break;
		case R.id.right_btn:
			if (mUiStage.rightMode == RightButtonMode.Continue) {
				if (mUiStage != Stage.FirstChoiceValid) {
					throw new IllegalStateException("expected ui stage "
							+ Stage.FirstChoiceValid + " when button is "
							+ RightButtonMode.Continue);
				}
				updateStage(Stage.NeedToConfirm);
			} else if (mUiStage.rightMode == RightButtonMode.Confirm) {
				if (mUiStage != Stage.ChoiceConfirmed) {
					throw new IllegalStateException("expected ui stage "
							+ Stage.ChoiceConfirmed + " when button is "
							+ RightButtonMode.Confirm);
				}
				saveChosenPatternAndFinish();
			} else if (mUiStage.rightMode == RightButtonMode.Ok) {
				if (mUiStage != Stage.HelpScreen) {
					throw new IllegalStateException(
							"Help screen is only mode with ok button, but "
									+ "stage is " + mUiStage);
				}
				mLockPatternView.clearPattern();
				mLockPatternView.setDisplayMode(DisplayMode.Correct);
				updateStage(Stage.Introduction);
			}
			break;
		}
	}

	/**
	 * �����������
	 */
	private void saveChosenPatternAndFinish() {
		MomeyManagerApplication.getInstance().getLockPatternUtils().saveLockPattern(mChosenPattern);
		showToast("�������óɹ�");
		SaveDataUtil.setAppToBack(this, 0);
		SaveDataUtil.setAppLock(this, 1);
		// startActivity(new Intent(this, UnlockGesturePasswordActivity.class));
		finish();
	}
}
