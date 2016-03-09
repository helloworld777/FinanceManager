package com.lu.momeymanager.view.widget.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lu.momeymanager.R;
import com.lu.momeymanager.view.widget.adapter.LuAdapter;
import com.lu.momeymanager.view.widget.adapter.ViewHolder;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.bean.SimilarDateMoneyBean;
import com.lu.momeymanager.manager.InOutBeanManager;

public class DetailMouthMoneyFragment extends BaseFragment {

	private ListView listview;
	private LuAdapter<InOutBean> adapter;

	private InOutBeanManager smsManager;

	private SimilarDateMoneyBean similarDateMoneyBean;
	private int position;
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
		// TODO Auto-generated method stub
		initData();
		
		View view = inflater.inflate(R.layout.fragment_mouth_money, null);
		listview = findViewById(view, R.id.listview);

		adapter = new LuAdapter<InOutBean>(getActivity(), similarDateMoneyBean.getBeans(), R.layout.item_listview) {
			@Override
			public void convert(ViewHolder helper, InOutBean item) {
				helper.setString(R.id.tvMumber, ""+item.getNumber());
				helper.setString(R.id.tvDate, item.getDate());
				helper.setString(R.id.tvBank, item.getBank());
				helper.setString(R.id.tvCarNumber, item.getCardNumber());
//				helper.getView(R.id.tvBank).setVisib);
			}
		};
		
		listview.setAdapter(adapter);
		
		return view;
	}
	
	private void initData(){
		smsManager=InOutBeanManager.getDefault();
		position=getArguments().getInt("position", 0);
		similarDateMoneyBean=smsManager.getSimilarDateMoneyBeans().get(position);
	}
}
