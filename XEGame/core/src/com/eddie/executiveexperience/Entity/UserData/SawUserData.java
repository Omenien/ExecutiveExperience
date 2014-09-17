package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.utils.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.utils.libgdx.graphics.AnimatedSprite;

public class SawUserData extends EntityUserData
{
    private float degreesPerSecond;

    public SawUserData(GameStage gameStage, float width, float height)
    {
        super(width, height);

        spriteAnimationData = gameStage.getAssetManager().get("assets/saw/Saw.json");
        animatedSprite = new AnimatedSprite(spriteAnimationData.getAnimation("normal"));
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
