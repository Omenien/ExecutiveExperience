package com.eddie.executiveexperience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eddie.executiveexperience.Entity.Enemy;
import com.eddie.executiveexperience.Entity.Player;
import com.eddie.executiveexperience.World.Level;
import com.eddie.executiveexperience.World.WorldUtils;
import com.siondream.core.physics.CategoryBitsManager;
import com.siondream.core.physics.CollisionHandler;

import java.io.FileNotFoundException;

public class GameStage extends Stage implements ContactListener
{
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private World world;
    private Ground ground;
    private Player player;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    protected SpriteBatch batch;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected ShapeRenderer shapeRenderer;
    protected Box2DDebugRenderer renderer;
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer mapRenderer;

    protected CategoryBitsManager categoryBitsManager;
    protected CollisionHandler collisionHandler;

    protected Assets assets;

    public GameStage()
    {
        categoryBitsManager = new CategoryBitsManager();

        assets = new Assets("config/assets.json");
        assets.loadGroup("base");
        assets.finishLoading();

        setupWorld();
        renderer = new Box2DDebugRenderer();
        setupCamera();

        try
        {
            Level level = new Level("Level 1.json");
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void setupWorld()
    {
        WorldUtils.createWorld();
        WorldUtils.getWorld().setContactListener(collisionHandler);
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

    public short getCategoryBits(String level)
    {
        return categoryBitsManager.getCategoryBits(level);
    }

    public World getWorld()
    {
        return world;
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

    public CategoryBitsManager getCategoryBitsManager()
    {
        return categoryBitsManager;
    }
}
