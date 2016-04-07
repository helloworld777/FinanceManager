package com.lu.momeymanager.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lu.financemanager.R;
import com.lu.momeymanager.view.adapter.LuAdapter;
import com.lu.momeymanager.view.adapter.ViewHolder;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.manager.InOutBeanManager;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class DetailMouthMoneyFragment extends BaseFragment implements AdapterView.OnItemClickListener {

	private ListView listview;
	private MyAdater adapter;
	private ColumnChartView chart;
	private ColumnChartData data;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLabels = false;
	private boolean hasLabelForSelected = false;
	private InOutBeanManager smsManager;

	private SimilarDateMoneyBean similarDateMoneyBean;
	private int position;
//	private LuAdapter<InOutBean> adapter;
//	private double count = 0;
	public static DetailMouthMoneyFragment newInstance(int poistion){
		DetailMouthMoneyFragment detailMouthMoneyFragment=new DetailMouthMoneyFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("position", poistion);
		detailMouthMoneyFragment.setArguments(bundle);

		return detailMouthMoneyFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initData();
		View view = inflater.inflate(R.layout.fragment_detail_mouth_money, null);
		listview = findViewById(view, R.id.listview);
		adapter = new MyAdater(getActivity(), similarDateMoneyBean.getBeans(), R.layout.item_listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		chart=findViewById(view,R.id.chart);
//		generateNegativeSubcolumnsData();

		Button button=findViewById(view,R.id.btnChart);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listview.setVisibility(View.GONE);
				chart.setVisibility(View.VISIBLE);
			}
		});
		return view;
	}
	private void generateNegativeSubcolumnsData() {

		int numSubcolumns = 1;
		int numColumns=0;
		numColumns = smsManager.getAllMoney().size();

		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {

			values = new ArrayList<SubcolumnValue>();
//			for (int j = 0; j < numSubcolumns; ++j) {
//				int sign = getSign();
			values.add(new SubcolumnValue(Float.parseFloat(similarDateMoneyBean.getBeans().get(i).getNumberText()), ChartUtils.pickColor
					()));
//			values.add(new SubcolumnValue(Float.parseFloat(similarDateMoneyBean.getBeans().get(i).getNumberText()), ChartUtils.pickColor
//					()));
//			}

			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);
		}

		data = new ColumnChartData(columns);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("Axis X");
				axisY.setName("Axis Y");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		chart.setColumnChartData(data);
	}
	class MyAdater extends LuAdapter<InOutBean>{
		public MyAdater(Context context, List<InOutBean> datas, int mItemLayoutId) {
			super(context, datas, mItemLayoutId);
		}

		@Override
		public void convert(ViewHolder helper, InOutBean item) {
			helper.setString(R.id.tvMumber, ""+item.getNumber());
			helper.setString(R.id.tvDate, item.getDate());
			helper.setString(R.id.tvBank, item.getBank());
			helper.setString(R.id.tvCarNumber, item.getCardNumber());
			if(item.isShowDetial){
				helper.getView(R.id.tvDetail).setVisibility(View.VISIBLE);
				helper.setString(R.id.tvDetail,item.detail);
			}else{
				helper.getView(R.id.tvDetail).setVisibility(View.GONE);
			}

		}
		public void setShowDetail(int position){
			InOutBean inOutBean=datas.get(position);
			inOutBean.isShowDetial=!inOutBean.isShowDetial;
			notifyDataSetChanged();
		}
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
	private void initData(){
		smsManager=InOutBeanManager.getDefault();
		position=getArguments().getInt("position", 0);
		similarDateMoneyBean=smsManager.getSimilarDateMoneyBeans().get(position);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		adapter.setShowDetail(position);
	}
}
