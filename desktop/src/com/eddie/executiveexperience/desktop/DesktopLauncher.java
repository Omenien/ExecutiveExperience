package com.eddie.executiveexperience.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.Game;

public class DesktopLauncher
{
    protected static LwjglApplication application;

    public static void main(String[] arg)
    {
        Game game = new Game();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 768;
        config.y = 0;
        config.resizable = false;
        config.foregroundFPS = 60;
        config.backgroundFPS = 60;
        config.vSyncEnabled = true;

        application = new LwjglApplication(game, config);

        Env.init(game);
    }
}
