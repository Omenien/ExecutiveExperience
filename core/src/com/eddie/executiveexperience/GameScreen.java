package com.eddie.executiveexperience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends ScreenAdapter
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

        Gdx.input.setInputProcessor(stage);

        deathTintSprite = new Sprite(new Texture("assets/graphics/wasted.png"));

        XEGame.game.getMusicManager().play(MusicManager.GameMusic.GAME_MUSIC);
    }

    public void update(float delta)
    {
        stage.act(delta);

        if(stage.isPlayerDead())
        {
            if(Gdx.input.isKeyPressed(Input.Keys.ENTER) || stage.skipDeathScreen)
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

        XEGame.game.getMusicManager().setVolume(delta * delta * 1000 > 1 ? 1 : (delta * delta * 1000 < 0 ? 0 : delta * delta * 1000));
    }

    public void draw()
    {
        GL20 gl = Gdx.gl20;

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT);

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

        /*if(cameraPosX < (camera.viewportWidth / 2))
        {
            cameraPosX = camera.viewportWidth / 2;
        }
        else if(cameraPosX > getGameStage().getMapWidth() - (camera.viewportWidth / 2))
        {
            cameraPosX = getGameStage().getMapWidth() - (camera.viewportWidth / 2);
        }

        if(cameraPosY < (camera.viewportHeight / 2))
        {
            cameraPosY = camera.viewportHeight / 2;
        }
        else if(cameraPosY > getGameStage().getMapHeight() - (camera.viewportHeight / 2))
        {
            cameraPosY = getGameStage().getMapHeight() - (camera.viewportHeight / 2);
        }*/

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
            batch.setProjectionMatrix(UICamera.combined);

            batch.begin();

            deathTintSprite.draw(batch);

            batch.end();
        }
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

    public void reload()
    {
        stage.killPlayer();

        stage.skipDeathScreen = true;
    }

    public GameStage getGameStage()
    {
        return stage;
    }

    public Assets getAssets()
    {
        return XEGame.game.getAssetManager();
    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }
}