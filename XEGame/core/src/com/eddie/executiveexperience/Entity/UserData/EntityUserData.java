package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.Animation.SpriteAnimationData;
import net.dermetfan.utils.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.utils.libgdx.graphics.AnimatedSprite;

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
}
