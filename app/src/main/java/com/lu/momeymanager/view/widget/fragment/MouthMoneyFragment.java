package com.lu.momeymanager.view.widget.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lu.financemanager.R;
import com.lu.momeymanager.view.widget.adapter.LuAdapter;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.manager.ExcelManager;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.view.widget.DividerItemDecoration;

import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MouthMoneyFragment extends BaseFragment {

	private static final String TAG = "MouthMoneyFragment";
	private RecyclerView listview;
//	private ListView listview;
	private LuAdapter<SimilarDateMoneyBean> adapter;

	private InOutBeanManager smsManager;

	private List<SimilarDateMoneyBean> similarDateMoneyBeans;
	@SuppressWarnings("unused")
	private double count = 0;
	private int sort=0;
	private DecimalFormat decimalFormat = new DecimalFormat("#.00");
	
	private TextView textView;
	
//	private List<BalanceBean> balanceBeans;
	public MouthMoneyFragment(){
		EventBus.getDefault().register(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext=getContext();
		// TODO Auto-generated method stub
		initData();
		
		View view = inflater.inflate(R.layout.fragment_mouth_money, null);
		listview = findViewById(view, R.id.listview);

//		adapter = new LuAdapter<SimilarDateMoneyBean>(getActivity(), similarDateMoneyBeans, R.layout.item_listview) {
//			@Override
//			public void convert(ViewHolder helper, SimilarDateMoneyBean item) {
//				helper.setString(R.id.tvMumber, item.getCount());
//				if(sort==0){
//					helper.setString(R.id.tvDate, item.getDate());
//				}else{
//					helper.setString(R.id.tvDate, item.getBeans().get(0).getBank());
//				}
//
////				helper.getView(R.id.tvBank).setVisibility(View.GONE);
//				helper.setString(R.id.tvBank, item.getAllConsume());
//				helper.setString(R.id.tvCarNumber, item.getAllIncoming());
//			}
//		};
//		addFooterView();
//		listview.setAdapter(adapter);
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				EventBus.getDefault().post(new BaseEvent(BaseEvent.CHANGE_FRAGMENT,arg2));
//			}
//		});

		listview.setLayoutManager(new LinearLayoutManager(getActivity()));
		listview.setAdapter(new MyAdapter());
		listview.setItemAnimator(new DefaultItemAnimator());
		listview.addItemDecoration(new DividerItemDecoration(mContext,LinearLayoutManager.VERTICAL));
		//添加分割线
//		listview.addItemDecoration(new DividerItemDecoration(
//				getActivity(), DividerItemDecoration.HORIZONTAL_LIST));

		return view;
	}
	class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			MyViewHolder holder=new MyViewHolder(LayoutInflater.from(MouthMoneyFragment.this.getActivity()).inflate(R.layout.item_listview,parent,false));

			return holder;
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, final int position) {
			SimilarDateMoneyBean item=similarDateMoneyBeans.get(position);
//			holder.tvMumber.setText(similarDateMoneyBeans.get(position).);
			holder.tvMumber.setText(item.getCount());
			if(sort==0){
				holder.tvDate.setText(item.getDate());
			}else{
				holder.tvDate.setText(item.getBeans().get(0).getBank());
			}

//				helper.getView(R.id.tvBank).setVisibility(View.GONE);
			holder.tvBank.setText(item.getAllConsume());
			holder.tvCarNumber.setText(item.getAllIncoming());
			holder.rl_item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EventBus.getDefault().post(new BaseEvent(BaseEvent.CHANGE_FRAGMENT,position));
				}
			});
		}


		@Override
		public int getItemCount() {
			return similarDateMoneyBeans.size();
		}
		class MyViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
		{

			TextView tvMumber;
			TextView tvDate;
			TextView tvBank;
			TextView tvCarNumber;
			RelativeLayout rl_item;
			public MyViewHolder(View view)
			{
				super(view);
				tvMumber = (TextView) view.findViewById(R.id.tvMumber);
				tvDate = (TextView) view.findViewById(R.id.tvDate);
				tvBank = (TextView) view.findViewById(R.id.tvBank);
				tvCarNumber = (TextView) view.findViewById(R.id.tvCarNumber);
				rl_item= (RelativeLayout) view.findViewById(R.id.rl_item);
			}
		}
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

//		listview.addFooterView(listViewFoodView);
	}
	private String getBalance(){
//		StringBuffer balance=new StringBuffer();
//		String balanceString=getString(R.string.balance_flag);
//		count = 0;
		StringBuffer buffer=new StringBuffer();
		double in=0,out=0;
//		balanceBeans=smsManager.getBalanceBeans();
		for (SimilarDateMoneyBean bean : similarDateMoneyBeans) {
//			count += bean.getNumber();
			
			for(InOutBean inOutBean:bean.getBeans()){
				
				if(inOutBean.getNumber()>0){
					in+=inOutBean.getNumber();
				}else{
					out+=inOutBean.getNumber();
				}
			};
//			balance.append(bean.getBank()+bean.getCardNumber()+"("+balanceString+":"+bean.getNumber()+"),");
//			LogUtil.d(TAG, "bean.getNumber():"+bean.getNumber());
		}
		buffer.append(""+decimalFormat.format(out)+","+decimalFormat.format(in));
		
		
		
//		balanceBeans=smsManager.getBalanceBeans();
//		for (BalanceBean bean : balanceBeans) {
//			count += bean.getNumber();
//			
//			balance.append(bean.getBank()+bean.getCardNumber()+"("+balanceString+":"+bean.getNumber()+"),");
//			LogUtil.d(TAG, "bean.getNumber():"+bean.getNumber());
//		}
//		balance.append(getString(R.string.count_balance)+":"+decimalFormat.format(count));
		return buffer.toString();
	}
	private void initData(){
		smsManager=InOutBeanManager.getDefault();
		similarDateMoneyBeans=smsManager.getSimilarDateMoneyBeans();
		LogUtil.d(this,"similarDateMoneyBeans.size:"+similarDateMoneyBeans.size());
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
