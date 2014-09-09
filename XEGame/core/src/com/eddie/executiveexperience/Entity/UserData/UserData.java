package com.eddie.executiveexperience.Entity.UserData;

public abstract class UserData
{
    UserDataType userDataType;

    protected float width;
    protected float height;

    public UserData()
    {
    }

    public UserData(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public UserDataType getUserDataType()
    {
        return userDataType;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }
}