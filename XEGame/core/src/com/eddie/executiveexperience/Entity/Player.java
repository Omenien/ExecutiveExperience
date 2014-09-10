package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
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
        getUserData().getAnimatedBox2DSprite().play();
        // getUserData().getAnimatedBox2DSprite().draw(batch, body);
        getUserData().getAnimatedBox2DSprite().draw(batch, body);
    }

    public void jump()
    {
        if(!(jumping || hit))
        {
//            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(), body.getWorldCenter(), true);

            float angle = body.getAngle();

            float jumpingImpulseMagnitude = getUserData().getJumpingImpulseMagnitude();

            Vector2 jumpingLinearImpulse = new Vector2((float) (Math.cos(angle * Math.PI / 180)), (float) (Math.sin(angle * Math.PI / 180)));
            jumpingLinearImpulse.scl(jumpingImpulseMagnitude);

            body.applyLinearImpulse(jumpingLinearImpulse, body.getPosition(), true);

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