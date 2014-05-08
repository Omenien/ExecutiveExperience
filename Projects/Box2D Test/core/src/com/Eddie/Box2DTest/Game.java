package com.Eddie.Box2DTest;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter
{
	private static Game instance;

	private static final String TAG = Game.class.getName();

	private WorldController worldController;

	private WorldRenderer worldRenderer;

	public Game()
	{
		instance = this;
	}

	public static Game getInstance()
	{
		return instance;
	}

	@Override
	public void create()
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
	}

	@Override
	public void render()
	{
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();
	}

	public void update(float deltaTime)
	{
		worldController.update(deltaTime);
	}

	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void dispose()
	{
		worldRenderer.dispose();
	}

	public WorldRenderer getWorldRenderer()
	{
		return worldRenderer;
	}

	public WorldController getWorldController()
	{
		return worldController;
	}
}
