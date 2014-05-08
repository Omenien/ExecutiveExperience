package com.Eddie.Box2DTest;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class AbstractGameObject
{
	Vector2 position;
	Rectangle bounds;

	public AbstractGameObject(Vector2 position, Rectangle bounds)
	{
		this.position = position;

		this.bounds = bounds;
	}

	public void setPosition(Vector2 position)
	{
		this.position = position;
	}

	public void setBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}

	public Vector2 getPosition()
	{
		return position;
	}

	public Rectangle getBounds()
	{
		return bounds;
	}
}
