package com.lu.momeymanager.app;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.FileUtil;
import com.lu.momeymanager.util.StringUtil;
import com.lu.momeymanager.view.lockpatternview.LockPatternUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class MomeyManagerApplication extends Application {
	private LockPatternUtils mLockPatternUtils;
	private static MomeyManagerApplication managerApplication;
	private String username;

	@Override
	public void onCreate() {
		super.onCreate();
		StringUtil.initString(this);
		managerApplication=this;
		InOutBeanManager.init(this);
		initImageLoader();

		SpeechUtility.createUtility(this, "appid=57282072 ");


	}

	private void initUmeng() {
	}

	private void initImageLoader() {

		File cacheDir = new File(FileUtil.getImgCachePath());
		ImageLoaderConfiguration config  = new ImageLoaderConfiguration
				.Builder(this)
				.memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY -2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2* 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheFileCount(100) //缓存的文件数量
				.diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(this,5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for releaseapp
				.build();//开始构建
		ImageLoader.getInstance().init(config);
	}

	public String getUsername(){
//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		return username;
	}
	public void setUsername(String s){
//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		 username=s;
	}
	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	public static MomeyManagerApplication getDefault(){
		return managerApplication;
	}
	public void setmLockPatternUtils(LockPatternUtils mLockPatternUtils) {
		this.mLockPatternUtils = mLockPatternUtils;
	}
	public static MomeyManagerApplication getInstance(){
		return managerApplication;
	}
}
