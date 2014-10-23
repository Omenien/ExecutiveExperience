package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.Graphics.SpriteAnimationData;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public abstract class EntityUserData extends UserData
{
    protected SpriteAnimationData spriteAnimationData;
    protected AnimatedSprite animatedSprite;
    protected AnimatedBox2DSprite animatedBox2DSprite;

    public EntityUserData(float width, float height)
    {
        super(width, height);
    }

    public AnimatedBox2DSprite getAnimatedBox2DSprite()
    {
        return animatedBox2DSprite;
    }

    public SpriteAnimationData getSpriteAnimationData()
    {
        return spriteAnimationData;
    }
}
