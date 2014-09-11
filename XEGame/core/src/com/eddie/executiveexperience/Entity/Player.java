package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.GameActor;

public class Player extends GameActor
{
    private boolean jumping;
    private boolean hit;

    public Player(Body body)
    {
        super(body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        getUserData().getAnimatedBox2DSprite().draw(batch, body);
    }

    public void jump()
    {
        if(!(jumping || hit))
        {
//            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(), body.getWorldCenter(), true);

            float angle = MathUtils.radiansToDegrees * body.getAngle();

            float jumpingImpulseMagnitude = getUserData().getJumpingImpulseMagnitude();

            float xComponent = (float) (Math.sin(angle * Math.PI / 180));
            float yComponent = (float) (Math.cos(angle * Math.PI / 180));

            Vector2 jumpingLinearImpulse = new Vector2(xComponent, yComponent);
            jumpingLinearImpulse.scl(jumpingImpulseMagnitude);

            body.applyLinearImpulse(jumpingLinearImpulse, body.getWorldCenter(), true);

            jumping = true;
        }
    }

    public void hit()
    {
        body.applyAngularImpulse(getUserData().getHitAngularImpulse(), true);

        hit = true;
    }

    public void landed()
    {
        if(jumping)
        {
            jumping = false;
        }

        if(hit)
        {
            hit = false;
        }
    }

    public boolean isHit()
    {
        return hit;
    }

    @Override
    public PlayerUserData getUserData()
    {
        return (PlayerUserData) userData;
    }
}