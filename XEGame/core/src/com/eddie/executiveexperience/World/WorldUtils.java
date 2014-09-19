package com.eddie.executiveexperience.World;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.EnemyType;
import com.eddie.executiveexperience.Entity.UserData.EnemyUserData;
import com.eddie.executiveexperience.Entity.UserData.FootSensorUserData;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Entity.UserData.SawUserData;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameStage;

import java.util.Random;

public class WorldUtils
{
    private final static Random random = new Random(12381912);

    private static World world;

    public static void createWorld()
    {
        world = new World(Env.gravity, true);
    }

    public static Body createPlayer(GameStage gameStage)
    {
        if(world == null)
        {
            createWorld();
        }

        Fixture playerSensorFixture;
        Fixture playerBodyFixture;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.PLAYER_X, Constants.PLAYER_Y));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2);

        PolygonShape sensorShape = new PolygonShape();
        float radius = Constants.PLAYER_WIDTH * 0.85f;
        Vector2[] vertices = new Vector2[8];
        vertices[0] = new Vector2(0, 0);
        for(int i = 0; i < 7; i++)
        {
            float angle = (i / 6.0f * 90f - 135f) * MathUtils.degreesToRadians;
            vertices[i + 1] = new Vector2(radius * MathUtils.cos(angle), radius * MathUtils.sin(angle));
        }
        sensorShape.set(vertices);

        Body body = world.createBody(bodyDef);
        playerBodyFixture = body.createFixture(shape, Constants.PLAYER_DENSITY);
        playerSensorFixture = body.createFixture(sensorShape, 0);
        playerSensorFixture.setSensor(true);
        playerSensorFixture.setUserData(new FootSensorUserData());

        body.resetMassData();
        body.setGravityScale(Constants.PLAYER_GRAVITY_SCALE);

        body.setUserData(new PlayerUserData(gameStage, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, playerBodyFixture, playerSensorFixture));

        //body.setFixedRotation(true);

        body.setBullet(true);

        sensorShape.dispose();
        shape.dispose();

        return body;
    }

    public static Body createEnemy()
    {
        EnemyType enemyType = EnemyType.SLIME_SMALL;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(enemyType.getX(), enemyType.getY()));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(enemyType.getWidth() / 2, enemyType.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, enemyType.getDensity());
        body.resetMassData();
        EnemyUserData entityData = new EnemyUserData(enemyType.getWidth(), enemyType.getHeight());
        body.setUserData(entityData);
        shape.dispose();
        return (body);
    }

    public static Body createSaw(GameStage gameStage)
    {
        EnemyType enemyType = EnemyType.SAW_STATIONARY_SLOW;

        float sawY = random.nextFloat() * (gameStage.getMapHeight() - enemyType.getHeight());

        if(sawY < 0)
        {
            sawY = 30;
        }

        CircleShape shape = new CircleShape();
        shape.setRadius(enemyType.getWidth() / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(enemyType.getX(), sawY));

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, enemyType.getDensity());
        body.resetMassData();

        SawUserData entityData = new SawUserData(gameStage, enemyType.getWidth(), enemyType.getHeight());

        body.setUserData(entityData);

        shape.dispose();

        return body;
    }

    public static World getWorld()
    {
        return world;
    }
}
