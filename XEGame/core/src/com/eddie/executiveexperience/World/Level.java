package com.eddie.executiveexperience.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.FileNotFoundException;

public class Level
{
    LevelFile levelFile;

    Json json;

    public Level(String fileName) throws FileNotFoundException
    {
        json = new Json();

        FileHandle file = Gdx.files.internal(fileName);

        if(file.exists())
        {
            levelFile = json.fromJson(LevelFile.class, file);
        }
        else
        {
            levelFile = new LevelFile();

            json.setOutputType(JsonWriter.OutputType.json);

            file.writeString(json.prettyPrint(levelFile), false);

            throw new FileNotFoundException(file.path());
        }
    }

    public String getMapPath()
    {
        return levelFile.mapFile;
    }

    public String getPhysicsLayer() { return levelFile.physicsLayer; }

    public String getEntityLayer() { return levelFile.entityLayer; }

    public String[] getInvisibleLayers() { return levelFile.invisibleLayers; }
}
