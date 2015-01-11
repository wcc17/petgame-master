package com.mygdx.game.android;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.util.Notification;

public class AndroidLauncher extends AndroidApplication 
{
	private PendingIntent pendingIntent;
	public MyGdxGame game;
	public long currentId;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		game = new MyGdxGame(new AndroidNotification(this));
	      
		initialize(game, config);
		
		currentId = game.currentNotificationID;
		
		Gdx.app.log("DEBUG", "current notification id: " + currentId);
	}
}

class AndroidNotification implements Notification
{
	AndroidLauncher launcher;
	Calendar calendar;
	Intent myIntent;
	PendingIntent pendingIntent;
	MyGdxGame game;
	public Long currentId;
	
	ArrayList<PendingIntent> pendingIntentArray;
	
	public AndroidNotification(AndroidLauncher a)
	{
		launcher = a;
		game = a.game;
		currentId = a.currentId;
		pendingIntentArray = new ArrayList<PendingIntent>();
	}

	public void deployNotification(int s, int m, int h, int d, String msg, MyGdxGame game)
	{    
		//make sure to let the calendar reset here
		calendar = Calendar.getInstance();
		
		long timeToAddInMillis = (s * 1000) + ((m * 60) * 1000) + (((h * 60) * 60) * 1000) + ((((d * 24) * 60) * 60) * 1000);
		Gdx.app.log("DEBUG", "AndroidLauncher.deployNotification - timeToAddInMillis = " + timeToAddInMillis);
		
        long currentTimeInMillis = calendar.getTimeInMillis();
        Gdx.app.log("DEBUG", "AndroidLauncher.deployNotification - currentTimeInMillis = " + currentTimeInMillis);
        
        long notificationTimeInMillis = timeToAddInMillis + currentTimeInMillis;
        Gdx.app.log("DEBUG", "AndroidLauncher.deployNotification - notificationTimeInMillis = " + notificationTimeInMillis);
     
        myIntent = new Intent(launcher, MyReceiver.class);
        long xId = getNextUniqueId();
        game.currentNotificationID = xId;
        String s_xId = String.valueOf(xId);
        myIntent.putExtra("x_id", s_xId);
        myIntent.setAction(s_xId);
        myIntent.putExtra("notification_string_message", msg);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getBroadcast(launcher, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
     
        AlarmManager alarmManager = (AlarmManager)launcher.getSystemService(Context.ALARM_SERVICE);
        pendingIntentArray.add(pendingIntent); 	//in case it needs to be cancelled
        
        //THIS IS THE MONEY RIGHT HERE. CALL THIS TO SCHEDULE NOTIFICATION
        alarmManager.set(AlarmManager.RTC, notificationTimeInMillis, pendingIntent);
	}
	
	public void clearNotifications()
	{
		launcher.getApplicationContext();
		NotificationManager nM = (NotificationManager) 
				launcher.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		
		nM.cancelAll();
		
		AlarmManager alarmManager = (AlarmManager)launcher.getSystemService(Context.ALARM_SERVICE);
		for(int i = 0; i < pendingIntentArray.size(); i++)
		{
			alarmManager.cancel(pendingIntentArray.get(i));
		}
		
		
	}
	
	public long getNextUniqueId()
	{
		currentId += 1;
		
		return currentId;
	}
}
