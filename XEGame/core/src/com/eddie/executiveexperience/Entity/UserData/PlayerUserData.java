package com.eddie.executiveexperience.Entity.UserData;

import com.badlogic.gdx.math.Vector2;
import com.eddie.executiveexperience.Constants;

public class PlayerUserData extends UserData
{
    private Vector2 jumpingLinearImpulse;

    public PlayerUserData(float width, float height)
    {
        super(width, height);
        jumpingLinearImpulse = Constants.PLAYER_JUMPING_LINEAR_IMPULSE;
        userDataType = UserDataType.PLAYER;
    }

    public Vector2 getJumpingLinearImpulse()
    {
        return jumpingLinearImpulse;
    }

    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse)
    {
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }

    public float getHitAngularImpulse()
    {
        return Constants.PLAYER_HIT_ANGULAR_IMPULSE;
    }
}
