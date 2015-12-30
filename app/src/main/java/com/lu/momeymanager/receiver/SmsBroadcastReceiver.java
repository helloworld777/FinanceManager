package com.lu.momeymanager.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsMessage;

import com.lu.financemanager.R;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")
public class SmsBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsBroadcastReceiver";
	private String puFaFlag;
	private String puFaFlag2;
	private String jianHangFlag;
	private String jianHangFlag2;
	private String puFaNumber;
	private String jinHangNumber;

	private String puFa;
	private String jinHang;
	private String weiHao;
	private String rmb;
	private InOutBeanManager inOutBeanManager;
	private String balance;
	
	private boolean isBalance;
	private String bank;
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d(TAG, "ACTION:" + intent.getAction());
		// Toast.makeText(context, "ACTION:"+intent.getAction(),
		// Toast.LENGTH_LONG).show();
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			
			inOutBeanManager=InOutBeanManager.getDefault();
			if(inOutBeanManager==null){
				InOutBeanManager.init(context);
				inOutBeanManager=InOutBeanManager.getDefault();
			}
			puFaFlag = context.getString(R.string.pufa_out_flag1);
			String pufa_in_flag1 = context.getString(R.string.pufa_in_flag1);
//			String pufa_in_flag1 = context.getString(R.string.pufa_in_flag1);
			
			jianHangFlag = context.getString(R.string.jinhang_flag);
			jianHangFlag2 = context.getString(R.string.jinhang_flag2);
			puFaNumber = context.getString(R.string.pufa_number);
			jinHangNumber = context.getString(R.string.jinhang_number);
			puFa = context.getString(R.string.pufa);
			jinHang = context.getString(R.string.jinhang);
			weiHao = context.getString(R.string.weihao);
			balance = context.getString(R.string.balance_flag);
			// TODO Auto-generated method stub
			rmb=context.getString(R.string.rmb);
			Object[] pduses = (Object[]) intent.getExtras().get("pdus");
			for (Object pdus : pduses) {
				byte[] pdusmessage = (byte[]) pdus;
				SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);
				String mobile = sms.getOriginatingAddress();//
				String body = sms.getMessageBody(); //
				Date date = new Date(sms.getTimestampMillis());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String time = format.format(date); //
				if (mobile.equals(puFaNumber) || mobile.equals(jinHangNumber)) {
					boolean b = false;
					String flag = "";
					if(body.contains(balance)){
						flag=mobile.equals(puFaNumber)?balance:rmb;
						isBalance=true;
					}else{
						isBalance=false;
						if (mobile.equals(puFaNumber)) {
							if (body.contains(puFaFlag)) {
								flag = puFaFlag;
								b = true;
							} else if (body.contains(puFaFlag2)) {
								flag = puFaFlag2;

							}
						}
						if (mobile.equals(jinHangNumber)) {
							if (body.contains(jianHangFlag)) {
								flag = jianHangFlag;
								b = true;
							} else if (body.contains(jianHangFlag2)) {
								flag = jianHangFlag2;

							}
						}
					}
					bank=mobile.equals(puFaNumber)?puFa:jinHang;
					if(!isBalance){
						inOutBeanManager.insertMoneyToTable(inOutBeanManager.resolveMsgToMoneyBean(body, flag, bank, time, b));
					}else{
						inOutBeanManager.insertBalanceToTable(inOutBeanManager.resolveMsgToBalanceBean(body, flag, bank, time, true));
					}
				}
			}
		}
	}

}
