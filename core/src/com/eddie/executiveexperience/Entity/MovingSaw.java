package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.math.Vector2;
import com.eddie.executiveexperience.GameStage;

public class MovingSaw extends Saw
{
    protected Vector2 velocity;

    public MovingSaw(GameStage gameStage, float x, float y, Vector2 velocity)
    {
        super(gameStage, x, y);

        this.velocity = velocity;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        body.setLinearVelocity(velocity);
    }
}
