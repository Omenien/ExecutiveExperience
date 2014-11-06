package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.InputManager;
import com.eddie.executiveexperience.Screen;

public class HomeScreen extends Screen
{
    SpriteBatch batch;

    OrthographicCamera guiCamera;

    protected HomeStage homeStage;

    public HomeScreen()
    {
        batch = new SpriteBatch();

        guiCamera = new OrthographicCamera(Env.virtualWidth, Env.virtualHeight);
        guiCamera.position.set(Env.virtualWidth / 2, Env.virtualHeight / 2, 0);

        homeStage = new HomeStage();
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

        homeStage.draw();
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

    @Override
    public void processInput(InputManager inputManager)
    {

    }
}
