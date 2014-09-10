package com.eddie.executiveexperience;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eddie.executiveexperience.Entity.Enemy;
import com.eddie.executiveexperience.Entity.Player;
import com.eddie.executiveexperience.World.Level;
import com.eddie.executiveexperience.World.MapBodyManager;
import com.eddie.executiveexperience.World.WorldUtils;
import com.siondream.core.physics.CategoryBitsManager;
import net.dermetfan.utils.libgdx.graphics.Box2DSprite;

import java.io.FileNotFoundException;

public class GameStage extends Stage implements ContactListener
{
    private World world;
    private Player player;

    private Level curLevel;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    protected SpriteBatch batch;
    protected Viewport viewport;
    protected OrthographicCamera camera;

    protected Box2DDebugRenderer box2DDebugRenderer;
    protected OrthographicCamera debugCamera;

    protected TiledMap map;
    protected OrthogonalTiledMapRenderer mapRenderer;
    protected MapBodyManager mapBodyManager;

    protected CategoryBitsManager categoryBitsManager;

    protected Assets assets;

    public GameStage()
    {
        batch = new SpriteBatch();

        categoryBitsManager = new CategoryBitsManager();

        assets = new Assets("config/assets.json");
        assets.loadGroup("base");
        assets.finishLoading();

        setupWorld();
        world = WorldUtils.getWorld();

        box2DDebugRenderer = new Box2DDebugRenderer();
        box2DDebugRenderer.setDrawAABBs(Env.drawABBs);
        box2DDebugRenderer.setDrawBodies(Env.drawBodies);
        box2DDebugRenderer.setDrawContacts(Env.drawContacts);
        box2DDebugRenderer.setDrawInactiveBodies(Env.drawInactiveBodies);
        box2DDebugRenderer.setDrawJoints(Env.drawJoints);
        box2DDebugRenderer.setDrawVelocities(Env.drawVelocities);
        debugCamera = new OrthographicCamera(Env.virtualWidth * Env.pixelsToMeters, Env.virtualHeight * Env.pixelsToMeters);
        debugCamera.position.set(debugCamera.viewportWidth / 2, debugCamera.viewportHeight / 2, 0.0f);
        batch.setProjectionMatrix(debugCamera.combined);

        setupCamera();

        try
        {
            curLevel = new Level("Level 1.json");
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void loadMap()
    {
        map = assets.get(curLevel.getMapPath());

        mapBodyManager = new MapBodyManager(world, Env.metersToPixels, null, Env.debugLevel);
        mapBodyManager.createPhysics(this, map);

        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    private void setupWorld()
    {
        WorldUtils.createWorld();
        WorldUtils.getWorld().setContactListener(this);
        setupPlayer();
        createEnemy();
    }

    private void setupPlayer()
    {
        player = new Player(WorldUtils.createPlayer(this));
        addActor(player);
    }

    private void setupCamera()
    {
        camera = new OrthographicCamera(Env.virtualWidth * Env.pixelsToMeters, Env.virtualHeight * Env.pixelsToMeters);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0.0f);
        camera.setToOrtho(false);
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

        debugCamera.update();
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        box2DDebugRenderer.render(WorldUtils.getWorld(), debugCamera.combined);

        batch.begin();

        Box2DSprite.draw(batch, getWorld());

        batch.end();
    }

    public short getCategoryBits(String level)
    {
        return categoryBitsManager.getCategoryBits(level);
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

        if((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsTerrain(b)) || (BodyUtils.bodyIsTerrain(a) && BodyUtils.bodyIsPlayer(b)))
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

    public World getWorld()
    {
        return world;
    }

    public Assets getAssetManager()
    {
        return assets;
    }
}