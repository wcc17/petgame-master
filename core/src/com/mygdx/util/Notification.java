package com.mygdx.util;

import com.mygdx.game.MyGdxGame;

public interface Notification
{
	public void deployNotification(int s, int m, int h, int d, String msg, MyGdxGame game);
	public void clearNotifications();
}
