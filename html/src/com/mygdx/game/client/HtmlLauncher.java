package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.util.Notification;

public class HtmlLauncher extends GwtApplication 
{
	@Override
    public GwtApplicationConfiguration getConfig () 
    {
		return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener () 
    {
    	return new MyGdxGame(new HTMLNotification());
    }
}

class HTMLNotification implements Notification
{
	public HTMLNotification()
	{
	   
	}

	public void deployNotification(int s, int m, int h, int d, String msg)
	{
	   
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