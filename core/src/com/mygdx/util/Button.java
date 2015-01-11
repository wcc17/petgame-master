/**
 * 
 * KEEPING FOR A LITTLE WHILE BEFORE I THROW IT OUT
 * 
 * 
 * 
package com.mygdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attributes;

public class Button 
{
	private TextureRegion buttonRegion;
	private Attributes attribute;
	
	//the only coords available to this class is the textureRegion bounds. this is where
	//the texture actually lives on the screen
	private int actualX;
	private int actualY;
	
	public Button(Texture t, int startX, int startY, int width, int height, Attributes a)
	{
		//Texture t has all of the buttons, this region is a specific button
		buttonRegion = new TextureRegion(t, startX, startY, width, height);
		
		attribute = a;
	}
	
	public TextureRegion getButtonRegion()
	{
		return buttonRegion;
	}
	
	public boolean checkForButtonPress()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		
		x *= 800;
		x /= Gdx.graphics.getWidth();
		
		y *= 480;
		y /= Gdx.graphics.getHeight();
		
		//Let's say your screen has width=800, height=600 and your world has width=480 height=320. 
		//Then your new X,Y for your sprite should be:
		//1920x1080
		//int x = Gdx.input.getX()*(800/Gdx.graphics.getWidth());
		//int y = Gdx.input.getY()*(480/Gdx.graphics.getHeight());
		//USING 480 AS VIRTUAL HEIGHT
		
		//Gdx.app.log("area touched: ", x + ", " + y);
		
		if ( (x >= actualX) && (x <= (actualX + buttonRegion.getRegionWidth())) )
		{
			if ( (y >= (480 - (actualY + buttonRegion.getRegionHeight()))) 
					&& (y <= (480 - actualY)) )
			{
				if(attribute != null)
				{
					if(attribute.getValue() < 4)
					{
						Gdx.app.log("Button Press: ", "Attribute increased by 1");
						attribute.addToAttribute();
					}
				}
				else
				{
					//handle what happens when the timer is clicked
					Gdx.app.log("Button Press: ", "Timer button clicked");
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public void setPosition(int x, int y)
	{
		actualX = x;
		actualY = y;
		
	}
	public int getX()
	{
		return actualX;
	}
	public int getY()
	{
		return actualY;
	}

}

**/
