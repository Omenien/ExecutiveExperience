package com.Eddie.Box2DTest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.dermetfan.utils.libgdx.graphics.Box2DSprite;

public abstract class AbstractGameObject
{
	Vector2 position;

	BodyDef bodyDef;
	Body body;

	FixtureDef fixtureDef;
	Fixture fixture;

	Box2DSprite sprite;

	public AbstractGameObject(Vector2 pos)
	{
		position = pos;

		bodyDef = new BodyDef();

		fixtureDef = new FixtureDef();

		body = Game.getInstance().getWorldController().getWorld().createBody(bodyDef);

		fixture = body.createFixture(fixtureDef);

		body.setUserData(sprite);

		fixture.setUserData(sprite);
	}
}
