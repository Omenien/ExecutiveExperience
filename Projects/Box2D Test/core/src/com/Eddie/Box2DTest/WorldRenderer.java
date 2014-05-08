package com.Eddie.Box2DTest;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;

	private SpriteBatch spriteBatch;

	private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	private Box2DDebugRenderer debugRenderer;

	private WorldController worldController;

	TiledMapRenderer mapRenderer;

	public WorldRenderer(WorldController worldController)
	{
		this.worldController = worldController;

		init();
	}

	private void init()
	{
		spriteBatch = new SpriteBatch();

		debugRenderer = new Box2DDebugRenderer();

		mapRenderer = new OrthogonalTiledMapRenderer(worldController.getMap());

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.position.set(0, 0 ,0);
		camera.position.add(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, 0);

		mapRenderer.setView(camera);
	}

	public  void render()
	{
		mapRenderer.setView(camera);

		mapRenderer.render();

		if (DEBUG_DRAW_BOX2D_WORLD)
		{
			debugRenderer.render(worldController.getWorld(), camera.combined);
		}

		spriteBatch.setProjectionMatrix(camera.combined);

		spriteBatch.begin();

		spriteBatch.end();
	}

	public void handleInput()
	{
		if(Gdx.app.getType() != Application.ApplicationType.Desktop)
		{
			return;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A))
		{

		}
	}

	public void resize(int width, int height)
	{
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void dispose()
	{
		 spriteBatch.dispose();
	}
}
