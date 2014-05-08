package com.Eddie.Box2DTest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import net.dermetfan.utils.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.utils.libgdx.graphics.AnimatedSprite;

import java.util.ArrayList;

public abstract class Entity extends AbstractGameObject
{
	Texture texture;
	ArrayList<TextureRegion> frames;

	Animation animation;
	AnimatedSprite animatedSprite;

	AnimatedBox2DSprite sprite;

	Body body;
	BodyDef bodyDef;

	public Entity()
	{
		super(new Vector2(0, 0), new Rectangle());
	}

	public void init(Vector2 position, Rectangle bounds)
	{
		animation = new Animation(1 / 3f, (TextureRegion[]) (frames.toArray()));
		animatedSprite = new AnimatedSprite(animation);

		sprite = new AnimatedBox2DSprite(animatedSprite);

		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		body = Game.getInstance().getWorldController().getWorld().createBody(bodyDef);
		body.setUserData(sprite);
	}

	public abstract void update();
}
