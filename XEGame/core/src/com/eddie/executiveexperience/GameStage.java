package com.eddie.executiveexperience;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.eddie.executiveexperience.Entity.Enemy;
import com.eddie.executiveexperience.Entity.Player;
import com.eddie.executiveexperience.Entity.Saw;
import com.eddie.executiveexperience.World.Level;
import com.eddie.executiveexperience.World.MapBodyManager;
import com.eddie.executiveexperience.World.WorldUtils;
import com.siondream.core.physics.CategoryBitsManager;

import java.io.FileNotFoundException;

public class GameStage extends Stage implements ContactListener
{
    private World world;
    private Player player;

    private Level curLevel;

    private final float TIME_STEP = 1 / 60f;
    private float accumulator = 0f;

    protected SpriteBatch batch;

    protected Box2DDebugRenderer box2DDebugRenderer;
    protected OrthographicCamera camera;

    protected TiledMap map;
    protected MapProperties mapProperties;
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

        camera = new OrthographicCamera(Env.virtualWidth * Env.pixelsToMeters, Env.virtualHeight * Env.pixelsToMeters);

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
        mapProperties = map.getProperties();

        mapBodyManager = new MapBodyManager(world, Env.metersToPixels, null, Env.debugLevel);
        mapBodyManager.createPhysics(this, map);

        mapRenderer = new OrthogonalTiledMapRenderer(map, Env.pixelsToMeters);
    }

    private void setupWorld()
    {
        WorldUtils.createWorld();
        WorldUtils.getWorld().setContactListener(this);
        setupPlayer();
        createSaw();
    }

    private void setupPlayer()
    {
        player = new Player(WorldUtils.createPlayer(this));
        addActor(player);
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

        player.handleInput();

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
            if(BodyUtils.bodyIsSaw(body))
            {
                createSaw();
            }

            WorldUtils.getWorld().destroyBody(body);
        }
    }

    private void createEnemy()
    {
        Enemy enemy = new Enemy(WorldUtils.createEnemy());
        addActor(enemy);
    }

    private void createSaw()
    {
        Saw saw = new Saw(WorldUtils.createSaw(this));
        addActor(saw);
    }

    @Override
    public boolean keyDown(int keyCode)
    {
        if(keyCode == Env.playerJump)
        {
            player.jump();
        }

        return false;
    }

    @Override
    public void draw()
    {
        super.draw();

        int mapWidth = mapProperties.get("width", Integer.class);
        int mapHeight = mapProperties.get("height", Integer.class);

        Vector2 playerPos = player.getBody().getPosition();

        float cameraPosX = playerPos.x + (Constants.PLAYER_WIDTH / 2);
        float cameraPosY = playerPos.y + (Constants.PLAYER_HEIGHT / 2);

        if(cameraPosX < (camera.viewportWidth / 2))
        {
            cameraPosX = camera.viewportWidth / 2;
        }
        else if(cameraPosX > mapWidth - (camera.viewportWidth / 2))
        {
            cameraPosX = mapWidth - (camera.viewportWidth / 2);
        }

        if(cameraPosY < (camera.viewportHeight / 2))
        {
            cameraPosY = camera.viewportHeight / 2;
        }
        else if(cameraPosY > mapHeight - (camera.viewportHeight / 2))
        {
            cameraPosY = mapHeight - (camera.viewportHeight / 2);
        }

        camera.position.set(cameraPosX, cameraPosY, 0.0f);

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        box2DDebugRenderer.render(WorldUtils.getWorld(), camera.combined);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for(Actor actor : getActors())
        {
            actor.draw(batch, 0.0f);
        }

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