package com.eddie.executiveexperience;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.eddie.executiveexperience.Screens.GameScreen;

import java.nio.ByteBuffer;

public class XEGame extends Game
{
    public static XEGame game;

    private static GameScreen gameScreen;

    Assets assets;

    public XEGame()
    {
        game = this;
    }

    @Override
    public void create()
    {
        assets = new Assets("assets/config/assets.json");
        assets.loadGroup("base");
        assets.finishLoading();

        gameScreen = new GameScreen();

        setScreen(gameScreen);
    }

    @Override
    public void render()
    {
        GL20 gl = Gdx.gl20;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    public GameScreen getGameScreen()
    {
        return gameScreen;
    }

    public Assets getAssetManager()
    {
        return assets;
    }
}

/*
 * Credits:
 * Thomas Snyder, Jonathan Bees, Isaac Merritt - Bug Testing
 * Blue Strike Warrior - Graphics
 */