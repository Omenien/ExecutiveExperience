package com.eddie.executiveexperience.Graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.eddie.executiveexperience.Env;

/**
 * @author Edward Jones
 * @class SpriteAnimationData
 * @date 09/11/2014
 * @brief Holds Graphics frame sequences and reads them from JSON files and a spritesheet-like texture. Original by David Saltares M&aacute;rquez.
 */
public class SpriteAnimationData
{
    private Logger logger;

    Texture texture;
    float frameDuration;
    ObjectMap<String, Animation> animations;
    Animation defaultAnimation;

    public SpriteAnimationData()
    {
        logger = new Logger("Animation", Env.debugLevel);
        animations = new ObjectMap<>();
        defaultAnimation = null;
        frameDuration = 0.0f;
        texture = null;
    }

    /**
     * @param animationName name of the desired Graphics
     * @return Graphics object containing the sequence of frames, null if not found
     */
    public Animation getAnimation(String animationName)
    {
        Animation animation = animations.get(animationName);

        if(animation == null)
        {
            logger.info("Animation: " + animationName + " not found, returning default.");

            return defaultAnimation;
        }

        return animation;
    }

    public Texture getTexture()
    {
        return texture;
    }
}
