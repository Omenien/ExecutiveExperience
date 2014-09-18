package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.SawUserData;
import com.eddie.executiveexperience.GameActor;

public class Saw extends GameActor
{
    public Saw(Body body)
    {
        super(body);
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

        float angle = body.getAngle() + (getUserData().getDegreesPerSecond() * MathUtils.degreesToRadians) * delta;

        body.setTransform(body.getPosition().x, body.getPosition().y, angle);

        body.setLinearVelocity(Constants.SAW_LINEAR_VELOCITY);
    }

    @Override
    public SawUserData getUserData()
    {
        return (SawUserData) userData;
    }


    public Body getBody()
    {
        return body;
    }
}