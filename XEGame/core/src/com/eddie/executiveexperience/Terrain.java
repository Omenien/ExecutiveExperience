package com.eddie.executiveexperience;

import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.Entity.UserData.GroundUserData;

public class Terrain extends GameActor
{
    public Terrain(Body body)
    {
        super(body);
    }

    @Override
    public GroundUserData getUserData()
    {
        return (GroundUserData) userData;
    }
}
