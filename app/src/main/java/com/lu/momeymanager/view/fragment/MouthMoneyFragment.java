package com.lu.momeymanager.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lu.financemanager.R;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.AsyncTaskUtil;
import com.lu.momeymanager.util.LogUtil;
import com.lu.momeymanager.view.widget.DividerItemDecoration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class MouthMoneyFragment extends BaseFragment {

	private static final String TAG = "MouthMoneyFragment";
	private RecyclerView listview;
	private MyAdapter adapter;
//	private InOutBeanManager smsManager;
	private List<SimilarDateMoneyBean> similarDateMoneyBeans;
	@SuppressWarnings("unused")
	private double count = 0;
	private int sort=0;
	private DecimalFormat decimalFormat = new DecimalFormat("#.00");
	
	private TextView textView;
	
//	private List<BalanceBean> balanceBeans;
	public MouthMoneyFragment(){
		EventBus.getDefault().register(this);
		smsManager=InOutBeanManager.getDefault();
		similarDateMoneyBeans=smsManager.getSimilarDateMoneyBeans();
	}

	private ColumnChartView chart;
	private ColumnChartData data;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLabels = false;
	private boolean hasLabelForSelected = false;
	private InOutBeanManager smsManager;
	private int type=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext=getContext();
		View view = inflater.inflate(R.layout.fragment_mouth_money, null);
		adapter=new MyAdapter();
		listview = findViewById(view, R.id.listview);
		listview.setLayoutManager(new LinearLayoutManager(getActivity()));
		listview.setAdapter(adapter);
		listview.setItemAnimator(new DefaultItemAnimator());
		listview.addItemDecoration(new DividerItemDecoration(mContext,LinearLayoutManager.VERTICAL));
		//添加分割线
//		listview.addItemDecoration(new DividerItemDecoration(
//				getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
//		listview.addf
		chart=findViewById(view,R.id.chart);
		generateNegativeSubcolumnsData();


		Button button=findViewById(view,R.id.btnChart);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listview.setVisibility(View.GONE);
				chart.setVisibility(View.VISIBLE);
			}
		});
		initData();
		return view;
	}
	public void changeShowType(int type){
		switch (type){
			case 0:
				listview.setVisibility(View.VISIBLE);
				chart.setVisibility(View.GONE);
				break;
			case 1:
				listview.setVisibility(View.GONE);
				chart.setVisibility(View.VISIBLE);
				break;
		}
	}
	private void generateNegativeSubcolumnsData() {

		int numSubcolumns = 2;
		int numColumns=0;
			numColumns = similarDateMoneyBeans.size();
		d("numColumns:"+numColumns);
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			values.add(new SubcolumnValue(Float.parseFloat(similarDateMoneyBeans.get(i).getAllConsume()), ChartUtils.COLORS[2]));
			values.add(new SubcolumnValue(Float.parseFloat(similarDateMoneyBeans.get(i).getAllIncoming()), ChartUtils.COLORS[3]));

//			for (int j = 0; j < numSubcolumns; ++j) {
//				int sign = getSign();
//				values.add(new SubcolumnValue((float) Math.random() * 50f * sign + 5 * sign, ChartUtils.pickColor
//						()));
//			}
			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);
		}

		data = new ColumnChartData(columns);
		data.setStacked(true);
//		List<AxisValue> values1=new ArrayList<AxisValue>();
//		AxisValue axisValue=new AxisValue();
//		values1.add();
		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
//			axisY.setValues(values1);
			if (hasAxesNames) {
				axisX.setName("Mouth");
				axisY.setName("number");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		chart.setColumnChartData(data);
	}
	private int getSign() {
		int[] sign = new int[]{-1, 1};
		return sign[Math.round((float) Math.random())];
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
			holder.tvMumber.setText(item.getCount());
			if(sort==0){
				holder.tvDate.setText(item.getDate());
			}else{
				holder.tvDate.setText(item.getBeans().get(0).getBank());
			}

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
	public void updateData(final int sortType){
		sort=sortType;
		smsManager.setSort(sortType);
		similarDateMoneyBeans.clear();
		initData();;
//		dialogLoading.show();
//		new AsyncTaskUtil().setIAsyncTaskCallBack(new AsyncTaskUtil.IAsyncTaskCallBack() {
//			@Override
//			public Object doInBackground(String... arg0) {
//				smsManager.setSort(sortType);
//				smsManager.refresh();
//				sort=sortType;
//				return null;
//			}
//
//			@Override
//			public void onPostExecute(Object result) {
//				adapter.notifyDataSetChanged();
//				dialogLoading.dismiss();
//			}
//		}).execute("");



	}
	private void addFooterView(){
		LinearLayout listViewFoodView = new LinearLayout(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		params.bottomMargin = 200;
		params.leftMargin = 100;
		textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText(getBalance());
		listViewFoodView.addView(textView, params);
	}
	private String getBalance(){
		StringBuffer buffer=new StringBuffer();
		double in=0,out=0;
		for (SimilarDateMoneyBean bean : similarDateMoneyBeans) {
			for(InOutBean inOutBean:bean.getBeans()){
				if(inOutBean.getNumber()>0){
					in+=inOutBean.getNumber();
				}else{
					out+=inOutBean.getNumber();
				}
			};
		}
		buffer.append(""+decimalFormat.format(out)+","+decimalFormat.format(in));
		
		return buffer.toString();
	}
	private void initData(){
		if(similarDateMoneyBeans.isEmpty()){
			new AsyncTaskUtil(getActivity()).setIAsyncTaskCallBack(new AsyncTaskUtil.IAsyncTaskCallBack() {
				@Override
				public Object doInBackground(String... arg0) {
					smsManager.getSmsFromPhone();
					return null;
				}
				@Override
				public void onPostExecute(Object result) {
					adapter.notifyDataSetChanged();
				}
			}).execute("");
			;
		}
		LogUtil.d(this,"similarDateMoneyBeans.size:"+similarDateMoneyBeans.size());
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
