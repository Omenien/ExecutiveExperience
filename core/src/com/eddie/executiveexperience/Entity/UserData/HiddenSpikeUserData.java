package com.eddie.executiveexperience.Entity.UserData;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.eddie.executiveexperience.Entity.HiddenSpike;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class HiddenSpikeUserData extends EntityUserData
{
    protected HiddenSpike.SpikeDirection spikeDirection;
    protected boolean isDeployed;
    private Fixture spriteFixture;

    public HiddenSpikeUserData(GameStage gameStage, float width, float height, Fixture spriteFixture, HiddenSpike.SpikeDirection spikeDirection)
    {
        super(width, height);

        this.spriteFixture = spriteFixture;

        spriteAnimationData = gameStage.getScreen().getAssets().get("assets/spike/Spike.json");
        animatedSprite = new AnimatedSprite(spriteAnimationData.getAnimation("deploy"));
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
        animatedBox2DSprite.setOrigin(animatedBox2DSprite.getWidth() / 2, animatedBox2DSprite.getHeight() / 2);
        animatedBox2DSprite.stop();

        isDeployed = false;

        this.spikeDirection = spikeDirection;

        userDataType = UserDataType.SPIKE_BLOCK;
    }

    public void deploy()
    {
        isDeployed = true;

        animatedBox2DSprite.play();
    }

    public Fixture getSpriteFixture()
    {
        return spriteFixture;
    }

    public HiddenSpike.SpikeDirection getSpikeDirection()
    {
        return spikeDirection;
    }
}
