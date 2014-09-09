package com.eddie.executiveexperience.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eddie.executiveexperience.XEGame;

public class MainMenuScreen extends ScreenAdapter
{
    XEGame game;

    SpriteBatch batch;

    OrthographicCamera guiCamera;

    public MainMenuScreen(XEGame game)
    {
        this.game = game;

        batch = new SpriteBatch();

        guiCamera = new OrthographicCamera(700, 700);
        guiCamera.position.set(700 / 2, 700 / 2, 0);
    }

    public void update()
    {
    }

    public void draw()
    {
        GL20 gl = Gdx.gl20;

        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT);
        guiCamera.update();
        batch.setProjectionMatrix(guiCamera.combined);
    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

    @Override
    public void pause()
    {
    }
}
