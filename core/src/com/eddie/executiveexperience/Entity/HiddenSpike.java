package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Entity.UserData.DeadlyFixtureUserData;
import com.eddie.executiveexperience.Entity.UserData.HiddenSpikeUserData;
import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

public class HiddenSpike extends GameActor
{
    public HiddenSpike(GameStage gameStage, float x, float y, MapProperties objectProperties)
    {
        super(gameStage);

        String spikeDirectionProperty = objectProperties.get("spikeDirection", "up", String.class);
        SpikeDirection spikeDirection = SpikeDirection.valueOf(spikeDirectionProperty.toUpperCase());
        System.out.println("Spike Direction: " + spikeDirection.toString());

        PolygonShape collisionShape = new PolygonShape();
        collisionShape.setAsBox(0.375f, 0.25f);

        PolygonShape spriteShape = new PolygonShape();
        spriteShape.setAsBox(0.5f, 0.5f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(new Vector2(x, y));

        Body body = gameStage.getWorld().createBody(bodyDef);
        Fixture collisionFixture = body.createFixture(collisionShape, 0);
        Fixture spriteFixture = body.createFixture(spriteShape, 0);
        spriteFixture.setSensor(true);
        collisionFixture.setSensor(true);
        collisionFixture.setUserData(new DeadlyFixtureUserData());
        body.resetMassData();

        HiddenSpikeUserData entityData = new HiddenSpikeUserData(gameStage, 0.5f * 2, 0.5f * 2, spriteFixture, spikeDirection);

        body.setUserData(entityData);

        collisionShape.dispose();
        spriteShape.dispose();

        setBody(body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        getUserData().getAnimatedBox2DSprite().draw(batch, getUserData().getSpriteFixture());
    }

    @Override
    public HiddenSpikeUserData getUserData()
    {
        return (HiddenSpikeUserData) userData;
    }

    public enum SpikeDirection
    {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
