package com.example.android_robot_01.bean;

import java.io.Serializable;

public class Result implements Serializable
{
	public static final int TYPE_TEXT=100000;//100000	//文本
	public static final int TYPE_URL=200000;//100000	//文本
	public static final int TYPE_NEW=302000;//100000		//新闻
	public static final int TYPE_RECIPE=308000;//100000	//菜谱

	public static final int TYPE_SONG=313000;	//儿歌
	public static final int TYPE_POME=314000;	//诗歌
//	public int TYPE_TEXT=100000;//100000
	private int code;
	private String text;
	public String url;
	public Result()
	{
	}
	
	public Result(int resultCode, String msg)
	{
		this.code = resultCode;
		this.text = msg;
	}

	public Result(int resultCode)
	{
		this.code = resultCode;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	

}
