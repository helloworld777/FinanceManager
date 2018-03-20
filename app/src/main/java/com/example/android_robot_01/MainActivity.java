package com.example.android_robot_01;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_robot_01.bean.ChatMessage;
import com.example.android_robot_01.bean.ChatMessage.Type;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.lu.financemanager.R;
import com.lu.momeymanager.util.DateUtil;
import com.lu.momeymanager.util.Debug;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.util.FileUtil;
import com.lu.momeymanager.util.TopNoticeDialog;
import com.lu.momeymanager.view.activity.BaseFragmentActivity;
import com.zhy.utils.HttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseFragmentActivity {
    /**
     * 展示消息的listview
     */
    private ListView mChatView;
    /**
     * 文本域
     */
    private EditText mMsg;
    /**
     * 存储聊天消息
     */
    private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
    /**
     * 适配器
     */
    private ChatMessageAdapter mAdapter;

    //	private TTSManager ttsManager;
    // 语音合成客户端
//    private SpeechSynthesizer mSpeechSynthesizer;

    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ChatMessage from = (ChatMessage) msg.obj;

//			ttsManager.startTTS(from.getDateStr());
//			mSpeechSynthesizer.speak(from.getDateStr());
            setParam();
            int code = mTts.startSpeaking(from.getMsg(), mTtsListener);
            if (code != ErrorCode.SUCCESS) {
                if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                    //未安装则跳转到提示安装页面
                    mInstaller.install();
                }else {
                    showTip("语音合成失败,错误码: " + code);
                }
            }

            mDatas.add(from);
            mAdapter.notifyDataSetChanged();
            mChatView.setSelection(mDatas.size() - 1);
        }

        ;
    };
    boolean isClear = false;
    private long start;


    // 语音合成对象
    private SpeechSynthesizer mTts;
    private ApkInstaller mInstaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_chatting);
        initView();
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mChatView.setAdapter(mAdapter);
        mChatView.setSelection(mDatas.size() - 1);
//		ttsManager=new TTSManager(this,"NEMOr9iVcjPBaA1G3GLypcca","4ad1a0bf27ac6f3ef4ef09f77cf1ec78");
//		mChatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//			}
//		});

//		setResource();

//		startTTS();


        findViewById(R.id.tvClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.writerObject(mActivity, mDatas);
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
//				FileUtil.writerObject(MainActivity.this,"ooo");
//				isClear=true;
            }
        });


        mMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String data = pasteData();
                if (data != null && !TextUtils.isEmpty(data)) {
                    showEditDialog(data);
                }
                return true;
            }
        });

        findViewById(R.id.tvName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               long end= SystemClock.elapsedRealtime();
                if(end-start<2000){
                    startActivity(com.lu.momeymanager.view.activity.MainActivity.class);
                }
                start=SystemClock.elapsedRealtime();
            }
        });

//        ShortcutUtil.createShortCut(this,R.drawable.header,R.string.app_name);
        TextView t= (TextView) findViewById(R.id.tvDate);
                t.setText(DateUtil.computerDate()+"-"+DateUtil.computerMarryDate());

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        mInstaller = new  ApkInstaller(this);
    }
    // 默认发音人
    private String voicer = "xiaoyan";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME,  "50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
//            mPercentForBuffering = percent;
//            showTip((percent+""));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            mPercentForPlaying = percent;
//            showTip(percent+"");
        }

        @Override
        public void onCompleted(SpeechError error) {
//            if (error == null) {
//                showTip("播放完成");
//            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
//            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    private void showTip(String msg){
        DialogUtil.showToast(this,msg);
    }
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Debug.d(MainActivity.this, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                TopNoticeDialog.showToast(MainActivity.this,"初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    private void showEditDialog(final String data) {
        String[] items = {"粘贴"};
        DialogUtil.showAlertDialog(mActivity, "选择", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mMsg.setText(data);

                DialogUtil.closeAlertDialog();
            }
        });
    }

    private void setResource() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private void startTTS() {
//        // 获取语音合成对象实例
//        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
//        // 设置context
//        mSpeechSynthesizer.setContext(this);
//        // 设置语音合成状态监听器
//        mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {
//            @Override
//            public void onSynthesizeStart(String s) {
//                d("onSynthesizeStart:" + s);
//            }
//
//            @Override
//            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
//                d("onSynthesizeDataArrived:" + s);
//            }
//
//            @Override
//            public void onSynthesizeFinish(String s) {
//                d("onSynthesizeFinish:" + s);
//            }
//
//            @Override
//            public void onSpeechStart(String s) {
//                d("onSpeechStart:" + s);
//            }
//
//            @Override
//            public void onSpeechProgressChanged(String s, int i) {
//                d("onSpeechProgressChanged:" + s);
//            }
//
//            @Override
//            public void onSpeechFinish(String s) {
//                d("onSpeechFinish:" + s);
//            }
//
//            @Override
//            public void onError(String s, SpeechError speechError) {
//                d("onError:" + s);
//            }
//        });
//        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
//        mSpeechSynthesizer.setApiKey("NEMOr9iVcjPBaA1G3GLypcca", "4ad1a0bf27ac6f3ef4ef09f77cf1ec78");
//        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
//        mSpeechSynthesizer.setAppId("7991891");
//        // 文本模型文件路径 (离线引擎使用)
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
//                + TEXT_MODEL_NAME);
//        // 声学模型文件路径 (离线引擎使用)
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
//                + SPEECH_FEMALE_MODEL_NAME);
//        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
//                + LICENSE_FILE_NAME);
//        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
//        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
//        if (authInfo.isSuccess()) {
//            d("authInfo isSuccess");
//            mSpeechSynthesizer.initTts(TtsMode.MIX);
//            mSpeechSynthesizer.speak("百度语音合成示例程序正在运行");
//        } else {
//            // 授权失败
//            d("authInfo error");
//        }
//    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initData() {

    }

    private void initView() {
        mChatView = (ListView) findViewById(R.id.id_chat_listView);
        mMsg = (EditText) findViewById(R.id.id_chat_msg);

        Object o = FileUtil.readObject(this);
        if (o != null && o instanceof List) {
            mDatas = (List<ChatMessage>) FileUtil.readObject(this);
            d("has local data ");
        }

        if (mDatas.isEmpty()) {
            mDatas.add(new ChatMessage(Type.INPUT, "我是小貅貅，很高兴为您服务"));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//		if(!isClear){
        FileUtil.writerObject(this, mDatas);
//		}

    }

    public void sendMessage(View view) {
        final String msg = mMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "您还没有填写信息呢...", Toast.LENGTH_SHORT).show();
            return;
        }

        ChatMessage to = new ChatMessage(Type.OUTPUT, msg);
        to.setDate(new Date());
        mDatas.add(to);

        mAdapter.notifyDataSetChanged();
        mChatView.setSelection(mDatas.size() - 1);

        mMsg.setText("");

        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }

        new Thread() {
            public void run() {
                ChatMessage from = null;
                try {
                    from = HttpUtils.sendMsg(msg);
                } catch (Exception e) {
                    from = new ChatMessage(Type.INPUT, "服务器挂了呢...");
                }

                Message message = Message.obtain();
                message.obj = from;
                mHandler.sendMessage(message);
            }

            ;
        }.start();

    }

}
