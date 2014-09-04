package com.eddie.executiveexperience;

import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.Entity.UserData.GroundUserData;

public class Ground extends GameActor
{
    public Ground(Body body)
    {
        super(body);
    }

    @Override
    public GroundUserData getUserData()
    {
        return (GroundUserData) userData;
    }
}
