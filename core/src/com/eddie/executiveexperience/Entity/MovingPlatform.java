package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.MovingPlatformUserData;
import com.eddie.executiveexperience.GameStage;

public class MovingPlatform extends GameActor
{
    protected Vector2 startPos;
    protected Vector2 finalPos;

    protected float speed;

    protected int movingTo;

    public MovingPlatform(GameStage gameStage, float x, float y, MapObject mapObject)
    {
        super(gameStage);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.5f, 0.271428571f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(new Vector2(x, y));

        Body body = gameStage.getWorld().createBody(bodyDef);
        Fixture fixture = body.createFixture(shape, Constants.ENTITY_DENSITY);
        fixture.setFriction(5.0f);
        body.resetMassData();

        MovingPlatformUserData entityData = new MovingPlatformUserData(gameStage, getClass().getSimpleName(), 3f, 0.542857143f);

        body.setUserData(entityData);

        shape.dispose();

        setBody(body);

        movingTo = 1;

        startPos = new Vector2(x, y);

        finalPos = new Vector2(Float.parseFloat(mapObject.getProperties().get("finalPosX", "0.0", String.class)), Float.parseFloat(mapObject.getProperties().get("finalPosY", "0.0", String.class)));

        speed = Float.parseFloat(mapObject.getProperties().get("speed", "5.0", String.class));
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        getUserData().getAnimatedBox2DSprite().draw(batch, body);
    }

    @Override
    public void act(float delta)
    {
        if(body == null)
        {
            return;
        }

        super.act(delta);

        Vector2 destination = movingTo == 1 ? new Vector2(finalPos.x, finalPos.y) : new Vector2(startPos.x, startPos.y);
        destination.sub(body.getPosition());

        Vector2 newVelocity = destination;

        if(newVelocity.len() > speed / 60f)
        {
            newVelocity.nor();

            newVelocity.scl(speed);
        }
        else
        {
            movingTo = movingTo == 1 ? 0 : 1;
        }

        body.setLinearVelocity(newVelocity);
    }

    @Override
    public MovingPlatformUserData getUserData()
    {
        return (MovingPlatformUserData) userData;
    }
}
