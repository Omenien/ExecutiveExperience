package com.eddie.executiveexperience;

import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.Entity.UserData.UserData;
import com.eddie.executiveexperience.Entity.UserData.UserDataType;

public class BodyUtils
{
    public static boolean bodyIsPlayer(Body body)
    {
        return bodyIsType(body, UserDataType.PLAYER);
    }

    public static boolean bodyIsEnemy(Body body)
    {
        return bodyIsType(body, UserDataType.ENEMY);
    }

    public static boolean bodyIsSaw(Body body)
    {
        return bodyIsType(body, UserDataType.SAW);
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
}