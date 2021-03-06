package com.lu.momeymanager.view.activity.angle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.lu.momeymanager.util.ScreenUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Main Activity
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleActivity extends Activity
{
	public AngleSurfaceView mGLSurfaceView; // The main GL View
	public XmlPullParser xmlParser;
	protected AngleUI mCurrentUI = null;
	protected int screeW,screenH;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		screeW= ScreenUtil.getScreenSize(this)[0];
		screenH= ScreenUtil.getScreenSize(this)[1];
		try
		{
			Thread.sleep(100);
			mGLSurfaceView = new AngleSurfaceView(this);
			mGLSurfaceView.setAwake(true);
			mGLSurfaceView.start();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Set the current user interface
	 * 
	 * @param currentUI
	 */
	public void setUI(AngleUI currentUI)
	{
		if (mCurrentUI != currentUI)
		{
			if (mCurrentUI != null)
			{
				mCurrentUI.onDeactivate();
				mGLSurfaceView.removeObject(mCurrentUI);
			}
			mCurrentUI = currentUI;
			if (mCurrentUI != null)
			{
				mCurrentUI.onActivate();
				mGLSurfaceView.addObject(mCurrentUI);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mCurrentUI != null)
			if (mCurrentUI.onTouchEvent(event))
				return true;
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (mCurrentUI != null)
			if (mCurrentUI.onTrackballEvent(event))
				return true;
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mCurrentUI != null)
			if (mCurrentUI.onKeyDown(keyCode, event))
				return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mGLSurfaceView.onPause();
		if (mCurrentUI != null)
			mCurrentUI.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mGLSurfaceView.onResume();
		if (mCurrentUI != null)
			mCurrentUI.onResume();
	}

	@Override
	public void finish()
	{
		mGLSurfaceView.delete();
		super.finish();
	}

	public boolean executeXML(int resId)
	{
		xmlParser = getResources().getXml(resId);
		Log.d("XML", "executeXML " + resId);
		return nextXMLCommand();
	}

	public boolean nextXMLCommand()
	{
		try
		{
			xmlParser.next();
			while ( ((xmlParser.getEventType() != XmlPullParser.START_TAG)|| (xmlParser.getDepth() != 2)) && (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) )
				xmlParser.next();// skip comments
			if (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT)
			{
				Log.d("XML", "nextXMLCommand");
				executeXMLCommand(xmlParser.getName().toLowerCase());
				return true;
			}
			else
				executeXMLCommand(null);
		}
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	protected void executeXMLCommand(String command)
	{

	}

}
