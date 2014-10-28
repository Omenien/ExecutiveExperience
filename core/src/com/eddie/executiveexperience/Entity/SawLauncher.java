package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.SawLauncherUserData;
import com.eddie.executiveexperience.GameStage;
import com.eddie.executiveexperience.XEGame;

public class SawLauncher extends GameActor
{
    protected Vector2 sawVelocity;
    protected Saw spawnedSaw;

    public SawLauncher(GameStage gameStage, float x, float y, MapObject mapObject)
    {
        super(gameStage);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(new Vector2(x, y));

        Body body = gameStage.getWorld().createBody(bodyDef);
        Fixture fixture = body.createFixture(shape, Constants.ENTITY_DENSITY);
        body.resetMassData();

        SawLauncherUserData entityData = new SawLauncherUserData(gameStage, 1f, 1f);

        body.setUserData(entityData);

        shape.dispose();

        setBody(body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        getUserData().getAnimatedBox2DSprite().draw(batch, body);
    }

    @Override
    public void act(float delta)
    {
        if(body == null)
        {
            return;
        }

        super.act(delta);

        if(spawnedSaw == null)
        {
            spawnedSaw = new MovingSaw(XEGame.game.getGameScreen().getGameStage(), body.getPosition().x, body.getPosition().y, new Vector2(-1, -1));
        }
    }


    @Override
    public SawLauncherUserData getUserData()
    {
        return (SawLauncherUserData) userData;
    }
}
