package com.eddie.executiveexperience.World;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Logger;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameStage;

import java.lang.reflect.Constructor;
import java.util.Iterator;

public class MapObjectManager
{
    private Logger logger;

    /**
     * @param loggingLevel verbosity of the embedded logger.
     */
    public MapObjectManager(int loggingLevel)
    {
        logger = new Logger("MapObjectManager", loggingLevel);
        logger.info("Initializing MapObjectManager");
    }

    public void createObjects(GameStage gameStage, TiledMap map)
    {
        createObjects(gameStage, map, "Objects");
    }

    public void createObjects(GameStage gameStage, TiledMap map, String layerName)
    {
        MapLayer layer = map.getLayers().get(layerName);

        if(layer == null)
        {
            logger.error("Unable to find Object layer \"" + layerName + "\" in map. Objects will not be loaded.");
            return;
        }

        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIt = objects.iterator();

        while(objectIt.hasNext())
        {
            MapObject object = objectIt.next();

            MapProperties objectProperties = object.getProperties();

            String type = objectProperties.get("type", "Saw", String.class);

            float x = objectProperties.get("x", 0f, Float.class) * Env.pixelsToMeters;
            float y = objectProperties.get("y", 0f, Float.class) * Env.pixelsToMeters;

            Constructor constructor;

            try
            {
                constructor = Class.forName("com.eddie.executiveexperience.Entity." + type).getConstructor(GameStage.class, Float.TYPE, Float.TYPE, MapObject.class);

                constructor.newInstance(gameStage, x, y, object);
            }
            catch(Exception e)
            {
                logger.error("Error loading object of type " + type + " at (" + x + ", " + y + ").");

                e.printStackTrace();
            }
        }
    }
}