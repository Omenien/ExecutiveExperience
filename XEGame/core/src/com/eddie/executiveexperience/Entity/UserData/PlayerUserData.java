package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.utils.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.utils.libgdx.graphics.AnimatedSprite;

public class PlayerUserData extends EntityUserData
{
    private int jumpingImpulseMagnitude;

    public PlayerUserData(GameStage gameStage, float width, float height)
    {
        super(width, height);

        jumpingImpulseMagnitude = Constants.PLAYER_JUMPING_IMPULSE_MAGNITUDE;

        spriteAnimationData = gameStage.getAssetManager().get("player/PlayerAnimation.json");
        animatedSprite = new AnimatedSprite(spriteAnimationData.getAnimation("stand"));
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);

        userDataType = UserDataType.PLAYER;
    }

    public void setJumpingImpulseMagnitude(int jumpingImpulseMagnitude)
    {
        this.jumpingImpulseMagnitude = jumpingImpulseMagnitude;
    }

    public int getJumpingImpulseMagnitude()
    {
        return jumpingImpulseMagnitude;
    }

    public float getHitAngularImpulse()
    {
        return Constants.PLAYER_HIT_ANGULAR_IMPULSE;
    }
}
