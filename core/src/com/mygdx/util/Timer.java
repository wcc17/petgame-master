package com.mygdx.util;

import com.badlogic.gdx.Gdx;

public class Timer 
{
	/**
	time
	1 second = 1000 milliseconds
	1 minute = 60 seconds = 60,000 milliseconds
	1 hour = 60 minutes = 3,600,000 milliseconds
	1 day = 24 hours = 86,400,000 milliseconds
	1 week = 7 days = 604,800,000 milliseconds
	when the pet is born, get startingTime = System.epochTime() whatever
	need a variable called elapsedtime, that sees how much time has passed since startTime
	this is the basis for keeping track of pets lifetime
	when hunger or tired is introduced, the monster will have its own hungerStartTime 
	that can change when a monster is fed or whatever
	**/
	
	public int seconds;
	public int minutes;
	public int hours;
	public int days;
	
	public long startTime;
	public long currentTime;
	public long elapsedTime;
	
	boolean printTimeFlag = false;
	
	public Timer(long sT, boolean pTF)
	{
		startTime = sT;
		printTimeFlag = pTF;
	}
	
	public void tick()
	{
		//every frame elapsed time needs to be checked
		//to get elapsed time, subtract birthTime from currentTime
		currentTime = System.currentTimeMillis();
		elapsedTime = currentTime - startTime;
		
		
		if(elapsedTime >= 999)
		{
			startTime = currentTime;
			elapsedTime = 0;
			
			seconds += 1;
			if(seconds == 30)
			{
				if(printTimeFlag) {Gdx.app.log("Time Alert: ", seconds + " second(s) have passed");}
			}
			
			if(seconds >= 60)
			{
				seconds = 0;
				minutes += 1;
				if(printTimeFlag) {Gdx.app.log("Time Alert: ",  minutes + " minute(s) have passed");}
				
				if(minutes >= 60)
				{
					minutes = 0;
					hours += 1;
					if(printTimeFlag) {Gdx.app.log("Time Alert: ", hours + " hour(s) have passed");}
					
					//don't think I'll be getting here any time soon...
					if(hours >= 24)
					{
						hours = 0;
						days += 1;
						if(printTimeFlag){Gdx.app.log("Time Alert: ", days + " day(s) have passed (congratulations btw)");}
					}
				}
			}
		}
	}
	
}
