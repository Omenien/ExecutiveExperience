package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.eddie.executiveexperience.Utils.Env;

public class EntityData
{
    private Logger logger;

    protected Texture texture;
    protected float frameDuration;
    protected ObjectMap<String, Animation> animations;
    protected Animation defaultAnimation;

    public EntityData()
    {
        logger = new Logger("EntityData", Env.debugLevel);

        animations = new ObjectMap<>();
        defaultAnimation = null;
        frameDuration = 0.0f;
        texture = null;
    }

    public Animation getAnimation(String animationName)
    {
        Animation animation = animations.get(animationName);

        if(animation == null)
        {
            logger.info("Animation " + animationName + " not found for " + getClass().getSimpleName() + ", returning default.");

            return defaultAnimation;
        }

        return animation;
    }

    public Texture getTexture()
    {
        return texture;
    }
}
