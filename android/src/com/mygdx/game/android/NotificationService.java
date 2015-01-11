package com.mygdx.game.android;

import java.io.File;

import com.badlogic.gdx.Gdx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class NotificationService extends Service 
{
      
    private NotificationManager mManager;
    private String notificationMessage;
    private long notificationFireTimeInMillis;
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
    }
 
   @SuppressWarnings({ "static-access", "deprecation" })
   @Override
   public void onStart(Intent intent, int startId)
   {
       super.onStart(intent, startId);
       
       notificationMessage = intent.getStringExtra("notification_string_message");
       Gdx.app.log("DEBUG", notificationMessage);
      
       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
       Intent intent1 = new Intent(this.getApplicationContext(),AndroidLauncher.class);
     
       android.app.Notification notification = new android.app.Notification(R.drawable.ic_launcher,
    		   notificationMessage, System.currentTimeMillis());
       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, 
    		   intent1,PendingIntent.FLAG_CANCEL_CURRENT);
       notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
       notification.setLatestEventInfo(this.getApplicationContext(), "Pet Notification",
    		   notificationMessage, pendingNotificationIntent);
       
       try {
    	    Uri notificationSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sms);
    	    //Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificationSound);
    	    r.play();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
       
       mManager.notify(0, notification);
    }
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
