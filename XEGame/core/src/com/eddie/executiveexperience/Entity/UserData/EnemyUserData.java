package com.eddie.executiveexperience.Entity.UserData;

import com.badlogic.gdx.math.Vector2;
import com.eddie.executiveexperience.Constants;

public class EnemyUserData extends UserData
{
    private Vector2 velocity;

    public EnemyUserData(float width, float height)
    {
        super(width, height);
        userDataType = UserDataType.ENEMY;
        velocity = Constants.ENEMY_LINEAR_VELOCITY;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }
}
