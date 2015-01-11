package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class Scoop extends Actor
{
	//moves across the screen when clean button is pressed
	Texture scoopTexture;
	public MoveToAction move;
	
	public Scoop()
	{
		scoopTexture = new Texture("scoop.png");
		this.setPosition(800,10);
		this.setVisible(false);
	}
	
	public void move()
	{
		Gdx.app.log("DEBUG", "ACTION BEING ADDED");
		move = new MoveToAction();
		move.setPosition(-110, 10);
		move.setDuration(1.0f);
		this.addAction(move);
	}
	
	@Override
    public void draw(Batch batch, float alpha)
	{
        batch.draw(scoopTexture, this.getX(), this.getY());
    }
}
