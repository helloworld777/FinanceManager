package com.lu.momeymanager.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
	public static void writerObject(Context context,Object o){
		File cacheDir = context.getCacheDir();//文件所在目录为getFilesDir();
		String cachePath=cacheDir.getPath();
		File file=new File(cachePath+File.separator+"objectCache.data");
		if(!file.exists()){
			try {
				file.createNewFile();file.delete();
			} catch (IOException e) {
				e.printStackTrace();
				Debug.d(FileUtil.class,"createNewFile error");
			}
		}
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream=new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(o);
			objectOutputStream.flush();
			Debug.d(FileUtil.class,"writeObject success");
		} catch (IOException e) {
			e.printStackTrace();
			Debug.d(FileUtil.class,"writeObject error");
		}finally {
			if(objectOutputStream!=null){
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static Object readObject(Context context){
		File cacheDir = context.getCacheDir();//文件所在目录为getFilesDir();
		String cachePath=cacheDir.getPath();
		File file=new File(cachePath+File.separator+"objectCache.data");
		ObjectInputStream inputStream=null;
		try {
			 inputStream=new ObjectInputStream(new FileInputStream(file));
			return inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			Debug.d(FileUtil.class,"readObject error");
		}finally {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
