package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class SawUserData extends EntityUserData
{
    private float degreesPerSecond;

    public SawUserData(GameStage gameStage, float width, float height)
    {
        super(gameStage, "Saw", width, height);

        animatedSprite = new AnimatedSprite(entityData.getAnimation("normal"));
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
        animatedBox2DSprite.setAutoUpdate(true);
        animatedBox2DSprite.setOrigin(animatedBox2DSprite.getWidth() / 2, animatedBox2DSprite.getHeight() / 2);
        animatedBox2DSprite.play();

        degreesPerSecond = Constants.SAW_DEGREES_PER_SECOND;

        userDataType = UserDataType.SAW;
    }

    public float getDegreesPerSecond()
    {
        return degreesPerSecond;
    }

    public void setDegreesPerSecond(float degreesPerSecond)
    {
        this.degreesPerSecond = degreesPerSecond;
    }
}
