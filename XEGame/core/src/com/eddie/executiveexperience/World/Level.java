package com.eddie.executiveexperience.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import net.dermetfan.utils.libgdx.box2d.Box2DMapObjectParser;

import java.io.FileNotFoundException;

public class Level
{
    LevelFile levelFile;

    Json json;

    OrthogonalTiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;
    Box2DMapObjectParser mapObjectParser;

    public Level(String fileName) throws FileNotFoundException
    {
        json = new Json();

        FileHandle file = Gdx.files.local(fileName);

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
}
