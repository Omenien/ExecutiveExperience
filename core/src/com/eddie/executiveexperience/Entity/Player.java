package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.eddie.executiveexperience.Entity.UserData.CollisionFixtureUserData;
import com.eddie.executiveexperience.Entity.UserData.FootSensorUserData;
import com.eddie.executiveexperience.Entity.UserData.PlayerUserData;
import com.eddie.executiveexperience.Entity.UserData.WallSensorUserData;
import com.eddie.executiveexperience.GameStage;
import com.eddie.executiveexperience.Utils.Constants;
import com.eddie.executiveexperience.Utils.Env;
import com.eddie.executiveexperience.Utils.InputManager;
import com.eddie.executiveexperience.Utils.XBoxGamepad;

public class Player extends GameActor
{
    protected static final float MAX_VELOCITY_X = 11f;
    private static final float MAX_JUMP_EXTEND_TIME = 0.3f;
    private static final float JUMP_TIME_BEFORE_EXTENSION = 0.05f;

    protected PlayerState playerState;
    protected Direction playerDirection;

    protected boolean jump;
    protected boolean wallJump;
    protected boolean extendJump;

    protected int jumpTimeout;
    protected float timeSinceJump;
    protected float jumpExtendTime;

    protected int deathTimeout;

    protected int numFootContacts;
    protected int leftWallContacts;
    protected int rightWallContacts;

    protected Fixture lowerCollisionFixture;

    protected boolean forcingWalk;
    protected boolean grounded;

    public Player(GameStage gameStage, float x, float y, MapObject mapObject)
    {
        super(gameStage);

        playerState = PlayerState.STANDING;
        playerDirection = Direction.RIGHT;

        Fixture playerWallSensorLeftFixture;
        Fixture playerWallSensorRightFixture;
        Fixture playerSpriteFixture;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x + Constants.PLAYER_WIDTH / 2f, y + Constants.PLAYER_HEIGHT / 2));

        PolygonShape spriteShape = new PolygonShape();
        spriteShape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2);

        PolygonShape collisionRect = new PolygonShape();
        collisionRect.setAsBox(Constants.PLAYER_WIDTH * 0.25f, Constants.PLAYER_HEIGHT / 2 - (Constants.PLAYER_WIDTH * 0.25f));

        CircleShape upperCollisionCircle = new CircleShape();
        upperCollisionCircle.setPosition(new Vector2(0f, Constants.PLAYER_HEIGHT / 2 - (Constants.PLAYER_WIDTH * 0.25f)));
        upperCollisionCircle.setRadius(Constants.PLAYER_WIDTH * 0.25f);

        CircleShape lowerCollisionCircle = new CircleShape();
        lowerCollisionCircle.setPosition(new Vector2(0f, -Constants.PLAYER_HEIGHT / 2 + (Constants.PLAYER_WIDTH * 0.25f)));
        lowerCollisionCircle.setRadius(Constants.PLAYER_WIDTH * 0.25f);

        Body body = gameStage.getWorld().createBody(bodyDef);

        playerSpriteFixture = body.createFixture(spriteShape, Constants.ENTITY_DENSITY);
        playerSpriteFixture.setSensor(true);

        Fixture collisionRectFixture = body.createFixture(collisionRect, 0);
        collisionRectFixture.setUserData(new CollisionFixtureUserData());

        Fixture upperCollisionFixture = body.createFixture(upperCollisionCircle, 0);
        upperCollisionFixture.setUserData(new CollisionFixtureUserData());

        lowerCollisionFixture = body.createFixture(lowerCollisionCircle, 0);
        lowerCollisionFixture.setUserData(new FootSensorUserData());
        lowerCollisionFixture.setFriction(1.0f);

        float sensorRadius = Constants.PLAYER_WIDTH * 0.7f;
        PolygonShape wallSensorShape = new PolygonShape();
        Vector2[] vertices = new Vector2[8];
        vertices[0] = new Vector2(0, 0);
        for(int i = 0; i < 7; i++)
        {
            float angle = (i / 6.0f * 90f - 225f) * MathUtils.degreesToRadians;
            vertices[i + 1] = new Vector2(sensorRadius * MathUtils.cos(angle), sensorRadius * MathUtils.sin(angle));
        }
        wallSensorShape.set(vertices);
        playerWallSensorLeftFixture = body.createFixture(wallSensorShape, 0);
        playerWallSensorLeftFixture.setSensor(true);
        playerWallSensorLeftFixture.setUserData(new WallSensorUserData(WallSensorUserData.Side.LEFT));

        wallSensorShape = new PolygonShape();
        vertices = new Vector2[8];
        vertices[0] = new Vector2(0, 0);
        for(int i = 0; i < 7; i++)
        {
            float angle = (i / 6.0f * 90f - 45f) * MathUtils.degreesToRadians;
            vertices[i + 1] = new Vector2(sensorRadius * MathUtils.cos(angle), sensorRadius * MathUtils.sin(angle));
        }
        wallSensorShape.set(vertices);
        playerWallSensorRightFixture = body.createFixture(wallSensorShape, 0);
        playerWallSensorRightFixture.setSensor(true);
        playerWallSensorRightFixture.setUserData(new WallSensorUserData(WallSensorUserData.Side.RIGHT));

        body.resetMassData();
        body.setGravityScale(Constants.PLAYER_GRAVITY_SCALE);

        body.setUserData(new PlayerUserData(gameStage, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, playerSpriteFixture));

        body.setFixedRotation(true);

        body.setBullet(true);

        spriteShape.dispose();
        wallSensorShape.dispose();
        collisionRect.dispose();
        upperCollisionCircle.dispose();
        lowerCollisionCircle.dispose();

        setBody(body);

        gameStage.setPlayer(this);

        jump = false;
        wallJump = false;
        extendJump = false;

        forcingWalk = false;
        grounded = false;

        jumpTimeout = 0;
        timeSinceJump = 0;
        jumpExtendTime = 0;

        numFootContacts = 0;
        leftWallContacts = 0;
        rightWallContacts = 0;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if(playerState == PlayerState.DEADING)
        {
            body.setLinearVelocity(new Vector2(0, 0));

            deathTimeout++;

            return;
        }

        timeSinceJump += delta;

        grounded = numFootContacts > 0 || body.getLinearVelocity().y == 0;

        if(grounded)
        {
            jumpExtendTime = 0;

            if(playerState == PlayerState.JUMPING && timeSinceJump > 0.2f)
            {
                playerState = PlayerState.STANDING;
            }
        }

        if(forcingWalk)
        {
            lowerCollisionFixture.setFriction(0.0f);
        }
        else
        {
            lowerCollisionFixture.setFriction(1.0f);
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

        if(wallJump)
        {
            if(jumpTimeout == 0)
            {
                wallJump();
            }
        }

        if(extendJump)
        {
            if(jumpExtendTime < MAX_JUMP_EXTEND_TIME && timeSinceJump > JUMP_TIME_BEFORE_EXTENSION)
            {
                body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 1.05f);

                jumpExtendTime += delta;
            }
        }

        if(playerState != PlayerState.JUMPING)
        {
            if(Math.abs(body.getLinearVelocity().x) > 0.2f && forcingWalk)
            {
                playerState = PlayerState.WALKING;
            }
            else
            {
                playerState = PlayerState.STANDING;
            }
        }

        if(body.getLinearVelocity().x > 0)
        {
            playerDirection = Direction.RIGHT;
        }
        else if(body.getLinearVelocity().x < 0)
        {
            playerDirection = Direction.LEFT;
        }

        jump = false;
        wallJump = false;
        extendJump = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(playerState == PlayerState.JUMPING)
        {
            if(playerDirection == Direction.RIGHT)
            {
                getUserData().getAnimatedBox2DSprite().setAnimation(getUserData().getEntityData().getAnimation("jump_right"));
            }
            else
            {
                getUserData().getAnimatedBox2DSprite().setAnimation(getUserData().getEntityData().getAnimation("jump_left"));
            }
        }
        else if(playerState == PlayerState.WALKING && numFootContacts > 0)
        {
            if(playerDirection == Direction.RIGHT)
            {
                getUserData().getAnimatedBox2DSprite().setAnimation(getUserData().getEntityData().getAnimation("walk_right"));
            }
            else
            {
                getUserData().getAnimatedBox2DSprite().setAnimation(getUserData().getEntityData().getAnimation("walk_left"));
            }
        }
        else
        {
            getUserData().getAnimatedBox2DSprite().setAnimation(getUserData().getEntityData().getAnimation("stand"));
        }

        getUserData().getAnimatedBox2DSprite().draw(batch, getUserData().getSpriteFixture());
    }

    public void jump()
    {
        jump = false;
        wallJump = false;
        extendJump = false;

        float angle = MathUtils.radiansToDegrees * body.getAngle();

        float jumpingImpulseMagnitude = getUserData().getJumpingImpulseMagnitude();

        float xComponent = (float) (Math.sin(angle * Math.PI / 180));
        float yComponent = (float) (Math.cos(angle * Math.PI / 180));

        Vector2 jumpingLinearImpulse = new Vector2(xComponent, yComponent);
        jumpingLinearImpulse.scl(jumpingImpulseMagnitude);

        body.applyLinearImpulse(jumpingLinearImpulse, body.getWorldCenter(), true);

        timeSinceJump = 0;

        jumpTimeout = 40;

        playerState = PlayerState.JUMPING;
    }

    private void wallJump()
    {
        jump = false;
        wallJump = false;
        extendJump = false;

        float jumpingImpulseMagnitude = getUserData().getJumpingImpulseMagnitude();

        float xComponent = 0.8f;
        float yComponent = 1f;

        if(leftWallContacts > 0 && rightWallContacts == 0)
        {
            xComponent *= 1.0f;
        }
        else if(leftWallContacts == 0 && rightWallContacts > 0)
        {
            xComponent *= -1.0f;
        }

        Vector2 jumpingLinearImpulse = new Vector2(xComponent, yComponent);
        jumpingLinearImpulse.scl(jumpingImpulseMagnitude);

        body.applyLinearImpulse(jumpingLinearImpulse, body.getWorldCenter(), true);

        timeSinceJump = 0;

        jumpTimeout = 20;

        playerState = PlayerState.JUMPING;
    }

    @Override
    public PlayerUserData getUserData()
    {
        return (PlayerUserData) userData;
    }

    public void incrementFootContacts()
    {
        numFootContacts++;
    }

    public void decrementFootContacts()
    {
        numFootContacts--;
    }

    public void incrementLeftWallContacts()
    {
        leftWallContacts++;
    }

    public void incrementRightWallContacts()
    {
        rightWallContacts++;
    }

    public void decrementLeftWallContacts()
    {
        leftWallContacts--;
    }

    public void decrementRightWallContacts()
    {
        rightWallContacts--;
    }

    public boolean isDead()
    {
        return playerState == PlayerState.DEADING && deathTimeout >= 5;
    }

    public void die()
    {
        playerState = PlayerState.DEADING;
    }

    public Body getBody()
    {
        return body;
    }

    public void processInput(InputManager inputManager)
    {
        if(playerState == PlayerState.DEADING)
        {
            return;
        }

        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        forcingWalk = true;

        int playerMoveLeftPressed = inputManager.isKeyDown(Env.playerMoveLeft) ? 1 : 0;
        int playerMoveRightPressed = inputManager.isKeyDown(Env.playerMoveRight) ? 1 : 0;

        float joystickX = inputManager.hasController() ? inputManager.getController().getAxis(XBoxGamepad.AXIS_LEFT_X) : 0.0f;

        float impulseX = (playerMoveLeftPressed * -1.5f) + (playerMoveRightPressed * 1.5f) + (joystickX * 1.5f);
        impulseX = impulseX > 1.5f ? 1.5f : impulseX < -1.5f ? -1.5f : impulseX;

        if(!grounded)
        {
            impulseX /= 5;
        }

        if(impulseX < -0f)
        {
            if(body.getLinearVelocity().x > -MAX_VELOCITY_X)
            {
                body.applyLinearImpulse(impulseX, 0.1f, position.x, position.y, true);
            }
        }
        else if(impulseX > 0f)
        {
            if(body.getLinearVelocity().x < MAX_VELOCITY_X)
            {
                body.applyLinearImpulse(impulseX, 0.1f, position.x, position.y, true);
            }
        }
        else
        {
            body.setLinearVelocity(velocity.x * 0.9f, velocity.y);

            forcingWalk = false;
        }

        if(inputManager.isKeyDown(Env.playerJumpKey) || inputManager.isButtonPressed(Env.playerJumpButton, false))
        {
            if(numFootContacts > 0 && !jump && !(leftWallContacts > 0 || rightWallContacts > 0))
            {
                jump = true;
            }
            else if(numFootContacts == 0 && body.getLinearVelocity().y > 0)
            {
                extendJump = true;
            }
            else if(numFootContacts == 0 && (leftWallContacts > 0 || rightWallContacts > 0))
            {
                wallJump = true;
            }
        }
    }

    public float getFriction()
    {
        return lowerCollisionFixture.getFriction();
    }

    protected enum PlayerState
    {
        DEADING,
        STANDING,
        WALKING,
        JUMPING
    }

    protected enum Direction
    {
        LEFT,
        RIGHT
    }
}