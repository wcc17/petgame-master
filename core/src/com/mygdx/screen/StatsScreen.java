package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;

public class StatsScreen implements Screen
{
	MyGdxGame game;
	SpriteBatch batch;
	
	OrthographicCamera camera;
	Stage stage;
	
	Image petImage;
	Texture petTexture;
	
	Image backgroundImage;
	Texture backgroundTexture;
	
	Texture buttonTexture;
	ImageButton backButton;
	
	//just set to default Arial font with default empty constructor
	BitmapFont arialFont;
	CharSequence timerText;
	CharSequence petNameText;
	CharSequence ownerNameText;
	
	//is set to true when back button is pressed, then checked in handleButtons
	boolean changeScreenEnabled = false;

	public StatsScreen(MyGdxGame gam) 
	{
		this.game = gam;
	}
	
	@Override
	public void show()
	{
		Gdx.app.log("DEBUG", "STATS SCREEN show METHOD CALLED");
		
		batch = game.getBatch();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new StretchViewport(800, 480, camera), batch);
		Gdx.input.setInputProcessor(stage);
		
		backgroundTexture = new Texture("statsbackground.png");
		backgroundImage = new Image(backgroundTexture);
		
		buttonTexture = new Texture("allbuttons.png");
		petTexture = new Texture("pet.png");
		petImage = new Image(petTexture);
		petImage.setHeight(115);
		petImage.setWidth(120);
		petImage.setPosition(645.0f, 337.5f);
		
		arialFont = new BitmapFont();
		arialFont.setColor(Color.BLACK);
		arialFont.setScale(1.2f);
		petNameText = this.game.getPetName();
		ownerNameText = this.game.getOwnerName();
		
		buttonSetup();
		
		stage.addActor(backgroundImage);
		stage.addActor(petImage);
		stage.addActor(backButton);
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		this.game.mainTimer.tick();
		batch.setProjectionMatrix(camera.combined);
		
		stage.draw();
		
		batch.begin();
		
		//set timer
		timerText = "Age: " + this.game.mainTimer.days + "d, " + this.game.mainTimer.hours 
				+ "h, " + this.game.mainTimer.minutes + "m, " + this.game.mainTimer.seconds + "s";
		arialFont.draw(batch, timerText, 200, 300);
		arialFont.draw(batch, petNameText, 200, 320);
		arialFont.draw(batch, ownerNameText, 200, 340);
		
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
		backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture, 160, 40, 160, 40)));
		backButton.setPosition(5, (475 - backButton.getHeight()));
		backButton.addListener(new InputListener() 
		{
	        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                backButton.setPosition(backButton.getX() + 1, backButton.getY() + buttonOffset);
	                Gdx.app.log("DEBUG", "STATS SCREEN backButton PRESSED");
	                changeScreenEnabled = true;
	                return true;
	        }
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button) 
	        {
	                backButton.setPosition(backButton.getX() - 1, backButton.getY() - buttonOffset);
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
		Gdx.app.log("DEBUG", "STATS SCREEN dispose METHOD CALLED");
		
		petImage.remove();	//this may cause problems, good catch if it doesn't
		backButton.remove();
		stage.dispose();
		petTexture.dispose();
		buttonTexture.dispose();
		arialFont.dispose();
		backgroundTexture.dispose();
	}
}