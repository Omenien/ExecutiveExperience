package com.Eddie.Box2DTest;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import net.dermetfan.utils.libgdx.graphics.Box2DSprite;

/**
 * Created by stejones26 on 5/7/2014.
 */
public class CameraHelper
{
	private static final String TAG = CameraHelper.class.getName();

	private Vector2 position;

	private Box2DSprite target;

	public CameraHelper()
	{
		position = new Vector2();
	}

	public void update(float deltaTime)
	{
		if(!hasTarget())
		{
			return;
		}

		position.x = target.getX() + target.getOriginX();

		position.y = target.getY() + target.getOriginY();
	}

	public void setPosition(float x, float y)
	{
		position.set(x, y);
	}

	public Vector2 getPosition()
	{
		return position;
	}

	public void setTarget(Box2DSprite target)
	{
		this.target = target;
	}

	public Box2DSprite getTarget()
	{
		return target;
	}

	public boolean hasTarget()
	{
		return target != null;
	}

	public boolean hasTarget(Box2DSprite target)
	{
		return hasTarget() && this.target.equals(target);
	}

	public void applyTo(OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;

		camera.update();
	}
}
