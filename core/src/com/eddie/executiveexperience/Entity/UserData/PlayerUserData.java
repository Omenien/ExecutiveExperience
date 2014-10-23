package com.eddie.executiveexperience.Entity.UserData;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class PlayerUserData extends EntityUserData
{
    private Fixture playerSpriteFixture;

    private int jumpingImpulseMagnitude;

    public PlayerUserData(GameStage gameStage, float width, float height, Fixture playerSpriteFixture)
    {
        super(width, height);

        this.playerSpriteFixture = playerSpriteFixture;

        jumpingImpulseMagnitude = Constants.PLAYER_JUMPING_IMPULSE_MAGNITUDE;

        spriteAnimationData = gameStage.getScreen().getAssets().get("assets/player/Player.json");
        animatedSprite = new AnimatedSprite(spriteAnimationData.getAnimation("stand"));
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
        animatedBox2DSprite.setOrigin(animatedBox2DSprite.getWidth() / 2, animatedBox2DSprite.getHeight() / 2);
        animatedBox2DSprite.play();

        userDataType = UserDataType.PLAYER;
    }

    public int getJumpingImpulseMagnitude()
    {
        return jumpingImpulseMagnitude;
    }

    public void setJumpingImpulseMagnitude(int jumpingImpulseMagnitude)
    {
        this.jumpingImpulseMagnitude = jumpingImpulseMagnitude;
    }

    public Fixture getSpriteFixture()
    {
        return playerSpriteFixture;
    }
}
