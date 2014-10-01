package com.eddie.executiveexperience;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.eddie.executiveexperience.Entity.UserData.CollisionFixtureUserData;
import com.eddie.executiveexperience.Entity.UserData.UserData;
import com.eddie.executiveexperience.Entity.UserData.UserDataType;

public class BodyUtils
{
    public static boolean bodyIsPlayer(Body body)
    {
        return bodyIsType(body, UserDataType.PLAYER);
    }

    public static boolean bodyIsDeadly(Body body)
    {
        return bodyIsType(body, UserDataType.SAW) || bodyIsType(body, UserDataType.SPIKE_BLOCK);
    }

    public static boolean bodyIsDoor(Body body)
    {
        return bodyIsType(body, UserDataType.DOOR);
    }

    public static boolean bodyIsTerrain(Body body)
    {
        return bodyIsType(body, UserDataType.TERRAIN);
    }

    public static boolean bodyInBounds(Body body)
    {
        UserData userData = (UserData) body.getUserData();

        if(userData != null)
        {
            switch(userData.getUserDataType())
            {
                case PLAYER:
                case SAW:
                    return body.getPosition().x + userData.getWidth() / 2 > 0 && body.getPosition().y + userData.getHeight() / 2 > 0;

                default:
                    return true;
            }
        }

        return true;
    }

    public static boolean fixtureIsDeadly(Fixture fixture)
    {
        return fixtureIsType(fixture, UserDataType.DEADLY_FIXTURE);
    }

    public static boolean fixtureIsJumpSensor(Fixture fixture)
    {
        return fixtureIsType(fixture, UserDataType.PLAYER_SENSOR_FOOT);
    }

    public static boolean fixtureIsWallSensor(Fixture fixture)
    {
        return fixtureIsType(fixture, UserDataType.PLAYER_SENSOR_SIDE);
    }

    public static boolean fixtureIsPlayerCollisionFixture(Body body, Fixture fixture)
    {
        if(((UserData) body.getUserData()).getUserDataType() == UserDataType.PLAYER)
        {
            return fixtureIsType(fixture, UserDataType.COLLISION_FIXTURE) || fixtureIsType(fixture, UserDataType.PLAYER_SENSOR_FOOT);
        }

        return false;
    }

    private static boolean bodyIsType(Body body, UserDataType type)
    {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == type;
    }

    private static boolean fixtureIsType(Fixture fixture, UserDataType type)
    {
        UserData userData = (UserData) fixture.getUserData();

        return userData != null && userData.getUserDataType() == type;
    }
}