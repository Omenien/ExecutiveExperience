package com.eddie.executiveexperience.Entity.UserData;

import com.badlogic.gdx.physics.box2d.Body;
import com.eddie.executiveexperience.GameStage;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import java.util.Vector;

public class MovingPlatformUserData extends EntityUserData
{
    private Vector<Body> passengers;

    public MovingPlatformUserData(GameStage gameStage, String name, float width, float height)
    {
        super(gameStage, name, width, height);

        animatedSprite = new AnimatedSprite(entityData.getAnimation("normal"));
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
        animatedBox2DSprite.setOrigin(animatedBox2DSprite.getWidth() / 2, animatedBox2DSprite.getHeight() / 2);
        animatedBox2DSprite.play();

        userDataType = UserDataType.MOVING_PLATFORM;

        passengers = new Vector<>();
    }


    public void addPassenger(Body passenger)
    {
        if (!passengers.contains(passenger))
        {
            passengers.add(passenger);
        }
    }

    public void removePassenger(Body passenger)
    {
        passengers.remove(passenger);
    }

    public Vector<Body> getPassengers()
    {
        return passengers;
    }
}
