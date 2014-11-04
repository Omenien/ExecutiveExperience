package com.eddie.executiveexperience.World;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class LevelFile implements Json.Serializable
{
    public String name;
    public String mapFile;

    public String entityLayer;
    public String physicsLayer;
    public String[] invisibleLayers;

    public LevelFile()
    {
        name = "Level 1";
        mapFile = "Level 1.tmx";

        physicsLayer = "Physics";
        entityLayer = "Entities";

        invisibleLayers = null;
    }

    @Override
    public void write(Json json)
    {
        json.writeValue("name", name);
        json.writeValue("mapFile", mapFile);

        json.writeValue("physicsLayer", physicsLayer);
        json.writeValue("entityLayer", entityLayer);

        if (invisibleLayers != null && invisibleLayers.length > 0)
        {
            json.writeValue("invisibleLayers", invisibleLayers);
        }
    }

    @Override
    public void read(Json json, JsonValue jsonData)
    {
        name = jsonData.get("name").asString();
        mapFile = jsonData.get("mapFile").asString();

        physicsLayer = jsonData.get("physicsLayer").asString();
        entityLayer = jsonData.get("entityLayer").asString();

        try
        {
            invisibleLayers = jsonData.get("invisibleLayers").asStringArray();
        }
        catch (Exception e)
        {
            invisibleLayers = null;
        }
    }
}
