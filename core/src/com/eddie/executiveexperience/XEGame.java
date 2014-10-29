package com.eddie.executiveexperience;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.eddie.executiveexperience.Entity.EntityData;
import com.eddie.executiveexperience.UI.HomeScreen;

public class XEGame extends Game
{
    public static XEGame game;

    public GameScreen gameScreen;
    public HomeScreen homeScreen;
    protected MusicManager musicManager;
    protected Assets assets;

    public XEGame()
    {
        game = this;
    }

    @Override
    public void create()
    {
        assets = new Assets("assets/config/assets.json");
        assets.loadGroup("base");
        assets.loadFolder("entities", "assets/entities/", EntityData.class, ".json", true);
        assets.loadFolder("maps", "assets/levels/", TiledMap.class, ".tmx", false);
        assets.finishLoading();

        musicManager = new MusicManager();

        homeScreen = new HomeScreen();

        setScreen(homeScreen);
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

    public MusicManager getMusicManager()
    {
        return musicManager;
    }
}

/*
 * Credits:
 * Thomas Snyder, Jonathan Bees, Isaac Merritt - Bug Testing
 * Blue Strike Warrior - Graphics
 */