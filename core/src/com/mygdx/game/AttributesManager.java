package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AttributesManager 
{
	//attribute data
	Texture poopTexture;
	Texture hungerProgressBarTexture;
	Texture sleepProgressBarTexture;
		
	Attributes hungerAttribute;
	Attributes sleepAttribute;
	Attributes poopAttribute;
	ArrayList<Sprite> poopArrayList;
	
	//is used to set attributes after loading game after a long period of time
	public long elapsedTime;
	
	MyGdxGame game;
	
	//as change Rate goes up, the rate of change in attributes goes up as well (for increase XOR decrease)
	float hungerChangeRate;
	float sleepChangeRate;
	float timeUntilHungerChange; //is affected by ChangeRate to get ActualTimeUntilChange
	float timeUntilSleepChange;
	int hungerAccumulationValue; //number actually added to or subtracted from attribute values
	int sleepAccumulationValue;
	
	public AttributesManager(MyGdxGame gam)
	{
		this.game = gam;
		
		//if hunger changes every 4 minutes, and accumulation value is 2, then it takes 4.8 hours to reduce hunger completely
		timeUntilHungerChange = 4;
		hungerChangeRate = 0.00f;
		hungerAccumulationValue = 2;
		
		//if sleep changes every 3 minutes, and accumulation value is 1, then it takes 7.25 hours to completely drain sleep
		timeUntilSleepChange = 3; 
		sleepChangeRate = 0.00f;
		sleepAccumulationValue = 1;
		
		poopTexture = new Texture("poop.png");
		hungerProgressBarTexture = new Texture("progressbarHealth.png");
		sleepProgressBarTexture = new Texture("progressbarSleep.png");
		
		poopArrayList = new ArrayList<Sprite>();
		
		//set up accumulating values
		hungerAttribute = new Attributes(hungerProgressBarTexture, "hunger");
		sleepAttribute = new Attributes(sleepProgressBarTexture, "sleep");
		poopAttribute = new Attributes(poopTexture, "poop");
		
		//start with initial decay rates (these are always in minutes)
		int finalTimeUntilHungerChange = (int) (timeUntilHungerChange - (hungerChangeRate * timeUntilHungerChange));
		int finalTimeUntilSleepChange = (int) (timeUntilSleepChange - (sleepChangeRate * timeUntilSleepChange));
		
		Gdx.app.log("DEBUG", "FINAL TIME UNTIL HUNGER CHANGE: " + finalTimeUntilHungerChange);
		Gdx.app.log("DEBUG", "FINAL TIME UNTIL SLEEP CHANGE: " + finalTimeUntilSleepChange);
		
		hungerAttribute.setAccumulationTime(finalTimeUntilHungerChange);
		sleepAttribute.setAccumulationTime(finalTimeUntilSleepChange);
		poopAttribute.setAccumulationTime(10);
	}
	
	public void accumulate(int petX, int petY)
	{
		//this is where we will handle changing decay rates depending on the state of the attributes
		//accumulateAttribute takes true or false to add or remove
		//I guess technically it can be called twice, once true, once false, may have to add second decay rate var
		//setAccumulationTime sets how long until the next stat is added or removed. 
		//so, this will be changed often
		//need to change accumulation values from 0-4 to 0-99. Split 0-99 into four-five sections for stats
		//and this will make it easier to fine tune the decay rates. 
		
		
		//handle pet attributes, false means 1 hunger is removed every 10 minutes
		hungerAttribute.accumulateAttribute(false, hungerAccumulationValue);
		//handle pet attributes, false means 1 sleep is removed every 10 minutes
		//eventually this false will be a boolean variable depending on whether pet is sleeping or not sleeping
		sleepAttribute.accumulateAttribute(false, sleepAccumulationValue);
		//set to true, poop is always building up. eating will eventually speed up the process
		//if the accumulateAttribute method returns true, that means attribute was just changed
		if(poopAttribute.accumulateAttribute(true, 1))
		{
			//no more than 10 poops on the screen at a time plz
			if(poopArrayList.size() < 10)
			{
				Sprite s = new Sprite(poopTexture);
				s.setX(petX - 30);
				s.setY(petY - 20);
				
				//just making sure the pooops don't spawn too close to each other here
				for(int i = 0; i < poopArrayList.size(); i++)
				{
					int distance = (int) (s.getX() - poopArrayList.get(i).getX());
					boolean moveLeft = false;
					boolean moveRight = false;
					
					//poop width is 42
					
					if(distance < 0)
					{
						distance *= (-1);
						moveLeft = true;
					}
					else
					{
						moveRight = true;
					}
					
					if(distance < 20)
					{
						if(moveLeft)
							s.setX(s.getX() - 15);
						if(moveRight)
							s.setX(s.getX() + 15);
					}

				}
				poopArrayList.add(s);
			}
		}
	}
	
	public void drawAttributes(boolean showStats, SpriteBatch batch)
	{
		//showStats controls whether hunger, sleep, play stats are shown on screen
		if(!showStats)
		{
			batch.draw(sleepAttribute.getFullProgressBarRegion(), 15, 440);
			batch.draw(hungerAttribute.getFullProgressBarRegion(), 15, 405);
		}
		
		//draw all the poops currently on the screen
		for(int i = 0; i < poopArrayList.size(); i++)
		{
			batch.draw(poopArrayList.get(i), poopArrayList.get(i).getX(), poopArrayList.get(i).getY());
		}
		
	}
	
	public void cleanPoop()
	{
		poopAttribute.setValue(0);
		poopArrayList.clear();
	}
	public void feedPet()
	{
		if(hungerAttribute.addToAttribute(35))
		{
			//play animation
		}
		else
		{
			//do not
		}
	}
	
	public void prepareNotifications()
	{
		Gdx.app.log("DEBUG", "preparing notifications!");
		
		int attributeNearFourthPercentile = 36;		//this is where the 4th section of the attribute bar is
		int attributeNearZero = 10;			//user will be notified when attribute is close to zero
		
		//current attribute values
		Gdx.app.log("DEBUG", "Hunger value: " + hungerAttribute.getValue());
		Gdx.app.log("DEBUG", "Sleep value: " + sleepAttribute.getValue());
		
		Attributes attributesArray[] = new Attributes[2];
		attributesArray[0] = hungerAttribute;
		attributesArray[1] = sleepAttribute;
		
		for(int i = 0; i < attributesArray.length; i++)
		{
			int valueToGoUntilFourthPercentile = attributesArray[i].getValue() - attributeNearFourthPercentile;
			int valueToGoUntilNearZero = attributesArray[i].getValue() - attributeNearZero;
			Gdx.app.log("DEBUG", attributesArray[i].getName() + 
					" value to go until fourth percentile: " + valueToGoUntilFourthPercentile);
			Gdx.app.log("DEBUG", attributesArray[i].getName() +
					" value to go until near zero: " + valueToGoUntilNearZero);
			
			
			valueToGoUntilFourthPercentile /= 3;
			valueToGoUntilNearZero /= 3;
			Gdx.app.log("DEBUG", "Number of times " + attributesArray[i].getName() + "will need to be taken away" +
					"before the 4th percentile: " + valueToGoUntilFourthPercentile);
			Gdx.app.log("DEBUG", "Number of times " + attributesArray[i].getName() + "will need to be taken away" +
					"before near zero: " + valueToGoUntilNearZero);
			
			float timeUntilChange = 0;
			float changeRate = 0;
			
			if(attributesArray[i].getName() == "hunger")
			{
				timeUntilChange = timeUntilHungerChange;
				changeRate = hungerChangeRate;
			}
			else if(attributesArray[i].getName() == "sleep")
			{
				timeUntilChange = timeUntilSleepChange;
				changeRate = sleepChangeRate;
			}
			
			int minutesToGoUntilFourthPercentile = (int) (valueToGoUntilFourthPercentile * (timeUntilChange 
					- (changeRate * timeUntilChange)));
			int minutesToGoUntilNearZero = (int) (valueToGoUntilNearZero * (timeUntilChange 
					- (changeRate * timeUntilChange)));
			Gdx.app.log("DEBUG", "minutes until " + attributesArray[i].getName() + 
					" fourth percentile notification should hit: " + minutesToGoUntilFourthPercentile);
			Gdx.app.log("DEBUG", "minutes until " + attributesArray[i].getName() +
					" near zero notification should hit: " + minutesToGoUntilNearZero);
			
			//for now will just use this check. if minutesToGo is 0, that means that the pet is already at 
			//36 or below, so no notifications will be prepared.
			if(minutesToGoUntilFourthPercentile > 0)
			{
				Gdx.app.log("DEBUG", attributesArray[i].getName() 
						+ " notification to be deployed in " + minutesToGoUntilFourthPercentile);
				String message = "";
				
				if(attributesArray[i].getName() == "hunger")
					message = this.game.ownerName + "! " + this.game.petName + " is hungry. Come feed it.";
				else if(attributesArray[i].getName() == "sleep")
					message = this.game.ownerName + "! " + this.game.petName + " is very sleepy. Come tell it to sleep.";
				
				this.game.notification.deployNotification(0, minutesToGoUntilFourthPercentile, 0, 0, message, this.game);
			}
			if(minutesToGoUntilNearZero > 0)
			{
				Gdx.app.log("DEBUG", attributesArray[i].getName()
						+ " notification to be deployed in " + minutesToGoUntilNearZero);
				String message = "";
				
				if(attributesArray[i].getName() == "hunger")
					message = this.game.ownerName + "! " + this.game.petName + " is extremely hungry. Come feed it or it might not make it.";
				else if(attributesArray[i].getName() == "sleep")
					message = this.game.ownerName + "! " + this.game.petName + " is very sleepy. Come tell it to sleep or it might not make it.";
				
				this.game.notification.deployNotification(0, minutesToGoUntilNearZero, 0, 0, message, this.game);
			}
		}
	}
	
	public void saveAttributeData(MyGdxGame game)
	{	
		Gdx.app.log("DEBUG", "SAVING POOP VALUE AS: " + poopArrayList.size());
		
		game.getSaveData().putBoolean("gameScreenSaveDataExists", true);
		game.getSaveData().putInteger("poop_on_screen", poopArrayList.size());
		
		game.getSaveData().putInteger("hunger_value", hungerAttribute.getValue());
		game.getSaveData().putInteger("sleep_value", sleepAttribute.getValue());
		game.getSaveData().putFloat("hunger_change_rate", hungerChangeRate);
		game.getSaveData().putFloat("sleep_change_rate", sleepChangeRate);
		game.getSaveData().putInteger("hunger_accumulation_value", hungerAccumulationValue);
		game.getSaveData().putInteger("sleep_accumulation_value", sleepAccumulationValue);
		game.getSaveData().putFloat("time_until_hunger_change", timeUntilHungerChange);
		game.getSaveData().putFloat("time_until_sleep_change", timeUntilSleepChange);
		
		//for each poop on the screen, this will create or update a numbered poop x and y
		//coord in the save data file. will be retrieved when app is restarted
		for(int i = 0; i < poopArrayList.size(); i++)
		{
			game.getSaveData().putInteger(("poopX" + i), (int)poopArrayList.get(i).getX());
			game.getSaveData().putInteger(("poopY" + i), (int)poopArrayList.get(i).getY());
		}
		
		game.getSaveData().flush();
	}
	
	//THIS IS CALLED IN GAMESCREEN'S getGameScreenData() METHOD, WHICH IS CALLED IN GAMESCREEN'S show() METHOD
	//will need to call this is sleepscreens show method
	public void getAttributeData(MyGdxGame game)
	{
		Gdx.app.log("DEBUG", "RETREIVING POOP VALUE AS: " + game.getSaveData().getInteger("poop_on_screen"));
		
		poopAttribute.setValue(game.getSaveData().getInteger("poop_on_screen"));
		hungerAttribute.setValue(game.getSaveData().getInteger("hunger_value"));
		sleepAttribute.setValue(game.getSaveData().getInteger("sleep_value"));
		
		hungerChangeRate = game.getSaveData().getFloat("hunger_change_rate");
		sleepChangeRate = game.getSaveData().getFloat("sleep_change_rate");
		hungerAccumulationValue = game.getSaveData().getInteger("hunger_accumulation_value");
		sleepAccumulationValue = game.getSaveData().getInteger("sleep_accumulation_value");
		timeUntilHungerChange = game.getSaveData().getFloat("time_until_hunger_change");
		timeUntilSleepChange = game.getSaveData().getFloat("time_until_sleep_change");
		
		int poop_on_screen = game.getSaveData().getInteger("poop_on_screen");
		Sprite s;
		for(int i = 0; i < poop_on_screen; i++)
		{
			int x = game.getSaveData().getInteger("poopX"+i);
			int y = game.getSaveData().getInteger("poopY"+i);
			
			s = new Sprite(poopTexture);
			s.setX(x);
			s.setY(y);
			poopArrayList.add(s);
		}
		
		if(elapsedTime > 0)
		{
			//use elapsedTime set in game.checkSaveGame which sets the correct time after time has elapsed outside of game
			long second, minute, hour, day;
			long x;
			x = elapsedTime / 1000;
			second = x % 60;
			x /= 60;
			minute = x % 60;
			x /= 60;
			hour = x % 24;
			x /= 24;
			day = x;
			
			//throw out seconds, convert hours, days to minutes
			minute += (hour * 60);
			minute += ((day * 24) * 60);
			
			Gdx.app.log("DEBUG",  "elapsed minutes in attributes manager: " + (minute));
			
			Attributes[] attributesArray = new Attributes[2];
			attributesArray[0] = hungerAttribute;
			attributesArray[1] = sleepAttribute;
			
			for(int i = 0; i < attributesArray.length; i++)
			{
				float timeUntilChange = 0;
				float changeRate = 0;
				String attributeName = attributesArray[i].getName();
				
				if(attributeName == "hunger")
				{
					timeUntilChange = timeUntilHungerChange;
					changeRate = hungerChangeRate;
				}
				else if(attributeName == "sleep")
				{
					timeUntilChange = timeUntilSleepChange;
					changeRate = sleepChangeRate;
				}
				
				int actualTimeUntilChange = (int) (timeUntilChange - (changeRate * timeUntilChange));
				Gdx.app.log("DEBUG", "actual time before one " + attributeName + " changes: " + actualTimeUntilChange);
				
				//DIVISION BY ZERO ERROR HERE, not sure if this is the right way to handle right now
				int timesChangedOverTime;
				if(actualTimeUntilChange == 0)
				{
					timesChangedOverTime = 0;
				}
				else
				{
					timesChangedOverTime = (int) (minute / actualTimeUntilChange);
				}
				Gdx.app.log("DEBUG", "times " + attributeName + " will have changed over time: " + timesChangedOverTime);
				
				
				int actualValueLost = timesChangedOverTime * 3;
				Gdx.app.log("DEBUG", "actual amount of " + attributeName + " lost: " + actualValueLost);
				
				Gdx.app.log("DEBUG", "actual value of " + attributeName + "before change: " + attributesArray[i].getValue());
				attributesArray[i].setValue(attributesArray[i].getValue() - actualValueLost);
				
				if(attributesArray[i].getValue() < 0)
				{
					attributesArray[i].setValue(0);
				}
				
				Gdx.app.log("DEBUG", "new " + attributeName + " value = " + attributesArray[i].getValue());
			}
		}
	}
	
	public Attributes getHungerAttribute()
	{
		return hungerAttribute;
	}
	public Attributes getSleepAttribute()
	{
		return sleepAttribute;
	}
	public Attributes getPoopAttribute()
	{
		return poopAttribute;
	}
	public ArrayList<Sprite> getPoopArrayList()
	{
		return poopArrayList;
	}
	
	public void dispose()
	{
		hungerProgressBarTexture.dispose();
		sleepProgressBarTexture.dispose();
		poopTexture.dispose();
	}

}
