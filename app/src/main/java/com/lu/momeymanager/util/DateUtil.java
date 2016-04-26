package com.lu.momeymanager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static Calendar calendar=null;
	
	/**
	 * 
	 * @return 2014-11-18 14:16:33
	 */
	public static String getDate(){
		
		return getDay()+" "+getTime();
	}
	/**
	 * 
	 * @return 2014/11/18 14:16:33
	 */
	public static String getDate2(){
		
		return getDay2()+" "+getTime();
	}
	/**
	 * 
	 * @return 2014_11_18_14_16_33
	 */
	public static String getDate3(){
		
		return getDay3()+"_"+getTime3();
	}
	/**
	 * 
	 * @return 14:16:33
	 */
	public static String getTime(){
		calendar=Calendar.getInstance();
		StringBuffer buffer=new StringBuffer();
		final int hour=calendar.get(Calendar.HOUR_OF_DAY);
		if(hour<10){
			buffer.append("0"+hour);
		}else{
			buffer.append(String.valueOf(hour));
		}
		buffer.append(":");
		final int minute=calendar.get(Calendar.MINUTE);
		if(minute<10){
			buffer.append("0"+minute);
		}else{
			buffer.append(String.valueOf(minute));
		}
		buffer.append(":");
		final int second=calendar.get(Calendar.SECOND);
		if(second<10){
			buffer.append("0"+second);
		}else{
			buffer.append(String.valueOf(second));
		}
		
		return new String(buffer);
	}
	public static String computerDate(){
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		Date date= null;
		Date now=null;
		try {
			date = sdf.parse("2015-11-22");
			now=sdf.parse(sdf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}

//		long now= System.currentTimeMillis();
//		Date now=sdf.parse(sdf.format(new Date()));
		Debug.d("date","date.getTime():"+date.getTime());
		Debug.d("date"," now.getTime():"+now.getTime());

		return String.valueOf((now.getTime()-date.getTime())/(1000*60*60*24));
	}
	/**
	 * 
	 */
	public static String getTime3(){
		calendar=Calendar.getInstance();
		StringBuffer buffer=new StringBuffer();
		final int hour=calendar.get(Calendar.HOUR_OF_DAY);
		if(hour<10){
			buffer.append("0"+hour);
		}else{
			buffer.append(String.valueOf(hour));
		}
		buffer.append("_");
		final int minute=calendar.get(Calendar.MINUTE);
		if(minute<10){
			buffer.append("0"+minute);
		}else{
			buffer.append(String.valueOf(minute));
		}
		buffer.append("_");
		final int second=calendar.get(Calendar.SECOND);
		if(second<10){
			buffer.append("0"+second);
		}else{
			buffer.append(String.valueOf(second));
		}
		
		return new String(buffer);
	}
	/**
	 * 
	 */
	public static String getTime2(){
		calendar=Calendar.getInstance();
		StringBuffer buffer=new StringBuffer();
		 int hour=calendar.get(Calendar.HOUR_OF_DAY);
		
		if(hour >12){
			buffer.append("0");
			hour-=12;
		}else{
			buffer.append("0");
			
		}
		
//		if(hour<10){
//			buffer.append("0"+hour);
//		}else {
			buffer.append(String.valueOf(hour));
//		}
		
		
		buffer.append(":");
		final int minute=calendar.get(Calendar.MINUTE);
		if(minute<10){
			buffer.append("0"+minute);
		}else{
			buffer.append(String.valueOf(minute));
		}
		
	
		
		return new String(buffer);
	}
	
	/**
	 * 
	 */
	public static String getDay(){
		calendar=Calendar.getInstance();
		final StringBuffer buffer=new StringBuffer();
		
		final int year=calendar.get(Calendar.YEAR);
		if(year<10){
			buffer.append("0"+year);
		}else{
			buffer.append(String.valueOf(year));
		}
		buffer.append("-");
		final int mouth=(calendar.get(Calendar.MONTH)+1);
		if(mouth<10){
			buffer.append("0"+mouth);
		}else{
			buffer.append(String.valueOf(mouth));
		}
		buffer.append("-");
		final int day=calendar.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			buffer.append("0"+day);
		}else{
			buffer.append(String.valueOf(day));
		}
		
		return new String(buffer);
	}
	/**
	 * 
	 */
	public static String getDay3(){
		calendar=Calendar.getInstance();
		final StringBuffer buffer=new StringBuffer();
		
		final int year=calendar.get(Calendar.YEAR);
		if(year<10){
			buffer.append("0"+year);
		}else{
			buffer.append(String.valueOf(year));
		}
		buffer.append("_");
		final int mouth=(calendar.get(Calendar.MONTH)+1);
		if(mouth<10){
			buffer.append("0"+mouth);
		}else{
			buffer.append(String.valueOf(mouth));
		}
		buffer.append("_");
		final int day=calendar.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			buffer.append("0"+day);
		}else{
			buffer.append(String.valueOf(day));
		}
		
		return new String(buffer);
	}
	/**
	 * 
	 */
	public static String getDay2(){
		calendar=Calendar.getInstance();
		final StringBuffer buffer=new StringBuffer();
		
		final int year=calendar.get(Calendar.YEAR);
		if(year<10){
			buffer.append("0"+year);
		}else{
			buffer.append(String.valueOf(year));
		}
		buffer.append("/");
		final int mouth=(calendar.get(Calendar.MONTH)+1);
		if(mouth<10){
			buffer.append("0"+mouth);
		}else{
			buffer.append(String.valueOf(mouth));
		}
		buffer.append("/");
		final int day=calendar.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			buffer.append("0"+day);
		}else{
			buffer.append(String.valueOf(day));
		}
		return new String(buffer);
	}
	
	
}
