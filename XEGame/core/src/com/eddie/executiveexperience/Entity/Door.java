package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.DoorUserData;
import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

public class Door extends GameActor
{
    public Door(GameStage gameStage, float x, float y, MapProperties objectProperties)
    {
        super(gameStage);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.DOOR_WIDTH / 2, Constants.DOOR_HEIGHT / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        Body body = gameStage.getWorld().createBody(bodyDef);
        Fixture bodyFixture = body.createFixture(shape, 0);
        bodyFixture.setSensor(true);
        body.resetMassData();

        DoorUserData entityData = new DoorUserData(Constants.DOOR_WIDTH, Constants.DOOR_HEIGHT, objectProperties.get("newLevel", "Level 1.tmx", String.class));

        body.setUserData(entityData);

        shape.dispose();

        setBody(body);
    }

    @Override
    public DoorUserData getUserData()
    {
        return (DoorUserData) userData;
    }
}
