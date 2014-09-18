package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Logger;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameActor;

public class Player extends GameActor
{
    private static final String TAG = "Player";

    private static final float MAX_VELOCITY_X = 7f;
    private static final float MAX_VELOCITY_Y = 15f;

    public boolean jump;
    private int jumpTimeout;
    private Logger logger;

    private int numFootContacts;

    public Player(Body body)
    {
        super(body);

        logger = new Logger(TAG, Env.debugLevel);

        jump = false;

        jumpTimeout = 0;
        numFootContacts = 0;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        boolean grounded = numFootContacts > 0;

        if(grounded)
        {
            getUserData().getBodyFixture().setFriction(0.2f);
        }
        else
        {
            getUserData().getBodyFixture().setFriction(0.0f);
        }

        handleInput(grounded);

        Vector2 velocity = body.getLinearVelocity();

        if(velocity.x < -MAX_VELOCITY_X)
        {
            body.setLinearVelocity(-MAX_VELOCITY_X, velocity.y);
        }
        else if(velocity.x > MAX_VELOCITY_X)
        {
            body.setLinearVelocity(MAX_VELOCITY_X, velocity.y);
        }

        if(velocity.y < -MAX_VELOCITY_Y)
        {
            body.setLinearVelocity(velocity.x, -MAX_VELOCITY_Y);
        }
        else if(velocity.x > MAX_VELOCITY_X)
        {
            body.setLinearVelocity(velocity.x, MAX_VELOCITY_X);
        }

        if(jumpTimeout > 0)
        {
            jumpTimeout--;
        }

        if(jump)
        {
            if(grounded && jumpTimeout == 0)
            {
                jump();
            }
        }

        jump = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        getUserData().getAnimatedBox2DSprite().draw(batch, getUserData().getBodyFixture());
    }

    public void jump()
    {
        float angle = MathUtils.radiansToDegrees * body.getAngle();

        float jumpingImpulseMagnitude = getUserData().getJumpingImpulseMagnitude();

        float xComponent = (float) (Math.sin(angle * Math.PI / 180));
        float yComponent = (float) (Math.cos(angle * Math.PI / 180));

        Vector2 jumpingLinearImpulse = new Vector2(xComponent, yComponent);
        jumpingLinearImpulse.scl(jumpingImpulseMagnitude);

        body.applyLinearImpulse(jumpingLinearImpulse, body.getWorldCenter(), true);

        jumpTimeout = 40;
    }

    @Override
    public PlayerUserData getUserData()
    {
        return (PlayerUserData) userData;
    }

    public void handleInput(boolean grounded)
    {
        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        if(Gdx.input.isKeyPressed(Env.playerMoveLeft) && velocity.x > -MAX_VELOCITY_X)
        {
            body.applyLinearImpulse(-2f, 0f, position.x, position.y, true);
        }
        else if(Gdx.input.isKeyPressed(Env.playerMoveRight) && velocity.x < MAX_VELOCITY_X)
        {
            body.applyLinearImpulse(2f, 0f, position.x, position.y, true);
        }
        else
        {
            body.setLinearVelocity(velocity.x * 0.9f, velocity.y);
        }

        if(grounded && !jump && Gdx.input.isKeyPressed(Env.playerJump))
        {
            jump = true;
        }
    }

    public void incrementFootContacts()
    {
        numFootContacts++;

        Vector2 linearVelocity = body.getLinearVelocity();

        body.setLinearVelocity(linearVelocity.x, 0.0f);
    }

    public void decrementFootContacts()
    {
        numFootContacts--;
    }

    public Body getBody()
    {
        return body;
    }
}