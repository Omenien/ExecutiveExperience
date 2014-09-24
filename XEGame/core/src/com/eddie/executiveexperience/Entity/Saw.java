package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.SawUserData;
import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

public class Saw extends GameActor
{
    public Saw(GameStage gameStage, float x, float y, MapProperties objectProperties)
    {
        super(gameStage);

        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.SAW_WIDTH / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        if(Boolean.parseBoolean(objectProperties.get("useCenter", "false", String.class)))
        {
            bodyDef.position.set(new Vector2(x + Constants.SAW_WIDTH / 2, y + Constants.SAW_HEIGHT / 2));
        }
        else
        {
            bodyDef.position.set(new Vector2(x + 0.5f, y + 0.5f));
        }

        Body body = gameStage.getWorld().createBody(bodyDef);
        body.createFixture(shape, Constants.ENEMY_DENSITY);
        body.resetMassData();

        SawUserData entityData = new SawUserData(gameStage, Constants.SAW_WIDTH, Constants.SAW_HEIGHT);

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

        float angle = body.getAngle() + (getUserData().getDegreesPerSecond() * MathUtils.degreesToRadians) * delta;

        //body.setTransform(body.getPosition().x, body.getPosition().y, angle);

        body.applyAngularImpulse(1.0f, true);
        body.applyTorque(1.0f, true);

//        body.setLinearVelocity(sawVelocity);
    }

    @Override
    public SawUserData getUserData()
    {
        return (SawUserData) userData;
    }


    public Body getBody()
    {
        return body;
    }
}