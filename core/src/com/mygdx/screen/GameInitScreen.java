package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;

public class GameInitScreen implements Screen
{
	MyGdxGame game;
	SpriteBatch batch;
	
	OrthographicCamera camera;
	Stage stage;
	
	Texture buttonTexture;
	ImageButton startButton;
	
	TextInputListener textFieldListener1;
	TextInputListener textFieldListener2;
	
	String petName;
	String ownerName;
	
	//is set to true when back button is pressed, then checked in handleButtons
	boolean changeScreenEnabled = false;
	boolean showNextDialog = false;
	boolean startButtonEnabled = false;

	public GameInitScreen(MyGdxGame gam) 
	{
		this.game = gam;
	}
	
	@Override
	public void show()
	{
		Gdx.app.log("DEBUG", "GAME INIT SCREEN show METHOD CALLED");
		
		batch = game.getBatch();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new StretchViewport(800, 480, camera), batch);
		Gdx.input.setInputProcessor(stage);
		
		buttonTexture = new Texture("allbuttons.png");
		
		textFieldListener1 = new TextInputListener()
		{
			@Override
			public void input(String text) 
			{
				petName = text;
				showNextDialog = true;
			}

			@Override
			public void canceled() 
			{
				
			}
		};
		textFieldListener2 = new TextInputListener()
		{
			@Override
			public void input(String text) 
			{
				ownerName = text;
				startButtonEnabled = true;
			}

			@Override
			public void canceled() 
			{
				
			}
		};
		
		Gdx.input.getTextInput(textFieldListener1, "Enter pet name", "Pick a good one!");
		
		buttonSetup();
		
		stage.addActor(startButton);
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		//this.game.mainTimer.tick(); this should not be started yet
		batch.setProjectionMatrix(camera.combined);
		
		stage.draw();
		
		if(showNextDialog)
		{
			Gdx.input.getTextInput(textFieldListener2, "Enter owner name", "Enter your name");
			showNextDialog = false;
		}
		
		//batch.begin();
		
		//batch.end();
		
		handleButtons();
	}
	
	@Override
	public void resize(int width, int height) 
	{
		stage.getViewport().update(width, height, false);
	}


	@Override
	public void hide() 
	{
		this.dispose();
	}


	@Override
	public void pause() 
	{

	}


	@Override
	public void resume() 
	{

	}
	
	public void buttonSetup()
	{
		//how much the buttons move
		final int buttonOffset = 1;
		
		//style has fields named pressedOffsetX and pressedOffSetY (didnt use because the text moved instead of the button on 
		//the timer
		startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture, 320, 40, 160, 40)));
		startButton.setPosition(800/2 - (startButton.getWidth()/2), 480/2 - (startButton.getHeight()/2));
		
	    startButton.addListener(new InputListener() 
		{
	        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                startButton.setPosition(startButton.getX() + 1, startButton.getY() + buttonOffset);
	                
	                if(startButtonEnabled)
	                {
		                Gdx.app.log("DEBUG", "GAMEINIT SCREEN backButton PRESSED");
		                changeScreenEnabled = true;
	                }
	                
	                return true;
	        }
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                startButton.setPosition(startButton.getX() - 1, startButton.getY() - buttonOffset);
	        }
		});
	}
	
	public void handleButtons()
	{
		if(changeScreenEnabled)
		{
			changeScreenEnabled = false;
			
			//2 = gameScreen
			this.game.setNames(petName, ownerName);
			
			this.game.getSaveData().putString("petName", this.game.getPetName());
			this.game.getSaveData().putString("ownerName", this.game.getOwnerName());
			this.game.getSaveData().putBoolean("saveGameExists", true);
			this.game.getSaveData().flush();
			
			this.game.handleScreens(2);
		}
	}
	
	@Override
	public void dispose()
	{
		Gdx.app.log("DEBUG", "GAMEINIT SCREEN dispose METHOD CALLED");
		
		buttonTexture.dispose();
		startButton.remove();
		
		stage.dispose();
	}
}