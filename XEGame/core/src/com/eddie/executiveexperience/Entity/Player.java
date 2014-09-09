package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.*;
import com.eddie.executiveexperience.Animation.SpriteAnimationData;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Screens.GameScreen;

public class Player extends GameActor
{
    private boolean jumping;
    private boolean hit;

    private SpriteAnimationData spriteAnimationData;

    public Player(GameStage gameStage, Body body)
    {
        super(body);

        spriteAnimationData = gameStage.getAssetManager().get("player/PlayerAnimation.json");
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