package com.mygdx.game.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver
{
      
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	String notificationMessage = intent.getStringExtra("notification_string_message");
    	
    	Intent service1 = new Intent(context, NotificationService.class);
    	
    	service1.putExtra("notification_string_message", notificationMessage);
    	
    	context.startService(service1);
    }   
}
