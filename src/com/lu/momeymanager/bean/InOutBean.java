package com.lu.momeymanager.bean;

public class InOutBean {
	private double number;
	private String note;
	private String date;
	private String numberText;
	private String bank;
	private String cardNumber;
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public InOutBean(String numberText, String note,String date, String bank,double number,String cardNumber) {
		super();
		this.numberText = numberText;
		this.note = note;
		this.date=date;
		this.number=number;
		this.setBank(bank);
		this.setCardNumber(cardNumber);
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNumberText() {
		return numberText;
	}
	public void setNumberText(String numberText) {
		this.numberText = numberText;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
}
