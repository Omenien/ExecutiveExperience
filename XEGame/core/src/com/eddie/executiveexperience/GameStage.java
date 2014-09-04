package com.eddie.executiveexperience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.eddie.executiveexperience.Entity.Enemy;
import com.eddie.executiveexperience.Entity.Player;

public class GameStage extends Stage implements ContactListener
{
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private Ground ground;
    private Player player;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage()
    {
        setupWorld();
        renderer = new Box2DDebugRenderer();
        setupCamera();
    }

    private void setupWorld()
    {
        WorldUtils.createWorld();
        WorldUtils.getWorld().setContactListener(this);
        setupGround();
        setupPlayer();
        createEnemy();
    }

    private void setupGround()
    {
        ground = new Ground(WorldUtils.createGround());
        addActor(ground);
    }

    private void setupPlayer()
    {
        player = new Player(WorldUtils.createPlayer());
        addActor(player);
    }

    private void setupCamera()
    {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0.0f);
        camera.update();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        Array<Body> bodies = new Array(WorldUtils.getWorld().getBodyCount());
        WorldUtils.getWorld().getBodies(bodies);

        for(Body body : bodies)
        {
            update(body);
        }

        accumulator += delta;

        while(accumulator >= delta)
        {
            WorldUtils.getWorld().step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    private void update(Body body)
    {
        if(!BodyUtils.bodyInBounds(body))
        {
            if(BodyUtils.bodyIsEnemy(body) && !player.isHit())
            {
                createEnemy();
            }

            WorldUtils.getWorld().destroyBody(body);
        }
    }

    private void createEnemy()
    {
        Enemy enemy = new Enemy(WorldUtils.createEnemy());
        addActor(enemy);
    }

    @Override
    public boolean keyDown(int keyCode)
    {
        Gdx.app.log("GameStage", "[Input Listener] Key Down - " + keyCode);

        switch(keyCode)
        {
            case Input.Keys.SPACE:
                player.jump();
                break;
        }

        return false;
    }

    @Override
    public void draw()
    {
        super.draw();

        renderer.render(WorldUtils.getWorld(), camera.combined);
    }

    @Override
    public void beginContact(Contact contact)
    {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsEnemy(b)) || (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsPlayer(b)))
        {
            player.hit();
        }

        if((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsGround(b)) || (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsPlayer(b)))
        {
            player.landed();
        }
    }

    @Override
    public void endContact(Contact contact)
    {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
    }
}
