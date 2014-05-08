package com.Eddie.Box2DTest;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity
{
	public final static float PLAYER_WIDTH = 70;
	public final static float PLAYER_HEIGHT = 84;

	public Player(Vector2 position)
	{
		super(position, new Rectangle(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT));

		init(position);
	}

	public void init(Vector2 position)
	{

	}

	@Override
	public void update()
	{
		handleInput();
	}

	protected void handleInput()
	{

	}
}
