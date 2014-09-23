package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.Entity.UserData.FootSensorUserData;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

public class Player extends GameActor
{
    protected static final float MAX_VELOCITY_X = 12f;
    private static final float MAX_JUMP_EXTEND_TIME = 0.15f;

    protected boolean jump;
    protected boolean extendJump;

    protected int jumpTimeout;
    protected float jumpExtendTime;

    protected int numFootContacts;

    public Player(GameStage gameStage, float x, float y, MapProperties objectProperties)
    {
        super(gameStage);

        Fixture playerSensorFixture;
        Fixture playerBodyFixture;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x + Constants.PLAYER_WIDTH / 2, y + Constants.PLAYER_HEIGHT / 2));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2);

        PolygonShape sensorShape = new PolygonShape();
        float radius = Constants.PLAYER_WIDTH * 0.75f;
        Vector2[] vertices = new Vector2[8];
        vertices[0] = new Vector2(0, 0);
        for(int i = 0; i < 7; i++)
        {
            float angle = (i / 6.0f * 90f - 135f) * MathUtils.degreesToRadians;
            vertices[i + 1] = new Vector2(radius * MathUtils.cos(angle), radius * MathUtils.sin(angle));
        }
        sensorShape.set(vertices);

        Body body = gameStage.getWorld().createBody(bodyDef);
        playerBodyFixture = body.createFixture(shape, Constants.PLAYER_DENSITY);
        playerSensorFixture = body.createFixture(sensorShape, 0);
        playerSensorFixture.setSensor(true);
        playerSensorFixture.setUserData(new FootSensorUserData());

        body.resetMassData();
        body.setGravityScale(Constants.PLAYER_GRAVITY_SCALE);

        body.setUserData(new PlayerUserData(gameStage, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, playerBodyFixture, playerSensorFixture));

        body.setFixedRotation(true);

        body.setBullet(true);

        sensorShape.dispose();
        shape.dispose();

        setBody(body);

        gameStage.setPlayer(this);

        jump = false;
        extendJump = false;

        jumpTimeout = 0;
        jumpExtendTime = 0;

        numFootContacts = 0;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        boolean grounded = numFootContacts > 0;

        if(grounded)
        {
            getUserData().getBodyFixture().setFriction(0.3f);

            jumpExtendTime = 0;
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

        if(extendJump)
        {
            if(jumpExtendTime < MAX_JUMP_EXTEND_TIME)
            {
                body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 1.05f);

                jumpExtendTime += delta;

                System.out.println(jumpExtendTime);
            }
        }

        jump = false;
        extendJump = false;
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
                if(grounded)
                {
                    body.applyLinearImpulse(-2f, 0f, position.x, position.y, true);
                }
                else
                {
                    body.applyLinearImpulse(-0.25f, 0f, position.x, position.y, true);
                }
            }
        }
        else if(Gdx.input.isKeyPressed(Env.playerMoveRight))
        {
            if(velocity.x < MAX_VELOCITY_X)
            {
                if(grounded)
                {
                    body.applyLinearImpulse(2f, 0f, position.x, position.y, true);
                }
                else
                {
                    body.applyLinearImpulse(0.25f, 0f, position.x, position.y, true);
                }
            }
        }
        else
        {
            if(velocity.x > 0.2f)
            {
                body.setLinearVelocity(velocity.x * 0.85f, velocity.y);
            }
            else
            {
                body.setLinearVelocity(0.0f, velocity.y);
            }
        }

        if(Gdx.input.isKeyPressed(Env.playerJump))
        {
            if(grounded && !jump)
            {
                jump = true;
            }
            else if(!grounded && body.getLinearVelocity().y > 0)
            {
                extendJump = true;
            }
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