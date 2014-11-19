package com.eddie.executiveexperience.World;

import com.badlogic.gdx.physics.box2d.World;
import com.eddie.executiveexperience.Utils.Env;

import java.util.Random;

public class WorldUtils
{
    private final static Random random = new Random(12381912);

    private static World world;

    public static void createWorld()
    {
        world = new World(Env.gravity, true);
    }

    public static World getWorld()
    {
        return world;
    }
}
