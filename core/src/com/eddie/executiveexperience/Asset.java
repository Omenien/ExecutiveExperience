package com.eddie.executiveexperience;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Asset implements Json.Serializable
{
    public Class<?> type;
    public String path;
    public AssetLoaderParameters parameters;

    @Override
    public void write(Json json)
    {
        json.writeValue("assetType", type.getName());
        json.writeValue("path", path);
        json.writeValue("parameters", parameters);
    }

    @Override
    public void read(Json json, JsonValue jsonData)
    {
        String typeString = jsonData.get("type").asString();

        switch(typeString)
        {
            case "SpriteAnimationData":
                typeString = "com.eddie.executiveexperience.Graphics.SpriteAnimationData";
                break;
        }

        try
        {
            type = Class.forName(typeString);
        }
        catch(Exception e)
        {
            type = null;
        }

        path = jsonData.get("path").asString();

        JsonValue parametersValue = jsonData.get("parameters");
        parameters = parametersValue != null ? json.fromJson(AssetLoaderParameters.class, parametersValue.toString()) : null;
    }
}