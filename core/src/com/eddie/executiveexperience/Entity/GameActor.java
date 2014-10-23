package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.eddie.executiveexperience.Entity.UserData.UserData;
import com.eddie.executiveexperience.GameStage;

public abstract class GameActor extends Actor
{
    protected Body body;
    protected UserData userData;

    public GameActor(GameStage gameStage)
    {
        gameStage.addActor(this);
    }

    public void loadGameActor()
    {

    }

    public Body getBody()
    {
        return body;
    }

    public void setBody(Body body)
    {
        this.body = body;

        this.userData = (UserData) body.getUserData();
    }

    public abstract UserData getUserData();
}
