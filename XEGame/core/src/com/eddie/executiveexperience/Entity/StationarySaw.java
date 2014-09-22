package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by scitech on 9/22/2014.
 */
public class StationarySaw extends Saw
{
    public StationarySaw(Body body)
    {
        super(body);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
    }
}
