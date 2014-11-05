package com.eddie.executiveexperience.Entity.UserData;

import com.eddie.executiveexperience.Entity.EntityData;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public abstract class EntityUserData extends UserData
{
    protected EntityData entityData;
    protected AnimatedSprite animatedSprite;
    protected AnimatedBox2DSprite animatedBox2DSprite;

    public EntityUserData(GameStage gameStage, String name, float width, float height)
    {
        super(width, height);

        entityData = gameStage.getScreen().getAssets().get("assets/entities/" + name.toLowerCase() + "/" + name + ".json");
    }

    public AnimatedBox2DSprite getAnimatedBox2DSprite()
    {
        return animatedBox2DSprite;
    }

    public EntityData getEntityData()
    {
        return entityData;
    }
}
