package com.lu.momeymanager.util;


import android.content.Context;

import com.lu.financemanager.R;

public class StringUtil {
	public  static String pufaOutFlag1;
	public  static String pufaOutFlag2;
	public static String puFaInFlag2;
	public static String puFaInFlag1;
	public  static String jianHangInFlag;
	public static String jianHangOutFlag;
	public static String puFaNumber;
	public static String jinHangNumber;

	public static String puFa;
	public static String jinHang;
	public static String weiHao;
	public static String rmb;
	
	public static String balance;
	public static void initString(Context context){
		
		pufaOutFlag1 = context.getString(R.string.pufa_out_flag1);
		pufaOutFlag2 = context.getString(R.string.pufa_out_flag2);
		puFaInFlag1 = context.getString(R.string.pufa_in_flag1);
		puFaInFlag2 = context.getString(R.string.pufa_in_flag2);
		jianHangOutFlag = context.getString(R.string.jinhang_flag);
		jianHangInFlag = context.getString(R.string.jinhang_flag2);
		puFaNumber = context.getString(R.string.pufa_number);
		jinHangNumber = context.getString(R.string.jinhang_number);
		puFa = context.getString(R.string.pufa);
		jinHang = context.getString(R.string.jinhang);
		weiHao = context.getString(R.string.weihao);
		balance = context.getString(R.string.balance_flag);
		// TODO Auto-generated method stub
		rmb=context.getString(R.string.rmb);
		
	}
}
