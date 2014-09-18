package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

import java.util.List;

public class Player extends GameActor
{
    private static final float MAX_VELOCITY_X = 7f;

    public boolean jump;

    private long lastGroundTime = 0;

    private boolean jumping;
    private boolean hit;

    public Player(Body body)
    {
        super(body);

        jump = false;

        jumping = false;
        hit = false;
    }

    @Override
    public void act(float delta)
    {
        if(body == null)
        {
            return;
        }

        super.act(delta);

        boolean grounded = isGrounded(delta);

        if(grounded)
        {
            lastGroundTime = System.nanoTime();
        }
        else
        {
            if(System.nanoTime() - lastGroundTime > 100000000)
            {
                grounded = true;
            }
        }

        if(!grounded)
        {
            getUserData().getBodyFixture().setFriction(0.0f);
        }
        else
        {
            getUserData().getBodyFixture().setFriction(0.2f);
        }

        if(jump)
        {
            jump = false;

            if(grounded)
            {
                jump();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        getUserData().getAnimatedBox2DSprite().draw(batch, getUserData().getBodyFixture());
    }

    public void jump()
    {
        if(!(jumping || hit))
        {
            float angle = MathUtils.radiansToDegrees * body.getAngle();

            float jumpingImpulseMagnitude = getUserData().getJumpingImpulseMagnitude();

            float xComponent = (float) (Math.sin(angle * Math.PI / 180));
            float yComponent = (float) (Math.cos(angle * Math.PI / 180));

            Vector2 jumpingLinearImpulse = new Vector2(xComponent, yComponent);
            jumpingLinearImpulse.scl(jumpingImpulseMagnitude);

            body.applyLinearImpulse(jumpingLinearImpulse, body.getWorldCenter(), true);

            jumping = true;
        }
    }

    public void hit()
    {
        body.applyAngularImpulse(getUserData().getHitAngularImpulse(), true);

        hit = true;
    }

    public void landed()
    {
        if(jumping)
        {
            jumping = false;
        }

        if(hit)
        {
            hit = false;
        }
    }

    public boolean isHit()
    {
        return hit;
    }

    private boolean isGrounded(float delta)
    {
//        groundedPlatform = null;
        Array<Contact> contactList = ((GameStage) getStage()).getWorld().getContactList();

        for(int i = 0; i < contactList.size; i++)
        {
            Contact contact = contactList.get(i);
            if(contact.isTouching() && (contact.getFixtureA() == getUserData().getPhysicsSensor() || contact.getFixtureB() == getUserData().getPhysicsSensor()))
            {
                Vector2 pos = body.getPosition();
                WorldManifold manifold = contact.getWorldManifold();
                boolean below = true;
                for(int j = 0; j < manifold.getNumberOfContactPoints(); j++)
                {
                    below &= (manifold.getPoints()[j].y < pos.y - 1.5f);
                }

                if(below)
                {
                    /*if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("p"))
                    {
                        groundedPlatform = (MovingPlatform) contact.getFixtureA().getBody().getUserData();
                    }

                    if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("p"))
                    {
                        groundedPlatform = (MovingPlatform) contact.getFixtureB().getBody().getUserData();
                    }*/

                    return true;
                }

                return false;
            }
        }
        return false;
    }

    @Override
    public PlayerUserData getUserData()
    {
        return (PlayerUserData) userData;
    }

    public void handleInput()
    {
        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        if(velocity.x < -MAX_VELOCITY_X)
        {
            body.setLinearVelocity(-MAX_VELOCITY_X, velocity.y);
        }
        else if(velocity.x > MAX_VELOCITY_X)
        {
            body.setLinearVelocity(MAX_VELOCITY_X, velocity.y);
        }

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
    }

    public Body getBody()
    {
        return body;
    }
}