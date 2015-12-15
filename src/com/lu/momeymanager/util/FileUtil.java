package com.lu.momeymanager.util;

import android.annotation.SuppressLint;
import java.io.File;

@SuppressLint("SdCardPath") 
public class FileUtil {
	public static final String APP_PATH="/sdcard/lu/moneymanager/";
	public static final String APP_EXCEL_PATH="/sdcard/lu/moneymanager/excel/";
	public static final String CARSH_PATH="/sdcard/lu/moneymanager/crash/";
	public static String mkdir(String path){
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		return path;
	}
	public static String getCarshPath(){
		return mkdir(CARSH_PATH);
	}
	public static String getAppPath(){
		return mkdir(APP_PATH);
	}
	public static String getExcelPath(){
		return mkdir(APP_EXCEL_PATH);
	}
}
