package com.lu.momeymanager.manager;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lu.financemanager.R;
import com.lu.momeymanager.bean.BalanceBean;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.db.DBHelper;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

public class InOutBeanManager {
	private static final String TAG = "SmsManager";
	private List<InOutBean> moneyBeans;
	private List<SimilarDateMoneyBean> similarDateMoneyBeans;
	private List<BalanceBean> balanceBeans;

	public List<BalanceBean> getBalanceBeans() {
		return balanceBeans;
	}

	public void setBalanceBeans(List<BalanceBean> balanceBeans) {
		this.balanceBeans = balanceBeans;
	}

	private int sort = 0;

	public List<SimilarDateMoneyBean> getSimilarDateMoneyBeans() {
		return similarDateMoneyBeans;
	}

	private DBHelper helper;
	private Context mContext;
	private SQLiteDatabase database;
	private static InOutBeanManager smsManager;

	public static void init(Context context) {
		smsManager = new InOutBeanManager(context);
	}

	public static InOutBeanManager getDefault() {
		// if (smsManager == null) {
		// smsManager = new SmsManager();
		// }
		return smsManager;
	}

	public void queryBalance() {

		if (balanceBeans.size() > 0) {
			return;
		}

		String sqlString = "select * from " + DBHelper.TABLE_BANK;
		Cursor cursor = database.rawQuery(sqlString, null);

		LogUtil.d(TAG, DBHelper.TABLE_BANK + "*****************" + cursor.getCount());
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				String bank = cursor.getString(cursor.getColumnIndex("bank"));
				String cardNumber = cursor.getString(cursor.getColumnIndex("cardNumber"));
				String msg = null;
				String phone = null;
				if (bank.contains(StringUtil.jinHang)) {
					msg = "211#" + cardNumber.substring(2);
					phone = StringUtil.jinHangNumber;
				} else if (bank.contains(StringUtil.puFa)) {
					msg = "Hq";
					phone = StringUtil.puFaNumber;
				}
				sendQueryBalanceMsg(phone, msg);

			}
		}
		cursor.close();
	}

	private void sendQueryBalanceMsg(String phone, String message) {
		LogUtil.d(TAG, "phone:" + phone + "message:" + message);
		PendingIntent pi = PendingIntent.getActivity(mContext, 0, new Intent(), 0);

		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phone, null, text, pi, null);
		}
	}

	public void sendJianHangQueryBalanceMsg() {
		String phone = StringUtil.jinHang;
		String message = mContext.getString(R.string.jianhang_balance_query) + "";
		sendQueryBalanceMsg(phone, message);
	}

	private InOutBeanManager(Context context) {
		this.mContext = context;
		moneyBeans = new ArrayList<InOutBean>();
		similarDateMoneyBeans = new ArrayList<SimilarDateMoneyBean>();
		helper = new DBHelper(context);
		balanceBeans = new ArrayList<BalanceBean>();
		if (database == null) {
			database = helper.getWritableDatabase();
		}
	}


	public List<InOutBean> getAllMoney() {
		return moneyBeans;
	}

//	private String weiHao;
	private Uri SMS_INBOX = Uri.parse("content://sms/");

	// public void getBalance(){
	// balanceBeans.clear();
	// * CREATE TABLE bank (" + "_id INTEGER NOT NULL PRIMARY KEY
	// * AUTOINCREMENT," +"bank VARCHAR(100
	// }

	private void clearData() {

		moneyBeans.clear();
		similarDateMoneyBeans.clear();
		balanceBeans.clear();
	}

	@SuppressLint("SimpleDateFormat")
	public void getSmsFromPhone() {

		clearData();
		long start = System.currentTimeMillis();
		if (database == null) {
			database = helper.getWritableDatabase();
		}
		Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_NAME, null);
		LogUtil.d(TAG, "*************" + DBHelper.TABLE_NAME + " data:" + cursor.getCount());
		if (cursor.getCount() > 0) {
			addSimilarMoneyBeanFromDb(cursor);
			return;
		}
		ContentResolver cr = mContext.getContentResolver();
		Cursor cur = cr.query(SMS_INBOX, null, "address=? or address=?", new String[]{StringUtil.puFaNumber,StringUtil.jinHangNumber}, null);
		if (null == cur) {
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		LogUtil.d(TAG, "��¼:" + cur.getCount());
		while (cur.moveToNext()) {
			String number = cur.getString(cur.getColumnIndex("address"));// �ֻ��
			String body = cur.getString(cur.getColumnIndex("body"));
			long date = cur.getLong(cur.getColumnIndex("date"));
			String date2 = format.format(new Date(date));
			long id = cur.getLong(cur.getColumnIndex("_id"));
			LogUtil.d(TAG, "date:" + date2 + ",_id:" + id);
			String flag=null;
			boolean out=true;
			String bank=null;
			if (number.equals(StringUtil.puFaNumber) || number.equals(StringUtil.jinHangNumber)) {
				if (number.equals(StringUtil.puFaNumber)) {
					bank=StringUtil.puFa;
					if (body.contains(StringUtil.pufaOutFlag1)) {
						flag=StringUtil.pufaOutFlag1;
						out=true;
						addMoneyBean(body, flag, bank, date2, out);
					} else if (body.contains(StringUtil.puFaInFlag2)) {
						flag=StringUtil.puFaInFlag2;
						out=false;
						addMoneyBean(body, flag, bank, date2, out);
					} else if(body.contains(StringUtil.pufaOutFlag2)){
						flag=StringUtil.pufaOutFlag2;
						out=true;
						addMoneyBean(body, flag, bank, date2, out);
					}
				} else if (number.equals(StringUtil.jinHangNumber)) {
					bank=StringUtil.jinHang;
					if (body.contains(StringUtil.jianHangOutFlag)) {
						flag=StringUtil.jianHangOutFlag;
						out=true;
						addMoneyBean(body, flag, bank, date2, out);
					} else if (body.contains(StringUtil.jianHangInFlag)) {
						flag=StringUtil.jianHangInFlag;
						out=false;
						addMoneyBean(body, flag, bank, date2, out);
					}
				}
				
				// if (body.contains(balance)) {
				// BalanceBean balanceBean = resolveMsgToBalanceBean(body,
				// balance, number.equals(puFaNumber) ? puFa : jinHang, date2,
				// true);
				// if (balanceBean != null) {
				// insertBalanceToTable(balanceBean);
				// }
				//
				// }
			} else {

			}

		}
		long end = System.currentTimeMillis();
		LogUtil.d(TAG, "*****************getSmsFromPhone:" + (end - start) / 1000.0 + "s");
	}
	private void resolveFlag(){
		
	}
	public void deleteSMS(ContentResolver CR) {
		try {
			// Query SMS
			Uri uriSms = Uri.parse("content://sms/sent");
			Cursor c = CR.query(uriSms, new String[] { "_id", "thread_id" }, null, null, null);
			if (null != c && c.moveToFirst()) {
				do {
					// Delete SMS
					long threadId = c.getLong(1);
					CR.delete(Uri.parse("content://sms/conversations/" + threadId), null, null);
					Log.d("deleteSMS", "threadId:: " + threadId);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("deleteSMS", "Exception:: " + e);
		}
	}

	private void addSimilarMoneyBeanFromDb(Cursor cursor) {
		while (cursor.moveToNext()) {
			double number = cursor.getDouble(cursor.getColumnIndex("number"));
			String bank = cursor.getString(cursor.getColumnIndex("bank"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String note = cursor.getString(cursor.getColumnIndex("note"));
			String cardNumber = cursor.getString(cursor.getColumnIndex("cardNumber"));
			InOutBean moneyBean = new InOutBean(number + "", note, date, bank, number, cardNumber);
			addSimilarMoneyBean(moneyBean);
		}
		cursor.close();
	}

	private void addBalanceFromDb(Cursor cursor2) {
		while (cursor2.moveToNext()) {
			String bank = cursor2.getString(cursor2.getColumnIndex("bank"));
			String cardNumber = cursor2.getString(cursor2.getColumnIndex("cardNumber"));
			String date = cursor2.getString(cursor2.getColumnIndex("date"));
			double balance = cursor2.getDouble(cursor2.getColumnIndex("balance"));
			BalanceBean balanceBean = new BalanceBean(cardNumber, bank, balance, date);
			balanceBeans.add(balanceBean);
		}
		cursor2.close();
	}

	private String[] resolverMsg(String body, String flag, boolean b) {
		String[] strings = new String[2];
		String str = body.substring(body.indexOf(flag));

		LogUtil.d(TAG, "str:" + str + ",flag:" + flag);

		if (flag.equals(StringUtil.balance)) {
			if (str.charAt(flag.length()) < '0' || str.charAt(flag.length()) > '9') {

				LogUtil.d(TAG, "first latter:" + str.charAt(flag.length()));
				return null;
			}
		}
		int index = str.indexOf(".");
		if (index == -1)
			return null;
		LogUtil.d(TAG, "index:" + index);
		String str2 = str.substring(flag.length(), index + 2);

		int weiHaoIndex = body.indexOf(StringUtil.weiHao);
		String weiHaoNumber = body.substring(weiHaoIndex, weiHaoIndex + StringUtil.weiHao.length() + 4);
		// bank += "(" + weiHaoNumber + ")";
		strings[0] = weiHaoNumber;
		double number = 0;
		try {
			number = Double.valueOf(str2);
		} catch (NumberFormatException e) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < str2.length(); i++) {
				char c = str2.charAt(i);
				if ((c >= '0' && c <= '9') || c == '.') {
					sBuffer.append(c);
				}
			}
			number = Double.valueOf(new String(sBuffer));
		}
		// if (number == 0) {
		// return;
		// }
		if (b) {
			str2 = "-" + str2;
			number = -number;
		}
		strings[1] = "" + number;
		return strings;
	}

	public BalanceBean resolveMsgToBalanceBean(String body, String flag, String bank, String date, boolean out) {
		String[] str2 = resolverMsg(body, flag, false);
		// LogUtil.d(TAG, "str2[0]:"+str2[0]+",str2[1]:"+str2[1]);
		return str2 == null ? null : new BalanceBean(str2[0], bank, Double.valueOf(str2[1]), date);
	}

	public InOutBean resolveMsgToMoneyBean(String body, String flag, String bank, String date, boolean out) {
		String[] str2 = resolverMsg(body, flag, out);
		return str2 == null ? null : new InOutBean(str2[0], "", date, bank, Double.valueOf(str2[1]), str2[0]);
	}

	/**
	 * CREATE TABLE bank (" + "_id INTEGER NOT NULL PRIMARY KEY
	 * AUTOINCREMENT," +"bank VARCHAR(100)," + "cardNumber varchar(100)," +
	 * "date VARCHAR(50), balance double)
	 * 
	 * @param balanceBean
	 */
	public void insertBalanceToTable(BalanceBean balanceBean) {
		// helper.createBankTable(database);

		String sqlString = "select * from " + DBHelper.TABLE_BANK + " where cardNumber='" + balanceBean.getCardNumber() + "'";
		Cursor cursor = database.rawQuery(sqlString, null);
		if (balanceBeans.contains(balanceBean)) {

		} else {
			balanceBeans.add(balanceBean);
			EventBus.getDefault().post(new BaseEvent(BaseEvent.UPDATE_BALANCE, null));
			LogUtil.d(TAG, "*********add " + balanceBean.getCardNumber());
		}

		if (cursor.getCount() > 0) {
		} else {
			ContentValues cValues = new ContentValues();
			cValues.put("balance", balanceBean.getNumber());
			cValues.put("bank", balanceBean.getBank());
			cValues.put("cardNumber", balanceBean.getCardNumber());
			cValues.put("date", balanceBean.getDate());
			database.insert(DBHelper.TABLE_BANK, null, cValues);
			LogUtil.d(TAG, "*********insert into balanceBean a data:" + balanceBean.getCardNumber());

		}

	}

	private void addMoneyBean(String body, String flag, String bank, String date, boolean b) {

		InOutBean bean = resolveMsgToMoneyBean(body, flag, bank, date, b);

		if (bean != null) {
			addSimilarMoneyBean(bean);

			insertMoneyToTable(bean);

		}

	}

	/**
	 * (" + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "number
	 * double," + "bank VARCHAR(100)," + "cardNumber varchar(100)," + "date
	 * VARCHAR(50), note varchar(100)
	 */
	public void insertMoneyToTable(InOutBean moneyBean) {
		ContentValues cValues = new ContentValues();
		cValues.put("number", moneyBean.getNumber());
		cValues.put("bank", moneyBean.getBank());
		cValues.put("cardNumber", moneyBean.getCardNumber());
		cValues.put("date", moneyBean.getDate());
		cValues.put("note", "");
		database.insert(DBHelper.TABLE_NAME, null, cValues);
		LogUtil.d("SmsBroadcastReceiver", "*********insert into moneyBean a data");
	}

	private void addSimilarMoneyBean(InOutBean bean) {
		boolean isHas = false;
		for (SimilarDateMoneyBean oldSimilarDateMoneyBean : similarDateMoneyBeans) {
			oldSimilarDateMoneyBean.setSort(sort);
			if (oldSimilarDateMoneyBean.isSimilar(bean)) {
				List<InOutBean> oldBeans = oldSimilarDateMoneyBean.getBeans();
				oldBeans.add(bean);
				isHas = true;
				break;
			}
		}
		if (!isHas) {
			List<InOutBean> moneyBeans = new ArrayList<InOutBean>();
			moneyBeans.add(bean);
			similarDateMoneyBeans.add(new SimilarDateMoneyBean(bean.getDate().substring(0, 7), moneyBeans));
		}
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;

	}

	public void refresh() {
		clearDateBase();
		getSmsFromPhone();
	}

	private void clearDateBase() {
		String sqlDeleteMoney = "delete  from " + DBHelper.TABLE_NAME;
		database.execSQL(sqlDeleteMoney);

		String sqlDeleteBank = "delete from " + DBHelper.TABLE_BANK;
		database.execSQL(sqlDeleteBank);
	}
}
