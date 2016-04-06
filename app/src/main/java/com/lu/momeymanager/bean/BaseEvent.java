package com.lu.momeymanager.bean;


import com.lu.momeymanager.util.Constant;

public class BaseEvent implements Constant{
	private Object data;
	public final static int CHANGE_FRAGMENT=0;
	public final static int UPDATE_MAIN=1;
	public final static int UPDATE_BALANCE=2;

	private int eventType;
	public Object getData() {
		return data;
	}

	public BaseEvent(int eventType,Object data) {
		super();
		this.setEventType(eventType);
		this.data = data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
}
