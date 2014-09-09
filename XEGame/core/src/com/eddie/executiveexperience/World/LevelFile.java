package com.eddie.executiveexperience.World;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class LevelFile implements Json.Serializable
{
    public String name;
    public String mapFile;

    public int playerStartX;
    public int playerStartY;

    public LevelFile()
    {
        name = "Level 1";
        mapFile = "Level 1.tmx";
        playerStartX = 10;
        playerStartY = 10;
    }

    @Override
    public void write(Json json)
    {
        json.writeValue("name", name);
        json.writeValue("mapFile", mapFile);
        json.writeValue("playerStartX", playerStartX);
        json.writeValue("playerStartY", playerStartY);

    }

    @Override
    public void read(Json json, JsonValue jsonData)
    {
        name = jsonData.get("name").asString();
        mapFile = jsonData.get("mapFile").asString();
        playerStartX = jsonData.get("playerStartX").asInt();
        playerStartY = jsonData.get("playerStartY").asInt();
    }
}
