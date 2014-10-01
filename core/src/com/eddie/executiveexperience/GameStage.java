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
import com.eddie.executiveexperience.Entity.Player;
import com.eddie.executiveexperience.Entity.UserData.DoorUserData;
import com.eddie.executiveexperience.Entity.UserData.WallSensorUserData;
import com.eddie.executiveexperience.Screens.GameScreen;
import com.eddie.executiveexperience.World.Level;
import com.eddie.executiveexperience.World.MapBodyManager;
import com.eddie.executiveexperience.World.MapObjectManager;
import com.eddie.executiveexperience.World.WorldUtils;

import java.io.FileNotFoundException;

public class GameStage extends Stage implements ContactListener
{
    private final float TIME_STEP = 1 / 60f;
    public String levelFile;
    public boolean loadNewMap;
    public String newLevel;
    protected GameScreen gameScreen;
    protected SpriteBatch batch;
    protected Box2DDebugRenderer box2DDebugRenderer;
    protected OrthographicCamera camera;
    protected TiledMap map;
    protected MapProperties mapProperties;
    protected OrthogonalTiledMapRenderer mapRenderer;
    protected MapBodyManager mapBodyManager;
    protected MapObjectManager mapObjectManager;
    protected int mapWidth;
    protected int mapHeight;
    private World world;
    private Player player;
    private Level curLevel;
    private float accumulator = 0f;

    public GameStage(String levelFile, GameScreen gameScreen)
    {
        this.gameScreen = gameScreen;

        this.levelFile = levelFile;

        batch = new SpriteBatch();

        setupWorld();
        world = WorldUtils.getWorld();

        box2DDebugRenderer = new Box2DDebugRenderer();

        if(Env.debug)
        {
            box2DDebugRenderer.setDrawAABBs(Env.drawABBs);
            box2DDebugRenderer.setDrawBodies(Env.drawBodies);
            box2DDebugRenderer.setDrawContacts(Env.drawContacts);
            box2DDebugRenderer.setDrawInactiveBodies(Env.drawInactiveBodies);
            box2DDebugRenderer.setDrawJoints(Env.drawJoints);
            box2DDebugRenderer.setDrawVelocities(Env.drawVelocities);
        }
        else
        {
            box2DDebugRenderer.setDrawAABBs(false);
            box2DDebugRenderer.setDrawBodies(false);
            box2DDebugRenderer.setDrawContacts(false);
            box2DDebugRenderer.setDrawInactiveBodies(false);
            box2DDebugRenderer.setDrawJoints(false);
            box2DDebugRenderer.setDrawVelocities(false);
        }

        camera = new OrthographicCamera(Env.virtualWidth * Env.pixelsToMeters, Env.virtualHeight * Env.pixelsToMeters);

        try
        {
            curLevel = new Level("assets/" + levelFile);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        loadNewMap = false;
        newLevel = "";
    }

    public void loadMap()
    {
        map = gameScreen.getAssets().get(curLevel.getMapPath());
        mapProperties = map.getProperties();

        mapBodyManager = new MapBodyManager(world, Env.metersToPixels, null, Env.debugLevel);
        mapBodyManager.createPhysics(this, map, curLevel.getPhysicsLayer());

        mapObjectManager = new MapObjectManager(Env.debugLevel);
        mapObjectManager.createObjects(this, map, curLevel.getEntityLayer());

        mapRenderer = new OrthogonalTiledMapRenderer(map, Env.pixelsToMeters);

        mapWidth = mapProperties.get("width", Integer.class);
        mapHeight = mapProperties.get("height", Integer.class);
    }

    private void setupWorld()
    {
        WorldUtils.createWorld();
        WorldUtils.getWorld().setContactListener(this);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        for(Actor actor : getActors())
        {
            if(actor instanceof GameActor)
            {
                GameActor gameActor = (GameActor) actor;

                Body body = gameActor.getBody();

                if(!BodyUtils.bodyInBounds(body))
                {
                    getRoot().removeActor(actor);

                    world.destroyBody(body);
                }
            }
        }

        accumulator += delta;

        while(accumulator >= delta)
        {
            WorldUtils.getWorld().step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void draw()
    {
        super.draw();

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
        mapRenderer.render(new int[]{0});

        box2DDebugRenderer.render(WorldUtils.getWorld(), camera.combined);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for(Actor actor : getActors())
        {
            actor.draw(batch, 0.0f);
        }

        batch.end();

        mapRenderer.render(new int[]{1});
    }

    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body a = fixtureA.getBody();
        Body b = fixtureB.getBody();

        if((BodyUtils.fixtureIsPlayerCollisionFixture(a, fixtureA) && BodyUtils.fixtureIsDeadly(fixtureB) || BodyUtils.fixtureIsDeadly(fixtureA) && BodyUtils.fixtureIsPlayerCollisionFixture(b, fixtureB)))
        {
            player.die();
        }

        if((BodyUtils.fixtureIsPlayerCollisionFixture(a, fixtureA) && BodyUtils.bodyIsDoor(b)))
        {
            loadNewMap = true;

            newLevel = ((DoorUserData) b.getUserData()).getNewLevel();
        }
        else if((BodyUtils.bodyIsDoor(a) && BodyUtils.fixtureIsPlayerCollisionFixture(b, fixtureB)))
        {
            loadNewMap = true;

            newLevel = ((DoorUserData) a.getUserData()).getNewLevel();
        }

        if((BodyUtils.fixtureIsJumpSensor(fixtureA) && BodyUtils.bodyIsTerrain(b)) || (BodyUtils.bodyIsTerrain(a) && BodyUtils.fixtureIsJumpSensor(fixtureB)))
        {
            player.incrementFootContacts();
        }

        if(BodyUtils.fixtureIsWallSensor(fixtureA) && BodyUtils.bodyIsTerrain(b))
        {
            WallSensorUserData wallSensorUserData = (WallSensorUserData) fixtureA.getUserData();

            if(wallSensorUserData.getSide() == WallSensorUserData.Side.LEFT)
            {
                player.incrementLeftWallContacts();
            }
            else
            {
                player.incrementRightWallContacts();
            }
        }
        else if(BodyUtils.bodyIsTerrain(a) && BodyUtils.fixtureIsWallSensor(fixtureB))
        {
            WallSensorUserData wallSensorUserData = (WallSensorUserData) fixtureB.getUserData();

            if(wallSensorUserData.getSide() == WallSensorUserData.Side.LEFT)
            {
                player.incrementLeftWallContacts();
            }
            else
            {
                player.incrementRightWallContacts();
            }
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body a = fixtureA.getBody();
        Body b = fixtureB.getBody();

        if((BodyUtils.fixtureIsJumpSensor(fixtureA) && BodyUtils.bodyIsTerrain(b)) || (BodyUtils.bodyIsTerrain(a) && BodyUtils.fixtureIsJumpSensor(fixtureB)))
        {
            player.decrementFootContacts();
        }

        if(BodyUtils.fixtureIsWallSensor(fixtureA) && BodyUtils.bodyIsTerrain(b))
        {
            WallSensorUserData wallSensorUserData = (WallSensorUserData) fixtureA.getUserData();

            if(wallSensorUserData.getSide() == WallSensorUserData.Side.LEFT)
            {
                player.decrementLeftWallContacts();
            }
            else
            {
                player.decrementRightWallContacts();
            }
        }
        else if(BodyUtils.bodyIsTerrain(a) && BodyUtils.fixtureIsWallSensor(fixtureB))
        {
            WallSensorUserData wallSensorUserData = (WallSensorUserData) fixtureB.getUserData();

            if(wallSensorUserData.getSide() == WallSensorUserData.Side.LEFT)
            {
                player.decrementLeftWallContacts();
            }
            else
            {
                player.decrementRightWallContacts();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
    }

    public World getWorld()
    {
        return world;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public boolean isPlayerDead()
    {
        float playerX = player.getBody().getPosition().x;
        float playerY = player.getBody().getPosition().y;

        return player.isDead() || !(playerX > 0 && playerX < mapWidth && playerY + (player.getUserData().getHeight() / 2) > 0);
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public GameScreen getScreen()
    {
        return gameScreen;
    }
}