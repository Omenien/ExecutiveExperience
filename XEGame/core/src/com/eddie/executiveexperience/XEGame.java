package com.eddie.executiveexperience;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eddie.executiveexperience.Screens.GameScreen;

public class XEGame extends Game
{
    public SpriteBatch batch;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void render()
    {
        GL20 gl = Gdx.gl20;
        gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        super.render();
    }
}
