package com.lu.momeymanager.view.activity.game;


import com.lu.momeymanager.view.activity.angle.AngleSprite;
import com.lu.momeymanager.view.activity.angle.AngleSpriteLayout;

/**
* @author Ivan Pajuelo
*/
public class Scrollable extends AngleSprite
{
	float mFieldX;
	float mFieldZ;
	AngleSprite mField;

	public Scrollable(AngleSprite field, AngleSpriteLayout layout)
	{
		super(layout);
		mField=field;
		mFieldX=0;
		mFieldZ=0;
	}

	public void place()
	{
		float s=(float) (((mFieldZ/mField.roLayout.roHeight)*3)+0.2);
		mPosition.set(mFieldX+mField.mPosition.mX-256,mFieldZ+mField.mPosition.mY-256);
		mScale.set(s,s);
	}

}
