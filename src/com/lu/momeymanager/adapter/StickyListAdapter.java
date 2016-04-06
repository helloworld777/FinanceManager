package com.lu.momeymanager.view.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lu.momeymanager.R;
import com.lu.momeymanager.bean.InOutBean;
import com.lu.momeymanager.view.widget.stickylis.StickyListHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

public class StickyListAdapter extends LuAdapter<InOutBean>implements StickyListHeadersAdapter, SectionIndexer{
	private String[] mSectionLetters;
	private int[] mSectionIndices;
	public StickyListAdapter(Context context, List<InOutBean> datas, int mItemLayoutId) {
		super(context, datas, mItemLayoutId);
		// TODO Auto-generated constructor stub
		
		mSectionLetters=new String[datas.size()];
		for(int i=0;i<mSectionLetters.length;i++){
			mSectionLetters[i]=datas.get(i).getDate();
		}
		getSectionIndices();
	}
	@Override
	public void convert(ViewHolder helper, InOutBean item) {
		helper.setString(R.id.tvMumber, item.getNumberText());
		helper.setString(R.id.tvDate, String.valueOf(item.getDate()));
		helper.setString(R.id.tvBank, item.getBank());
	}
	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		 if (mSectionIndices.length == 0) {
	            return 0;
	        }
	        
	        if (section >= mSectionIndices.length) {
	            section = mSectionIndices.length - 1;
	        } else if (section < 0) {
	            section = 0;
	        }
	        return mSectionIndices[section];
	}
	 private int[] getSectionIndices() {
	        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
	        String lastFirstChar = datas.get(0).getDate();
	        sectionIndices.add(0);
	        for (int i = 1; i < datas.size(); i++) {
	            if (datas.get(i).getDate().equals(lastFirstChar) ) {
	                lastFirstChar = datas.get(i).getDate();
	                sectionIndices.add(i);
	            }
	        }
	        int[] sections = new int[sectionIndices.size()];
	        for (int i = 0; i < sectionIndices.size(); i++) {
	            sections[i] = sectionIndices.get(i);
	        }
	        return sections;
	    }
	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		 for (int i = 0; i < mSectionIndices.length; i++) {
	            if (position < mSectionIndices[i]) {
	                return i - 1;
	            }
	        }
	        return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mSectionLetters;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HeaderViewHolder holder;
		 if (convertView == null) {
	            holder = new HeaderViewHolder();
	            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_header, parent, false);
	            holder.text = (TextView) convertView.findViewById(R.id.text);
	            convertView.setTag(holder);
	        } else {
	            holder = (HeaderViewHolder) convertView.getTag();
	        }

	        // set header text as first char in name
	        CharSequence headerChar = datas.get(position).getDate();
	        holder.text.setText(headerChar);
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub
		return mSectionIndices[position];
	}
	  class HeaderViewHolder {
	        TextView text;
	    }

}
