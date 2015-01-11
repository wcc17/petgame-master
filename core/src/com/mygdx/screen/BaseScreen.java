/**
package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.util.Timer;

public class BaseScreen implements Screen
{	
	SpriteBatch batch;
	
	public BaseScreen(MyGdxGame game)
	{
		this.game = game;
	}
	

	@Override
	public void render(float delta)
	{
		
		
	}

	@Override
	public void resize(int width, int height) 
	{
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void show() 
	{
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new StretchViewport(800, 480, camera));
		Gdx.input.setInputProcessor(stage);
		batch = (SpriteBatch) stage.getBatch();
	}

	@Override
	public void hide() 
	{
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() 
	{
		// IS NEVER CALLED AUTOMATICALLY, NEED TO CALL THIS METHOD 
		batch.dispose();
		//stage.dispose();
		
		Gdx.app.log("DEBUG: ", "BASE DISPOSE METHOD CALLED");
		
	}
	

}
**/
