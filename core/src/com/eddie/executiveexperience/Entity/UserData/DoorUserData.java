package com.eddie.executiveexperience.Entity.UserData;

public class DoorUserData extends UserData
{
    String newLevel;

    public DoorUserData(float width, float height, String newLevel)
    {
        super(width, height);

        this.newLevel = newLevel;

        userDataType = UserDataType.DOOR;
    }

    public String getNewLevel()
    {
        return newLevel;
    }
}
