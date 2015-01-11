package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.screen.GameInitScreen;
import com.mygdx.screen.GameScreen;
import com.mygdx.screen.SleepScreen;
import com.mygdx.screen.StatsScreen;
import com.mygdx.util.Notification;
import com.mygdx.util.Timer;

public class MyGdxGame extends Game 
{
	//main game stuff
	public Timer mainTimer;	//time overall, resets when pet dies (not when a new one is born)
	public GameScreen gameScreen;
	public StatsScreen statsScreen;
	public GameInitScreen gameInitScreen;
	public SleepScreen sleepScreen;
	public SpriteBatch batch;
	public Notification notification;
	public AttributesManager attributesManager;
	
	//save game
	Preferences saveGameData;
	public long currentNotificationID;
	
	//why, its offical save file stuff of course
	//will probably ahve a date in here eventually u kno wut im sayin
	String petName;
	String ownerName;
	
	//stats
	int timesPoopCleaned;
	int timesPetFed;
	int timesPetPlay;
	int timesPetSleep;
	int timesPetSleepTotalAmount;
	
	/*
	 * 0 - splash screen
	 * 1 - main menu
	 * 2 - game screen
	 * 3 - stats (timer) screen
	 * 4 - gameInitScreen 
	 * 5 - sleep screen
	 */
	int currentScreen = 0;
	
	int STATE_CURRENT;
	int STATE_CREATE = 0;
	int STATE_RENDER = 1;
	int STATE_RESUME = 2;
	int STATE_PAUSE = 3;
	int STATE_DISPOSE = 4;
	
	public MyGdxGame(Notification n)
	{
		notification = n;
	}
	
	@Override
	public void create()
	{
		STATE_CURRENT = STATE_CREATE;
		
		Gdx.app.log("DEBUG", "MAIN GAME create METHOD CALLED");
		mainTimer = new Timer(System.currentTimeMillis(), true);
		
		gameScreen = new GameScreen(this);
		statsScreen = new StatsScreen(this);
		gameInitScreen = new GameInitScreen(this);
		sleepScreen = new SleepScreen(this);
		
		batch = new SpriteBatch();
		
		saveGameData = Gdx.app.getPreferences("mygdxgame_save_game");
		
		//manage attributes here
		attributesManager = new AttributesManager(this);
		
		if(checkSaveGame())
		{
			this.handleScreens(2);
		}
		else
		{
			this.handleScreens(4);
		}
	}
	
	@Override
	public void resize(int width, int height) 
	{
		super.resize(width, height);
	}
	
	@Override
	public void render()
	{
		STATE_CURRENT = STATE_RENDER;
		super.render();
	}
	
	@Override
	public void resume()
	{
		Gdx.app.log("DEBUG", "MAIN GAME resume METHOD CALLED");
		STATE_CURRENT = STATE_RESUME;
		
		//on android, the pause method will stop the timer from running. on desktop it does not
		//have not checked ios yet
		if(Gdx.app.getType() == ApplicationType.Android)
		{
			checkSaveGame();
			
			if(this.getScreen() == gameScreen)
			{
				gameScreen.getGameScreenData();
			}
		}
		
		//PENDING NOTIFICATIONS NEED TO BE CLEARED HERE
		notification.clearNotifications();
	}
	
	@Override
	public void pause()
	{
		Gdx.app.log("DEBUG", "MAIN GAME pause METHOD CALLED");
		STATE_CURRENT = STATE_PAUSE;
		//saveCurrentGameData();
		
		//it kinda looks like on desktop version, pause and dispose are called. 
		//on android version, just pause is called
		if(this.getScreen() == gameScreen)
		{
			gameScreen.saveGameScreenData();
		}
		
		//this stuff used to be in dispose, but desktop version calls pause then dispose
		//android just calls pause.
		saveCurrentGameData(true);
	}
	
	@Override
	public void dispose()
	{	
		Gdx.app.log("DEBUG", "MAIN GAME dispose METHOD CALLED");
		STATE_CURRENT = STATE_DISPOSE;
		
		//disposes of the textures inside the manager
		attributesManager.dispose();
		batch.dispose();
	}
	
	public void handleScreens(int scene)
	{
		Gdx.app.log("DEBUG", "MAIN GAME handleScreens METHOD CALLED");
		
		saveCurrentGameData(false);
		
		switch(scene)
		{
			case 0:
				//splash screen
				break;
			case 1:
				//main menu
				break;
			case 2:
				//game screen
				//checkSaveGame();
				this.setScreen(gameScreen);
				break; 
			case 3:
				//stats screen
				//checkSaveGame();
				this.setScreen(statsScreen);
				break;
			case 4:
				//game setup screen
				//this probably won't happen
				this.setScreen(gameInitScreen);
				break;
			case 5:
				//sleep screen
				this.setScreen(sleepScreen);
				break;
		}
		
	}
	
	public boolean checkSaveGame()
	{
		boolean saveGameExists = saveGameData.getBoolean("saveGameExists");
		Gdx.app.log("DEBUG", "CHECKING SAVE GAME");
		
		if(!saveGameExists)
		{
			return false;
		}
		else
		{
			petName = saveGameData.getString("petName");
			ownerName = saveGameData.getString("ownerName");
			
			if(getSaveData().getBoolean("IDExists"))
			{
				currentNotificationID = getSaveData().getLong("currentID");
			}
			else
			{
				currentNotificationID = 0;
				getSaveData().putLong("currentID", currentNotificationID);
				getSaveData().putBoolean("IDExists", true);
			}
			
			if(saveGameData.getBoolean("timeSaved") == true)
			{
				mainTimer.seconds = saveGameData.getInteger("seconds");
				mainTimer.minutes = saveGameData.getInteger("minutes");
				mainTimer.hours = saveGameData.getInteger("hours");
				mainTimer.days = saveGameData.getInteger("days");
				
				long timeOnExit = this.saveGameData.getLong("timeOnExit");
				long currentTime = System.currentTimeMillis();
				
				long elapsedTime = currentTime - timeOnExit;
				
				//this will be set each time the game is loaded, so that the game can 
				//deal with values that should have decreased over the time the app was closed
				attributesManager.elapsedTime = elapsedTime;
				
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
				
				Gdx.app.log("DEBUG", "current time as of exit: " + mainTimer.days + "days, " + mainTimer.hours + "hours, "
						+ mainTimer.minutes + "minutes, " + mainTimer.seconds + "seconds.");
				Gdx.app.log("DEBUG", "Elapsed Time Since Exit: " + day + "days, " + hour + "hours, " + minute + "minutes, " + second + "seconds");
				
				mainTimer.seconds += second;
				if(mainTimer.seconds >= 60)
				{
					mainTimer.seconds -= 60;
					mainTimer.minutes += 1;
				}
				mainTimer.minutes += minute;
				if(mainTimer.minutes >= 60)
				{
					mainTimer.minutes -= 60;
					mainTimer.hours += 1;
				}
				mainTimer.hours += hour;
				if(mainTimer.hours >= 24)
				{
					mainTimer.hours -= 24;
					mainTimer.days += 1;
				}
				mainTimer.days += day;
				
				Gdx.app.log("time after adding them togeter", mainTimer.days + "days, " + mainTimer.hours + "hours, "
						+ mainTimer.minutes + "minutes, " + mainTimer.seconds + "seconds.");
			}
			
			return true;
		}
	}
	
	public void saveCurrentGameData(boolean prepareNotifications)
	{
		//THIS SHOULD BE CALLED EVERY TIME GAME IS EXITED, NO MATTER WHERE FROM. CHECK WHEN NOT HIGH
		
		//if timer has not yet been saved before, indicate that it has now been saved
		Gdx.app.log("DEBUG", "THE VALUE OF timeSaved IS" + saveGameData.getBoolean("timeSaved"));
		
		if(saveGameData.getBoolean("timeSaved") == false)
		{
			saveGameData.putBoolean("timeSaved", true);
		}
		else if(prepareNotifications)
		{
			//keeps notifications from going off during setup when opening the keyboard pauses the game
			if(this.getScreen() != gameInitScreen)
				attributesManager.prepareNotifications();
		}
		
		saveGameData.putLong("currentID", currentNotificationID);
		
		saveGameData.putInteger("seconds", mainTimer.seconds);
		saveGameData.putInteger("minutes", mainTimer.minutes);
		saveGameData.putInteger("hours", mainTimer.hours);
		saveGameData.putInteger("days", mainTimer.days);
		
		//when the game is exited (the only time the timer runs is when the game is running
		//save the time since epoch. then when the game starts back up, subtract this epoch
		//time from the current, then add those values to the mainTimer (in seconds, minutes, hours, days)
		saveGameData.putLong("timeOnExit", System.currentTimeMillis());
		
		//DONT FORGET THIS THIS ACTUALLY SAVES THE DATA
		saveGameData.flush();
	}
	
	public SpriteBatch getBatch()
	{
		return batch;
	}
	
	public void setNames(String pN, String oN)
	{
		petName = pN;
		ownerName = oN;
	}
	public String getPetName()
	{
		return petName;
	}
	public String getOwnerName()
	{
		return ownerName;
	}
	public Preferences getSaveData()
	{
		return saveGameData;
	}
}
