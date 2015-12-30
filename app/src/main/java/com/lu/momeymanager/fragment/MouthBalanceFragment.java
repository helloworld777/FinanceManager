package com.lu.momeymanager.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.lu.financemanager.R;
import com.lu.momeymanager.adapter.LuAdapter;
import com.lu.momeymanager.adapter.ViewHolder;
import com.lu.momeymanager.bean.BalanceBean;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.manager.ExcelManager;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.LogUtil;

import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MouthBalanceFragment extends BaseFragment {

	private static final String TAG = "MouthMoneyFragment";
	private ListView listview;
	private LuAdapter<SimilarDateMoneyBean> adapter;

	private InOutBeanManager smsManager;

	private List<SimilarDateMoneyBean> similarDateMoneyBeans;
	private double count = 0;
	private int sort=0;
	private DecimalFormat decimalFormat = new DecimalFormat("#.00");
	
	private TextView textView;
	
	private List<BalanceBean> balanceBeans;
	public MouthBalanceFragment(){
		EventBus.getDefault().register(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initData();
		
		View view = inflater.inflate(R.layout.fragment_mouth_money, null);
		listview = findViewById(view, R.id.listview);

		adapter = new LuAdapter<SimilarDateMoneyBean>(getActivity(), similarDateMoneyBeans, R.layout.item_listview) {
			@Override
			public void convert(ViewHolder helper, SimilarDateMoneyBean item) {
				helper.setString(R.id.tvMumber, item.getCount());
				if(sort==0){
					helper.setString(R.id.tvDate, item.getDate());
				}else{
					helper.setString(R.id.tvDate, item.getBeans().get(0).getBank());
				}
				
//				helper.getView(R.id.tvBank).setVisibility(View.GONE);
				helper.setString(R.id.tvBank, item.getAllConsume());
				helper.setString(R.id.tvCarNumber, item.getAllIncoming());
			}
		};
		addFooterView();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				EventBus.getDefault().post(new BaseEvent(BaseEvent.CHANGE_FRAGMENT,arg2));
			}
		});
		return view;
	}
	public void updateData(int sortType){
		smsManager.setSort(sortType);
		smsManager.refresh();
		sort=sortType;
		textView.setText(getBalance());
		adapter.notifyDataSetChanged();
	}
	private void addFooterView(){
		LinearLayout listViewFoodView = new LinearLayout(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		params.bottomMargin = 200;
		params.leftMargin = 100;
		textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		
//		for (SimilarDateMoneyBean bean : similarDateMoneyBeans) {
//			count += bean.getBalance();
//		}
		textView.setText(getBalance());
		listViewFoodView.addView(textView, params);

		listview.addFooterView(listViewFoodView);
	}
	private String getBalance(){
		StringBuffer balance=new StringBuffer();
		String balanceString=getString(R.string.balance_flag);
		count = 0;
		balanceBeans=smsManager.getBalanceBeans();
		for (BalanceBean bean : balanceBeans) {
			count += bean.getNumber();
			
			balance.append(bean.getBank()+bean.getCardNumber()+"("+balanceString+":"+bean.getNumber()+"),");
			LogUtil.d(TAG, "bean.getNumber():"+bean.getNumber());
		}
		balance.append(getString(R.string.count_balance)+":"+decimalFormat.format(count));
		return balance.toString();
	}
	private void initData(){
		smsManager=InOutBeanManager.getDefault();
		similarDateMoneyBeans=smsManager.getSimilarDateMoneyBeans();
		
		ExcelManager.excelToDisk(similarDateMoneyBeans);
	}
	public void onEventMainThread(BaseEvent baseEvent) {
		switch (baseEvent.getEventType()) {
		case BaseEvent.UPDATE_MAIN:
			updateData((Integer)baseEvent.getData());
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
