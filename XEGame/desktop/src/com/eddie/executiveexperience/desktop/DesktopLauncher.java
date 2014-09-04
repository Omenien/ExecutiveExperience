package com.eddie.executiveexperience.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eddie.executiveexperience.Constants;
import com.eddie.executiveexperience.XEGame;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.GAME_WIDTH;
        config.height = Constants.GAME_HEIGHT;
        config.height = 700;
        config.y = 0;
        config.resizable = false;
        config.foregroundFPS = 60;
        config.backgroundFPS = 60;
        config.vSyncEnabled = true;
        new LwjglApplication(new XEGame(), config);
    }
}
