package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.MovingPlatformUserData;
import com.eddie.executiveexperience.GameStage;

import java.util.Vector;

public class MovingPlatform extends GameActor
{
    protected Vector2 startPos;
    protected Vector2 finalPos;

    protected float speed;

    protected int movingTo;

    protected Vector<Body> passengers;

    public MovingPlatform(GameStage gameStage, float x, float y, MapObject mapObject)
    {
        super(gameStage);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(new Vector2(x, y));

        Body body = gameStage.getWorld().createBody(bodyDef);
        body.createFixture(shape, Constants.ENTITY_DENSITY);
        body.resetMassData();

        MovingPlatformUserData entityData = new MovingPlatformUserData(gameStage, getClass().getSimpleName(), 1f, 1f);

        body.setUserData(entityData);

        shape.dispose();

        setBody(body);

        passengers = new Vector<>();

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

        if(newVelocity.len() > speed)
        {
            newVelocity.nor();

            newVelocity.scl(speed);
        }
        else
        {
            movingTo = movingTo == 1 ? 0 : 1;

            for(Body passenger : passengers)
            {
                passenger.setLinearVelocity(newVelocity);
            }
        }

        body.setLinearVelocity(newVelocity);

        for(Body passenger : getUserData().getPassengers())
        {
            Vector2 passengerVelocity = passenger.getLinearVelocity();

            String output = "Passenger " + passenger.getUserData().getClass().getSimpleName() + ": Start Velocity - " + passengerVelocity.toString();

            passengerVelocity.x += newVelocity.x;

            if(newVelocity.y > 0)
            {
                passengerVelocity.y += newVelocity.y;
            }

            passenger.setLinearVelocity(passengerVelocity);

            output += ", New Velocity - " + passengerVelocity.toString();

            System.out.println(output);
        }
    }

    @Override
    public MovingPlatformUserData getUserData()
    {
        return (MovingPlatformUserData) userData;
    }
}
