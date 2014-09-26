package com.eddie.executiveexperience;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.eddie.executiveexperience.Entity.UserData.UserData;
import com.eddie.executiveexperience.Entity.UserData.UserDataType;

public class BodyUtils
{
    public static boolean bodyIsPlayer(Body body)
    {
        return bodyIsType(body, UserDataType.PLAYER);
    }

    public static boolean bodyIsSaw(Body body)
    {
        return bodyIsType(body, UserDataType.SAW);
    }

    public static boolean bodyIsDoor(Body body)
    {
        return bodyIsType(body, UserDataType.DOOR);
    }

    public static boolean bodyIsTerrain(Body body)
    {
        return bodyIsType(body, UserDataType.TERRAIN);
    }

    private static boolean bodyIsType(Body body, UserDataType type)
    {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == type;
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
            }
        }

        return true;
    }

    public static boolean fixtureIsJumpSensor(Fixture fixture)
    {
        UserData userData = (UserData) fixture.getUserData();

        if(userData != null)
        {
            UserDataType userDataType = userData.getUserDataType();

            if(userData.getUserDataType() == UserDataType.PLAYER_SENSOR_FOOT)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean fixtureIsWallSensor(Fixture fixture)
    {
        UserData userData = (UserData) fixture.getUserData();

        if(userData != null)
        {
            UserDataType userDataType = userData.getUserDataType();

            if(userData.getUserDataType() == UserDataType.PLAYER_SENSOR_SIDE)
            {
                return true;
            }
        }

        return false;
    }
}