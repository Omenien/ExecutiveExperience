package com.eddie.executiveexperience;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends Screen
{
    protected OrthographicCamera UICamera;
    protected OrthographicCamera camera;
    protected Batch batch;
    protected Sprite deathTintSprite;
    protected GameStage stage;

    public GameScreen()
    {
        stage = new GameStage("Level 1.json", this);
        stage.loadMap();

        batch = new SpriteBatch();

        UICamera = new OrthographicCamera(Env.virtualWidth, Env.virtualHeight);
        UICamera.setToOrtho(false);

        camera = new OrthographicCamera(Env.virtualWidth * Env.pixelsToMeters, Env.virtualHeight * Env.pixelsToMeters);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);

        deathTintSprite = new Sprite(new Texture("assets/graphics/wasted.png"));

        Game.instance.getMusicManager().play(MusicManager.GameMusic.GAME_MUSIC);
    }

    public void update(float delta)
    {
        stage.act(delta);

        if(stage.isPlayerDead())
        {
            if(Game.instance.inputManager.isKeyTyped(Input.Keys.ENTER))
            {
                String curLevel = stage.levelFile;

                stage.dispose();

                stage = new GameStage(curLevel, this);
                stage.loadMap();
            }
        }

        if(stage.loadNewMap)
        {
            String newLevel = stage.newLevel;

            stage.dispose();

            stage = new GameStage(newLevel, this);
            stage.loadMap();
        }
    }

    public void draw()
    {
        batch.setProjectionMatrix(UICamera.combined);

        batch.begin();

        Vector2 playerPos = getGameStage().getPlayer().getBody().getPosition();

        if(stage.isPlayerDead())
        {
            camera.zoom = 0.5f;
        }
        else
        {
            camera.zoom = 1;
        }

        float cameraPosX = playerPos.x + (Constants.PLAYER_WIDTH / 2);
        float cameraPosY = playerPos.y + (Constants.PLAYER_HEIGHT / 2);

        float minCameraX = camera.zoom * (camera.viewportWidth / 2);
        float maxCameraX = getGameStage().getMapWidth() - minCameraX;
        float minCameraY = camera.zoom * (camera.viewportHeight / 2);
        float maxCameraY = getGameStage().getMapHeight() - minCameraY;

        cameraPosX = Math.min(maxCameraX, Math.max(cameraPosX, minCameraX));
        cameraPosY = Math.min(maxCameraY, Math.max(cameraPosY, minCameraY));

        camera.position.set(cameraPosX, cameraPosY, 0);

        camera.update();

        stage.draw();

        if(stage.isPlayerDead())
        {
            deathTintSprite.draw(batch);

        }

        if(Env.debug)
        {
            Game.instance.getUI().writeln("Player: " + playerPos.toString() + ", Friction: " + getGameStage().getPlayer().getFriction(), Color.WHITE);
        }

        batch.end();
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        draw();
    }

    @Override
    public void pause()
    {
    }

    public GameStage getGameStage()
    {
        return stage;
    }

    public Assets getAssets()
    {
        return Game.instance.getAssetManager();
    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }

    @Override
    public void processInput(InputManager inputManager)
    {
        if(inputManager.isKeyTyped(Input.Keys.F))
        {
            Game.instance.toggleFullscreen();
        }

        getGameStage().getPlayer().processInput(inputManager);
    }
}