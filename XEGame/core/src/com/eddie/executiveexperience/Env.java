package com.eddie.executiveexperience;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.siondream.core.Settings;

public abstract class Env
{
    // Game Instance
    public static XEGame game;

    // Application config
    public static float virtualWidth;
    public static float virtualHeight;
    public static float aspectRatio;
    public static float metersToPixels;
    public static float pixelsToMeters;

    // Physics
    public static Vector2 gravity;

    // Key Config
    public static int playerMoveLeft;
    public static int playerMoveRight;
    public static int playerJump;

    // Debug
    public static int debugLevel;
    public static boolean debug;
    public static boolean drawBodies;
    public static boolean drawJoints;
    public static boolean drawABBs;
    public static boolean drawInactiveBodies;
    public static boolean drawVelocities;
    public static boolean drawContacts;
    public static boolean drawStage;
    public static boolean drawGrid;
    public static boolean drawFPS;

    private static Settings settings;

    public static void init(XEGame game)
    {
        Env.game = game;

        settings = new Settings("config/globals.xml");

        virtualWidth = settings.getInt("virtualWidth", 700);
        virtualHeight = settings.getInt("virtualHeight", 700);
        aspectRatio = virtualWidth / virtualHeight;
        metersToPixels = settings.getFloat("metersToPixels", 70);
        pixelsToMeters = 1.0f / metersToPixels;

        Vector3 gravity3 = settings.getVector("gravity", Vector3.Zero);
        gravity = new Vector2(gravity3.x, gravity3.y);

        playerMoveLeft = Input.Keys.valueOf(settings.getString("playerMoveLeft", "A"));
        playerMoveRight = Input.Keys.valueOf(settings.getString("playerMoveRight", "D"));
        playerJump = Input.Keys.valueOf(settings.getString("playerJump", "Space"));

        if(playerMoveLeft == -1)
        {
            playerMoveLeft = Input.Keys.A;
        }
        else if(playerMoveRight == -1)
        {
            playerMoveRight = Input.Keys.D;
        }
        else if(playerJump == -1)
        {
            playerJump = Input.Keys.SPACE;
        }

        debugLevel = settings.getInt("debugLevel", Logger.INFO);
        debug = debugLevel > Logger.ERROR;
        drawBodies = settings.getBoolean("drawBodies", false);
        drawJoints = settings.getBoolean("drawJoints", false);
        drawABBs = settings.getBoolean("drawABBs", false);
        drawInactiveBodies = settings.getBoolean("drawInactiveBodies", false);
        drawVelocities = settings.getBoolean("drawVelocities", false);
        drawContacts = settings.getBoolean("drawContacts", false);
        drawStage = settings.getBoolean("drawStage", false);
        drawGrid = settings.getBoolean("drawGrid", true);
        drawFPS = settings.getBoolean("drawFPS", true);
    }
}