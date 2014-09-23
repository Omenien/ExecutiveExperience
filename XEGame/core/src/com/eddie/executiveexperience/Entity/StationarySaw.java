package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.maps.MapProperties;
import com.eddie.executiveexperience.GameStage;

public class StationarySaw extends Saw
{
    public StationarySaw(GameStage gameStage, float x, float y, MapProperties objectProperties)
    {
        super(gameStage, x, y, objectProperties);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
    }
}
