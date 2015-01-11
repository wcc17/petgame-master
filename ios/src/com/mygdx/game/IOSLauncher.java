package com.mygdx.game;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSCalendar;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSDateComponents;
import org.robovm.apple.foundation.NSTimeZone;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UILocalNotification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.mygdx.util.Notification;

public class IOSLauncher extends IOSApplication.Delegate 
{
    @Override
    protected IOSApplication createApplication() 
    {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MyGdxGame(new IOSNotification()), config);
    }

    public static void main(String[] argv) 
    {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}

class IOSNotification implements Notification
{
	UILocalNotification notification;
	
	NSDate fireDate;
	NSDateComponents fireDateComponents;
	
	NSDate now;
	NSDateComponents nowDateComponents;
	NSCalendar calender;
	
	public IOSNotification()
	{
	   notification = new UILocalNotification();
	   
	   //now = new NSDate();
	   calender = new NSCalendar("gregorian");
	   nowDateComponents = new NSDateComponents();
	   
	   //fireDate = new NSDate();
	   fireDateComponents = new NSDateComponents();
	}

	public void deployNotification(int s, int m, int h, int d, String msg)
	{
		Gdx.app.log("DEBUG", "time to add: " + h + "h" + m + "m" + s + "s");
		//need current time
		Calendar c = new GregorianCalendar();
		Gdx.app.log("DEBUG", "current time before addition: " + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH)
				+ "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR) + ":" +
				c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		System.out.println(sdf.format(c.getTime()));
		
		int currentSeconds = c.get(Calendar.SECOND);
		int currentMinutes = c.get(Calendar.MINUTE);
		int currentHours = c.get(Calendar.HOUR);
		
		int currentMonth = (c.get(Calendar.MONTH)+1);
		int currentDays = c.get(Calendar.DAY_OF_MONTH);
	
		currentSeconds += s;
		if(currentSeconds >= 60)
		{
			currentSeconds -= 60;
			currentMinutes += 1;
		}
		currentMinutes += m;
		if(currentMinutes >= 60)
		{
			currentMinutes -= 60;
			currentHours += 1;
		}
		currentHours += h;
		if(currentHours >= 24)
		{
			currentHours -= 24;
			currentDays += 1;
		}
		currentDays += d;
		
		Gdx.app.log("DEBUG","current time after addition: " + currentHours + "h" + currentMinutes + "m" + currentSeconds + "s");
		
		//after here, current days is a day of the month, with probably 1 day added to it.
		//we need to check if theres a month overflow when setting the fire date
		
		//THIS IS TO CREATE THE FIRE DATE AFTER GETTING THE CURRENT DATE DATA
		fireDateComponents.setDay(currentDays);
		fireDateComponents.setMonth(currentMonth);
		fireDateComponents.setYear(c.get(c.YEAR));
		fireDateComponents.setHour(currentHours);
		fireDateComponents.setMinute(currentMinutes);
		fireDateComponents.setSecond(currentSeconds);
		fireDate = calender.newDateFromComponents(fireDateComponents);
		
		Gdx.app.log("DEBUG", "FIREDATE: " + fireDate.toString());
		
		notification.setTimeZone(NSTimeZone.defaultTimeZone());
		notification.setFireDate(fireDate);
		notification.setAlertBody(msg);
		notification.setSoundName("sms.wav");
		notification.setApplicationIconBadgeNumber((long) 1);
		
		Gdx.app.log("DEBUG", "ACTUAL DATE FIRED: " + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH)
				+ "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR) + ":" +
				c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
		
		//[[UIApplication sharedApplication] scheduleLocalNotification:localNotification];
		UIApplication.getSharedApplication().scheduleLocalNotification(notification);
	}
	
	public void clearNotifications()
	{
		
	}

	@Override
	public void deployNotification(int s, int m, int h, int d, String msg,
			MyGdxGame game) {
		// TODO Auto-generated method stub
		
	}
}