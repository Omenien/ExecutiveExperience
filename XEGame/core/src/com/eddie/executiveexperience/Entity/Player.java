package com.eddie.executiveexperience.Entity;

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

    public void jump()
    {
        if(!(jumping || hit))
        {
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(), body.getWorldCenter(), true);

            jumping = true;
        }
        else
        {
            System.out.println("jump fail");
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