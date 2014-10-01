package com.eddie.executiveexperience.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.eddie.executiveexperience.Assets;
import com.eddie.executiveexperience.GameStage;

public class GameScreen extends ScreenAdapter
{
    protected Assets assets;
    private GameStage stage;

    public GameScreen()
    {
        assets = new Assets("assets/config/assets.json");
        assets.loadGroup("base");
        assets.finishLoading();

        stage = new GameStage("Level 1.json", this);
        stage.loadMap();

        Gdx.input.setInputProcessor(stage);
    }

    public void update(float delta)
    {
        stage.act(delta);

        if(stage.isPlayerDead())
        {
            String curLevel = stage.levelFile;

            stage.dispose();

            stage = new GameStage(curLevel, this);
            stage.loadMap();
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
        GL20 gl = Gdx.gl20;

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT);

        stage.draw();
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
        return assets;
    }
}