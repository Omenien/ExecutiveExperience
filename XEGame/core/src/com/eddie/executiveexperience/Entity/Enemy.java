package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.Entity.UserData.EnemyUserData;
import com.eddie.executiveexperience.GameActor;

/**
 * Created by scitech on 9/4/2014.
 */
public class Enemy extends GameActor
{
    public Enemy(Body body)
    {
        super(body);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        body.setLinearVelocity(getUserData().getVelocity());
    }

    @Override
    public EnemyUserData getUserData()
    {
        return (EnemyUserData) userData;
    }
}
