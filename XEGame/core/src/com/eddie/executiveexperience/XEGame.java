package com.eddie.executiveexperience;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.eddie.executiveexperience.Screens.GameScreen;

public class XEGame extends Game
{
    private static GameScreen gameScreen;

    @Override
    public void create()
    {
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

    public static GameScreen getGameScreen()
    {
        if(gameScreen == null)
        {
            System.out.println("AN UNKNOWN ERROR HAS OCCURRED, PLEASE SELECT OKAY TO TRY AGAIN.");
        }

        return gameScreen;
    }
}
