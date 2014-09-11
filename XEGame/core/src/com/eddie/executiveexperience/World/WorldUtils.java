package com.eddie.executiveexperience.World;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.EnemyType;
import com.eddie.executiveexperience.Entity.UserData.EnemyUserData;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameStage;

public class WorldUtils
{
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

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.PLAYER_X, Constants.PLAYER_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.PLAYER_DENSITY);
        body.resetMassData();
        body.setGravityScale(Constants.PLAYER_GRAVITY_SCALE);
        body.setUserData(new PlayerUserData(gameStage, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        body.setFixedRotation(true);
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

    public static World getWorld()
    {
        return world;
    }
}
