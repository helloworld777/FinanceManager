package com.lu.momeymanager.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 *
 */
public final class ScreenUtil {

	public static int[] getScreenSize(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return new int[] { metrics.widthPixels, metrics.heightPixels };
	}
//	public static int[] getScreenSize(Context activity) {
//		DisplayMetrics metrics = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//		return new int[] { metrics.widthPixels, metrics.heightPixels };
//	}
}
