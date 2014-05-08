package com.Eddie.Box2DTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.dermetfan.utils.libgdx.box2d.Box2DMapObjectParser;

/**
 * Created by stejones26 on 5/7/2014.
 */
public class WorldController extends InputAdapter
{
	private static final String TAG = WorldController.class.getName();

	public CameraHelper cameraHelper;

	private World world;

	private TiledMap map;
	private TmxMapLoader mapLoader;

	private Box2DMapObjectParser parser;

	public WorldController()
	{
		init();
	}

	private void init()
	{
		Gdx.input.setInputProcessor(this);

		world = new World(new Vector2(0, -9.81f), true);

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map.tmx");

		parser = new Box2DMapObjectParser();
		parser.load(world, map);

		cameraHelper = new CameraHelper();
	}

	private void initPhysics()
	{
		if(world != null)
		{
			world.dispose();
		}

		world = new World(new Vector2(0, -9.81f), true);
	}

	public void update(float deltaTime)
	{
		cameraHelper.update(deltaTime);
	}

	@Override
	public boolean keyUp(int keyCode)
	{
		if(keyCode == Input.Keys.R)
		{
			init();

			Gdx.app.debug(TAG, "Game World Reset");
		}

		return false;
	}

	public World getWorld()
	{
		return world;
	}

	public TiledMap getMap()
	{
		return map;
	}
}
