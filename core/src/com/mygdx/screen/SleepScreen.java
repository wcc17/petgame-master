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
import com.mygdx.game.Attributes;
import com.mygdx.game.MyGdxGame;

public class SleepScreen implements Screen
{
	MyGdxGame game;
	SpriteBatch batch;
	
	OrthographicCamera camera;
	Stage stage;
	
	Image petImage;
	Texture petTexture;
	
	public int sleepValue;
	Texture emptyProgressBarTexture;
	
	Texture buttonTexture;
	ImageButton backButton;
	ImageTextButton timerButton;
	ImageTextButtonStyle timerButton_style;
	
	BitmapFont arialFont;
	String timerText;
	
	boolean changeScreenEnabled;
	
	public SleepScreen(MyGdxGame gam)
	{
		this.game = gam;
	}
	
	@Override
	public void show()
	{
		Gdx.app.log("DEBUG", "SLEEP SCREEN show METHOD CALLED");
		
		batch = game.getBatch();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new StretchViewport(800, 480, camera), batch);
		Gdx.input.setInputProcessor(stage);
		
		petTexture = new Texture("pet.png");
		petImage = new Image(petTexture);
		petImage.setPosition(((800/2) - petImage.getWidth()/2),((480/2) - petImage.getHeight()/2));
		
		emptyProgressBarTexture = new Texture("progressbarEmpty.png");
		this.game.attributesManager.getSleepAttribute().setValue(sleepValue);	//sleep value is set in GameScreen before switching to this screen
		this.game.attributesManager.getSleepAttribute().setAccumulationTime(10);
		
		arialFont = new BitmapFont();
		arialFont.setColor(Color.BLACK);
		
		buttonTexture = new Texture("allbuttons.png");
		buttonSetup();
		
		stage.addActor(backButton);
		stage.addActor(timerButton);
		stage.addActor(petImage);
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		this.game.mainTimer.tick();
		batch.setProjectionMatrix(camera.combined);
		
		//set timer
		timerText = this.game.mainTimer.days + "d, " + this.game.mainTimer.hours 
				+ "h, " + this.game.mainTimer.minutes + "m, " + this.game.mainTimer.seconds + "s";
		timerButton.setText(timerText);
		
		this.game.attributesManager.getSleepAttribute().accumulateAttribute(true, 3);
		
		if(this.game.attributesManager.getSleepAttribute().getValue() == 145)
		{
			changeScreenEnabled = true;
		}
		
		stage.draw();
		
		batch.begin();
		//batch.draw(sleepAttribute.getTextureRegion(), 8, 20);
		batch.draw(emptyProgressBarTexture, 15, 20);
		batch.draw(this.game.attributesManager.getSleepAttribute().getFullProgressBarRegion(), 15, 20);
		batch.end();
		
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
		this.dispose();
	}
	
	public void buttonSetup()
	{
		//how much the buttons move
		final int buttonOffset = 1;
		
		//style has fields named pressedOffsetX and pressedOffSetY (didnt use because the text moved instead of the button on 
		//the timer
		backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture, 160, 40, 160, 40)));
		backButton.setPosition(5, (475 - backButton.getHeight()));
		backButton.addListener(new InputListener() 
		{
	        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                backButton.setPosition(backButton.getX() + 1, backButton.getY() + buttonOffset);
	                Gdx.app.log("DEBUG", "SLEEP SCREEN backButton PRESSED");
	                changeScreenEnabled = true;
	                return true;
	        }
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                backButton.setPosition(backButton.getX() - 1, backButton.getY() - buttonOffset);
	        }
		});
		
		timerButton_style = new ImageTextButtonStyle();
		timerButton_style.up = timerButton_style.down = new TextureRegionDrawable(new TextureRegion(buttonTexture, 320, 0, 160, 40));
		timerButton_style.font = arialFont;
		timerButton_style.fontColor = Color.BLACK;
		timerButton = new ImageTextButton(timerText, timerButton_style);
		timerButton.setPosition(620, 430);
		timerButton.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				//timerButton.setPosition(timerButton.getX() + 1, timerButton.getY() + buttonOffset);
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				//timerButton.setPosition(timerButton.getX() - 1,  timerButton.getY() - buttonOffset);
			}
		});
	}
	
	public void handleButtons()
	{
		if(changeScreenEnabled)
		{
			changeScreenEnabled = false;
			
			//2 = gameScreen
			this.game.handleScreens(2);
		}
	}
	
	@Override
	public void dispose()
	{
		Gdx.app.log("DEBUG", "SLEEP SCREEN dispose METHOD CALLED");
		
		saveSleepScreenData();
		
		petImage.remove();	//this may cause problems, good catch if it doesn't
		backButton.remove();
		timerButton.remove();
		stage.dispose();
		petTexture.dispose();
		buttonTexture.dispose();
		emptyProgressBarTexture.dispose();
	}

	@Override
	public void resume() 
	{
	}
	
	public void saveSleepScreenData()
	{
		this.game.getSaveData().putInteger("sleep_value", this.game.attributesManager.getSleepAttribute().getValue());
		this.game.getSaveData().flush();
	}

}
