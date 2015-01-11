package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Pet;
import com.mygdx.game.Scoop;

public class GameScreen implements Screen
{
	MyGdxGame game;
	SpriteBatch batch;
	
	OrthographicCamera camera;
	Stage stage;
	
	Texture emptyProgressBarTexture;
	
	Texture petTexture;
	Pet pet;
	Scoop scoop;
	
	//FLOOR IS AT ~410 (~70)
	Image background;
	Texture backgroundTexture;
	
	//button data
	Texture buttonTexture;
	ImageButton feedButton;
	ImageButton sleepButton;
	ImageButton cleanButton;
	ImageTextButton timerButton;
	ImageTextButtonStyle timerButton_style;
	
	//just set to default Arial font with default empty constructor
	BitmapFont timerFont;
	String timerText;
	
	//used to switch between different user actions, eg feed pet animation, timer screen, sleep screen
	boolean feedPetEnabled = false;
	boolean sleepPetEnabled = false;
	boolean timeScreenEnabled = false;
	boolean poopScreenEnabled = false;
	
	public GameScreen(MyGdxGame gam) 
	{
		this.game = gam;
	}

	
	@Override
	public void show() 
	{
		Gdx.app.log("DEBUG", "GAME SCREEN show METHOD CALLED");
		
		//check for save game upon initialization of gameScene and make set variables if so
		//this.game.checkSaveGame();
		
		batch = game.getBatch();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new StretchViewport(800, 480, camera), batch);
		Gdx.input.setInputProcessor(stage);
		
		backgroundTexture = new Texture("background.png");
		background = new Image(backgroundTexture);
		
		petTexture = new Texture("pet.png");
		buttonTexture = new Texture("allbuttons.png");
		
		emptyProgressBarTexture = new Texture("progressbarEmpty.png");
		
		//set up pet
		pet = new Pet(petTexture, 0, 0, petTexture.getWidth(), petTexture.getHeight());
		scoop = new Scoop();
		
		//IDK MAYBE USE THIS, PROBABLY NOT, timer IS PASSED FROM PREVIOUS SCENE SO PROBS NOT
		//if no birth time exists in the settings file (whatever is used to load save data later)
		//mainTimer = new Timer(System.currentTimeMillis(), true);
		
		if(this.game.getSaveData().getBoolean("gameScreenSaveDataExists"))
		{
			getGameScreenData();
		}
		else
		{
			//Attributes class starts at 4 by default
			this.game.attributesManager.getPoopAttribute().setValue(0); 
		}
		
		//assigns to default Arial font with empty constructor
		//timerFont = new BitmapFont(Gdx.files.internal("calibri.fnt"), false);
		timerFont = new BitmapFont();
		
		buttonSetup();
		
		stage.addActor(background);
		stage.addActor(pet);
		stage.addActor(feedButton);
		stage.addActor(sleepButton);
		stage.addActor(cleanButton);
		stage.addActor(timerButton);
		stage.addActor(scoop);
		
	}

	@Override
	public void render(float delta) 
	{
		// update and draw stuff
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		this.game.mainTimer.tick();
		batch.setProjectionMatrix(camera.combined);
		
		//picks a random direction to move in, then moves
		pet.handleTimers();
		pet.move();
		
		this.game.attributesManager.accumulate((int)pet.getX(), (int)pet.getY());
		
		stage.act();
		stage.draw();
		
		//ACTUAL DRAWING TO SCREEN
		batch.begin();
		
		batch.draw(emptyProgressBarTexture, 15, 440);
		batch.draw(emptyProgressBarTexture, 15, 405);
		this.game.attributesManager.drawAttributes(poopScreenEnabled, batch);
		
		//set timer
		timerText = this.game.mainTimer.days + "d, " + this.game.mainTimer.hours 
				+ "h, " + this.game.mainTimer.minutes + "m, " + this.game.mainTimer.seconds + "s";
		timerButton.setText(timerText);
		
		batch.end();
		//END DRAWING TO SCREEN
		
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
		dispose();
	}


	@Override
	public void pause() 
	{
		Gdx.app.log("DEBUG", "GAME SCREEN pause METHOD CALLED");
		dispose();
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
		feedButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture, 0, 0, 160, 40)));
		feedButton.setPosition(445, 430);
		feedButton.addListener(new InputListener() 
		{
	        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                feedButton.setPosition(feedButton.getX() + 1, feedButton.getY() + buttonOffset);
	                feedPetEnabled = true;
	                return true;
	        }
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                feedButton.setPosition(feedButton.getX() - 1, feedButton.getY() - buttonOffset);
	        }
		});
		
		sleepButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture, 160, 0, 160, 40)));
		sleepButton.setPosition(620, 380);
		sleepButton.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				sleepButton.setPosition(sleepButton.getX() + 1, sleepButton.getY() + buttonOffset);
				sleepPetEnabled = true;
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				sleepButton.setPosition(sleepButton.getX() - 1, sleepButton.getY() - buttonOffset);
			}
		});
		
		cleanButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture, 0, 40, 160, 40)));
		cleanButton.setPosition(445, 380);
		cleanButton.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				cleanButton.setPosition(cleanButton.getX() + 1, cleanButton.getY() + buttonOffset);
				if(!poopScreenEnabled)
				{
					poopScreenEnabled = true;
					scoop.setVisible(true);
					scoop.move();
					
					GameScreen.this.game.attributesManager.cleanPoop();
					
					cleanButton.setVisible(false);
					feedButton.setVisible(false);
					timerButton.setVisible(false);
					sleepButton.setVisible(false);
					pet.setVisible(false);
				}
				
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				cleanButton.setPosition(cleanButton.getX() - 1, cleanButton.getY() - buttonOffset);
			}
		});
		
		timerButton_style = new ImageTextButtonStyle();
		timerButton_style.up = timerButton_style.down = new TextureRegionDrawable(new TextureRegion(buttonTexture, 320, 0, 160, 40));
		timerButton_style.font = timerFont;
		timerButton_style.fontColor = Color.BLACK;
		timerButton = new ImageTextButton(timerText, timerButton_style);
		timerButton.setPosition(620, 430);
		timerButton.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				timerButton.setPosition(timerButton.getX() + 1, timerButton.getY() + buttonOffset);
				timeScreenEnabled = true;
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				timerButton.setPosition(timerButton.getX() - 1,  timerButton.getY() - buttonOffset);
			}
		});
	}
	
	public void handleButtons()
	{
		if(feedPetEnabled)
		{
			//do things that happen when feeding the pet (an animation)
			feedPetEnabled = false;
			
			this.game.attributesManager.feedPet();
		}
		else if(sleepPetEnabled)
		{
			sleepPetEnabled = false;
			
			//pass the pet's current sleep value to the sleep screen for sleeping
			this.game.sleepScreen.sleepValue = this.game.attributesManager.getSleepAttribute().getValue();
			this.game.handleScreens(5);
		}
		else if(timeScreenEnabled)
		{
			//show time screen, pet stats
			timeScreenEnabled = false;
			
			this.game.handleScreens(3);
		}
		else if(poopScreenEnabled)
		{
			if(scoop.getActions().size < 1)
			{
				scoop.setVisible(false);
				scoop.setPosition(800, 0);
				poopScreenEnabled = false;
				
				cleanButton.setVisible(true);
				feedButton.setVisible(true);
				timerButton.setVisible(true);
				sleepButton.setVisible(true);
				pet.setVisible(true);
			}
		}
	}
	
	@Override
	public void dispose() 
	{
		Gdx.app.log("DEBUG", "GAME SCREEN dispose METHOD CALLED");
		
		saveGameScreenData();
		
		backgroundTexture.dispose();
		petTexture.dispose();
		buttonTexture.dispose();
		stage.dispose();
		timerButton.remove();
		emptyProgressBarTexture.dispose();
		
		//maybe remove actors here
	}
	
	public void saveGameScreenData()
	{
		this.game.attributesManager.saveAttributeData(game);
		
		//here we can use the attribute data and change rates to predict when
		//the user will need to recieve notifications. if the user opens the game again,
		//the notifications should be cleared. the game should reflect the information in the
		//notifications, so when the user getsGameScreenData, calculations based on elapsed
		//time will be applied to the data gotten from getsGameScreenData before the user sees it
		//this way, if the user recieves one of the scheduled notifications (ie, pet is hungry 
		//notification when hungerBar is in 4th percentile), the calculations after getsGameScreenData
		//will refelect what was said in the notification.
	}
	
	public void getGameScreenData()
	{
		//retrieves from savedData
		this.game.attributesManager.getAttributeData(game);
		
		//see comments in saveGameScreeData()
		
	}
	
	
}
