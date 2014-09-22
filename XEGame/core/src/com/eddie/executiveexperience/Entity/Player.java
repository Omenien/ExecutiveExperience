package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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

    private int numFootContacts;

    public Player(Body body)
    {
        super(body);

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

        if(body.getAngle() < -0.25)
        {
            body.setTransform(body.getPosition().x, body.getPosition().y, -0.25f);
        }
        else if(body.getAngle() > 0.25)
        {
            body.setTransform(body.getPosition().x, body.getPosition().y, 0.25f);
        }

        System.out.println("Player Angle: " + body.getAngle());
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

        if(Gdx.input.isKeyPressed(Env.playerMoveLeft))
        {
            if(velocity.x > -MAX_VELOCITY_X)
            {
                body.applyLinearImpulse(-2f, 0f, position.x, position.y, true);
            }
        }
        else if(Gdx.input.isKeyPressed(Env.playerMoveRight))
        {
            if(velocity.x < MAX_VELOCITY_X)
            {
                body.applyLinearImpulse(2f, 0f, position.x, position.y, true);
            }
        }
        else
        {
            if(velocity.x > 0.1f)
            {
                body.setLinearVelocity(velocity.x * 0.75f, velocity.y);
            }
            else
            {
                body.setLinearVelocity(0.0f, velocity.y);
            }
        }

        if(grounded && !jump && Gdx.input.isKeyPressed(Env.playerJump))
        {
            jump = true;
        }
    }

    public void incrementFootContacts()
    {
        numFootContacts++;
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