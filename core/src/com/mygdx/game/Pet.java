package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.util.Timer;

public class Pet extends Actor
{
	private float petX;
	private float petY;
	
	//will probably eventually be added to constructor for different types of pets and shit
	private float petSpeed = 100.0f;
	private boolean canMove = false;
	private boolean direction = false;
	private Random rand;
	
	private Timer startMovementTimer;
	private Timer stopMovementTimer;
	private int secondsUntilMovement = 0;
	private int secondsBeforeStop = 2;
	
	Texture petTexture;
	
	public Pet(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight)
	{
		super();
		
		petX = 320;
		petY = 60;
		
		petTexture = texture;
		
		//Min + (int)(Math.random() * ((Max - Min) + 1))
		rand = new Random();
		
		startMovementTimer = new Timer(System.currentTimeMillis(), false);
		stopMovementTimer = new Timer(System.currentTimeMillis(), false);
	}
	
	public float getX()
	{
		return petX;
	}
	
	public float getY()
	{
		return petY;
	}
	
	//happens every 3 - 7 seconds in handleTimers
	//direction is also determined in handleTimers every 3-7 seconds
	//after movement begins, movement stops after 2 seconds, set in handleTimers
	public void move()
	{
		if(canMove)
		{
			if(!direction)
			{
				petX -= Gdx.graphics.getDeltaTime() * petSpeed;
				fixBounds();
			}
			else if(direction)
			{
				petX += Gdx.graphics.getDeltaTime() * petSpeed;
				fixBounds();
			}
		}
	}
	
	private void fixBounds()
	{
		if(petX < (0 - (this.getWidth())))
		{
			petX = (0 - this.getWidth());
		}
		else if(petX > (Gdx.graphics.getWidth() - (this.getWidth())))
		{
			petX = (Gdx.graphics.getWidth() - (this.getWidth()));
		}
	}
	
	//if canMove is false, program waits 3-7 seconds, then changes to true and starts moving character
	//after canMove is set to true, another timer starts. after 2 seconds, make canMove false. repeat
	public void handleTimers()
	{
		//if pet cannot move yet, determine time before it can move, then move it
		if(!canMove)
		{
			//this is set to 0 at init and directly after the pet moves
			if(secondsUntilMovement == 0)
			{
				//Min + (int)(Math.random() * ((Max - Min) + 1))
				//get a number between 3 and 5
				secondsUntilMovement = 3 + (int)(Math.random() * ((5 - 3) + 1));
			}
			
			//let the clock tick
			startMovementTimer.tick();
			
			//if the timer has added up enough seconds to equal secondsUntilMovement, start over
			if(startMovementTimer.seconds >= secondsUntilMovement)
			{
				canMove = true;
				
				startMovementTimer.seconds = 0;
				secondsUntilMovement = 0; 
				
				//determine direction of pet
				int  n = rand.nextInt(50) + 1;
				if(n < 25)
					direction = false;
				else
					direction = true;
			}
		}
		
		//if pet can move, stop movement after 2 seconds (int secondsBeforeStop)
		else if(canMove)
		{
			stopMovementTimer.tick();
			
			if(stopMovementTimer.seconds >= secondsBeforeStop)
			{
				stopMovementTimer.seconds = 0;
				canMove = false;
			}
		}
	}
	
	@Override
    public void draw(Batch batch, float alpha)
	{
        batch.draw(petTexture, this.getX(), this.getY());
    }
}
