package com.lu.momeymanager.bean;

public class BalanceBean {
	private String cardNumber;
	private String bank;
	private double  number;
	private String date;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public BalanceBean(String cardNumber, String bank, double number) {
		super();
		this.cardNumber = cardNumber;
		this.bank = bank;
		this.number = number;
	}
	public BalanceBean(String cardNumber, String bank, double number,String date) {
		this(cardNumber,bank,number);
		this.date=date;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		BalanceBean balanceBean=(BalanceBean) o;
		return this.cardNumber.equals(balanceBean.cardNumber);
	}
}
