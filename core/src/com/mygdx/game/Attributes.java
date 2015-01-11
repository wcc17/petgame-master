package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.util.Timer;

/**
 * @author Christian Curry
 * Since there is only ever one monster active at the time, 
 * the only attributes that will need to be controlled are its hunger, stamina, etc.
 * Since these don't actually affect anything on the monster (except maybe one day its visual mood)
 * they will be seperate. Attributes is a generic name for something like hunger or stamina that takes a value
 * since these are all controlled by time/something not controlled by the monster (minigames), they are 
 * in a seperate class and are handled in the main render method
 * these give me the oppurtunity to pass the attributes to the monster or whatever if I ever need to without moving code
 */

public class Attributes 
{
	private int value; //empty = 0, full 145
	private int accumulationTime;
	private Timer attributeTimer;
	
	String className;
	
	//private TextureRegion textureRegion;
	private TextureRegion fullProgressBarRegion;

	public Attributes(Texture t, String name)
	{
		value = 40;	//when values are initiated in main game, they should start out empty to give player something to do
		
		className = name;
		
		attributeTimer = new Timer(System.currentTimeMillis(), false);
		
		//all attributes images are 120x30
		//textureRegion = new TextureRegion(t, 0, 0, 120, 30);
		
		fullProgressBarRegion = new TextureRegion(t, 0, 0, 170, 25);
	}
	
	//this will be called, for ex., when eat button is pressed, when play button is pressed
	public boolean addToAttribute(int valueToAdd)
	{
		if(value < 145)
		{
			value += valueToAdd;
			
			if(value > 145)
			{
				value = 145;
			}
			
			Gdx.app.log("DEBUG", this.getName() + " attribute increased by " + valueToAdd + ", is now: " + value);
			return true;
		}
		
		return false;
	}
	
	public boolean removeFromAttribute(int valueToRemove)
	{
		if(value > 0)
		{
			value -= valueToRemove;
			
			if(value < 0)
			{
				value = 0;
			}
			
			Gdx.app.log("DEBUG", this.getName() + " attribute decreased by " + valueToRemove + ", is now: " + value);
			
			return true;
		}
		
		return false;
	}
	
	//this will slowly add sleep, or slowly remove sleep, hunger, entertainment
	public boolean accumulateAttribute(boolean addOrRemove, int valueToAccumulate)
	{
		attributeTimer.tick();
		
		//SHOULD BE MINUTES, SECONDS NOW FOR TESTING
		if(attributeTimer.minutes >= accumulationTime)
		{
			//reset timer
			attributeTimer.minutes = 0;
			
			if(addOrRemove)
				addToAttribute(valueToAccumulate);
			else if(!addOrRemove)
				removeFromAttribute(valueToAccumulate);
			
			return true;
		}
		
		return false;
	}
	
	//takes an int that represents MINUTES!!!
	public void setAccumulationTime(int aT)
	{
		accumulationTime = aT;
	}
	
	public TextureRegion getFullProgressBarRegion()
	{
		//145 is max value
		fullProgressBarRegion.setRegionWidth((int)(value * 1.18f));
		return fullProgressBarRegion;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int v)
	{
		value = v;
	}
	
	public String getName()
	{
		return className;
	}
}
