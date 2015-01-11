package com.mygdx.game.desktop;

import net.sf.jcarrierpigeon.WindowPosition;
import net.sf.jtelegraph.Telegraph;
import net.sf.jtelegraph.TelegraphQueue;
import net.sf.jtelegraph.TelegraphType;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.util.Notification;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Test-Game";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MyGdxGame(new DesktopNotification()), config);
	}
}

class DesktopNotification implements Notification
{
	Telegraph telegraph;
	TelegraphQueue queue;
	
	public DesktopNotification()
	{
	
	}

	public void deployNotification(int s, int m, int h, int d, String msg, MyGdxGame game)
	{
		/**
		//THE DESKTOP VERSION MAY NOT GET TO HAVE NOTIFICATIONS (AT LEAST NOT WHEN PROGRAM ISN'T RUNNING)
		telegraph = new Telegraph("Pet Notification", msg,
				TelegraphType.NOTIFICATION_DONE, WindowPosition.BOTTOMLEFT, 4000);
		queue = new TelegraphQueue();
		queue.add(telegraph);
		**/
	}
	
	public void clearNotifications()
	{
		
	}
}