package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.GameStage;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class SawLauncherUserData extends EntityUserData
{
    public SawLauncherUserData(GameStage gameStage, float width, float height)
    {
        super(gameStage, "Spike", width, height);

        animatedSprite = new AnimatedSprite(entityData.getAnimation("normal"));
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
        animatedBox2DSprite.setOrigin(animatedBox2DSprite.getWidth() / 2, animatedBox2DSprite.getHeight() / 2);
        animatedBox2DSprite.stop();

        userDataType = UserDataType.SAW_LAUNCHER;
    }
}
