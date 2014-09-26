package com.eddie.executiveexperience.Entity.UserData;

public class WallSensorUserData extends UserData
{
    protected Side side;

    public WallSensorUserData(Side side)
    {
        super();

        this.side = side;

        userDataType = UserDataType.PLAYER_SENSOR_SIDE;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

    public enum Side
    {
        LEFT,
        RIGHT
    }
}
