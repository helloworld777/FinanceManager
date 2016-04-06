package com.lu.momeymanager.bean;

import java.text.DecimalFormat;
import java.util.List;

public class SimilarDateMoneyBean {
	private String date;
	private List<InOutBean> beans;
	private String bank;
	
	private static final int INCOMING = 0;
	private static final int CONSUME = 1;
	private static final int ALL = 2;

	public static final int TYPE_MOUTH = 0;
	public static final int TYPE_BANK = 1;

	private int sort = TYPE_MOUTH;

	public String getAllIncoming() {
		return new DecimalFormat("#.00").format(count(INCOMING));
	}

	public String getAllConsume() {
		return new DecimalFormat("#.00").format(count(CONSUME));
	}

	public double count(int countType) {
		double i = 0;
		for (InOutBean bean : beans) {

			switch (countType) {
			case INCOMING:
				if (bean.getNumber() > 0) {
					i += bean.getNumber();
				}
				break;
			case CONSUME:
				if (bean.getNumber() < 0) {
					i += bean.getNumber();
				}
				break;
			default:
				i += bean.getNumber();
				break;
			}

		}
		return i;
	}

	public SimilarDateMoneyBean(String date, List<InOutBean> beans) {
		super();
		this.date = date;
		this.beans = beans;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<InOutBean> getBeans() {
		return beans;
	}

	public void setBeans(List<InOutBean> beans) {
		this.beans = beans;
	}

	public boolean isSimilar(InOutBean bean) {
		boolean result = true;
		if (beans.size() < 0) {
			return false;
		}
		switch (sort) {
		case TYPE_MOUTH:
			result = bean.getDate().substring(0, 7).equals(beans.get(0).getDate().substring(0, 7));
			break;
		case TYPE_BANK:
			result = bean.getBank().equals(beans.get(0).getBank());
			break;
		default:
			break;
		}
		return result;
	}

	public double getBalance() {
		return count(ALL);
	}

	public String getCount() {
		return new DecimalFormat("#.00").format(count(ALL));
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
