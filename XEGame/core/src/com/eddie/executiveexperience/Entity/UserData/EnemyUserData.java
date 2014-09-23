package com.eddie.executiveexperience.Entity.UserData;

public class EnemyUserData extends UserData
{
    public EnemyUserData(float width, float height)
    {
        super(width, height);
        userDataType = UserDataType.ENEMY;
    }
}
