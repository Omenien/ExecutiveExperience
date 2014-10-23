package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Entity.UserData.DoorUserData;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameStage;

public class Door extends GameActor
{
    public Door(GameStage gameStage, float x, float y, MapObject mapObject)
    {
        super(gameStage);

        MapProperties objectProperties = mapObject.getProperties();

        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        float width = rectObject.getRectangle().getWidth() * Env.pixelsToMeters;
        float height = rectObject.getRectangle().getHeight() * Env.pixelsToMeters;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width / 2, y + height / 2);

        Body body = gameStage.getWorld().createBody(bodyDef);
        Fixture bodyFixture = body.createFixture(shape, 0);
        bodyFixture.setSensor(true);
        body.resetMassData();

        DoorUserData entityData = new DoorUserData(width, height, objectProperties.get("newLevel", "Level 1.tmx", String.class));

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
