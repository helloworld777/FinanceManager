package com.lu.momeymanager.activity;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.momeymanager.R;
import com.lu.momeymanager.adapter.LuAdapter;
import com.lu.momeymanager.adapter.ViewHolder;
import com.lu.momeymanager.bean.BalanceBean;
import com.lu.momeymanager.bean.BaseEvent;
import com.lu.momeymanager.manager.InOutBeanManager;
import com.lu.momeymanager.util.LogUtil;

import de.greenrobot.event.EventBus;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_balance_query)
public class BalanceQueryActivity extends BaseHeaderActivity{
	
	
	private static final String TAG = "BalanceQueryActivity";
	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;
	
	@ViewInject(R.id.ivMore)
	private ImageView ivMore;
	
	
	@ViewInject(R.id.btnRefresh)
	private Button btnRefresh;
	
	@ViewInject(R.id.listview)
	private ListView listView;
	
	private LuAdapter<BalanceBean> luAdapter;
	
	private InOutBeanManager manager;
	@Override
	@TargetApi(19)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		manager=InOutBeanManager.getDefault();
		EventBus.getDefault().register(this);
		initData();
		initWidget();
	}


	private void initData() {
		manager.queryBalance();
	}
	public void onEventMainThread(BaseEvent event){
		if(event.getEventType()==BaseEvent.UPDATE_BALANCE){
			LogUtil.d(TAG, "notifyDataSetChanged");
			luAdapter.notifyDataSetChanged();
			
		}
	}
	@com.lidroid.xutils.view.annotation.event.OnClick({R.id.ivBack})
	public void OnClick(View view){
		if(view.getId()==R.id.ivBack){
			finish();
		}
	}
	private void initWidget() {
		tvTitle.setText(getString(R.string.balance_query));
		ivMore.setVisibility(View.GONE);
		btnRefresh.setVisibility(View.GONE);
		
		
		luAdapter=new LuAdapter<BalanceBean>(this, manager.getBalanceBeans(),R.layout.item_listview) {
			@Override
			public void convert(ViewHolder helper, BalanceBean item) {
				// TODO Auto-generated method stub
//				super.convert(helper, item);
				helper.setString(R.id.tvMumber, ""+item.getNumber());
				helper.setString(R.id.tvBank, ""+item.getBank());
				helper.setString(R.id.tvCarNumber, ""+item.getCardNumber());
				helper.setString(R.id.tvDate, ""+item.getDate());
			}
		};
		listView.setAdapter(luAdapter);
	}
}
