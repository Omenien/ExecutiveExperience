package com.eddie.executiveexperience.World;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.eddie.executiveexperience.Entity.Player;
import com.eddie.executiveexperience.Entity.StationarySaw;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.GameStage;

import java.util.Iterator;

public class MapObjectManager
{
    private Logger logger;

    /**
     * @param world        box2D world to work with.
     * @param loggingLevel verbosity of the embedded logger.
     */
    public MapObjectManager(World world, int loggingLevel)
    {
        logger = new Logger("MapBodyManager", loggingLevel);
        logger.info("Initializing MapBodyManager");
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

        MapProperties mapProperties = map.getProperties();
        int mapHeight = mapProperties.get("height", Integer.class);

        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIt = objects.iterator();

        while(objectIt.hasNext())
        {
            MapObject object = objectIt.next();

            MapProperties objectProperties = object.getProperties();

            String type = objectProperties.get("type", "Saw", String.class);

            float x = objectProperties.get("x", 0f, Float.class) * Env.pixelsToMeters;
            float y = (mapHeight - objectProperties.get("y", 0f, Float.class)) * Env.pixelsToMeters;

            switch(type.toUpperCase())
            {
                case "PLAYER":
                    new Player(x, y, objectProperties);
                    break;

                case "STATIONARYSAW":
                    new StationarySaw(x, y, objectProperties);
                    break;
            }
        }
    }
}
