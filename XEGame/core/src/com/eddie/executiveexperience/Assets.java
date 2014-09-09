package com.eddie.executiveexperience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.*;
import com.eddie.executiveexperience.Animation.SpriteAnimationData;
import com.eddie.executiveexperience.Animation.SpriteAnimationLoader;

public class Assets implements Disposable, AssetErrorListener
{
    private Logger logger;

    private AssetManager assetManager;

    private ObjectMap<String, Array<Asset>> groups;

    private static String TAG = "AssetManager";

    public Assets(String assetFile)
    {
        logger = new Logger(TAG, Env.debugLevel);

        assetManager = new AssetManager();

        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(SpriteAnimationData.class, new SpriteAnimationLoader(new InternalFileHandleResolver()));

        loadGroups(assetFile);
    }

    public AssetLoader<?, ?> getLoader(Class<?> type)
    {
        return assetManager.getLoader(type);
    }

    public void loadGroup(String groupName)
    {
        Array<Asset> assets = groups.get(groupName, null);

        if(assets != null)
        {
            for(Asset asset : assets)
            {
                logger.debug("Loading Asset " + asset.path);

                assetManager.load(asset.path, asset.type, asset.parameters);
            }
        }
        else
        {
            // TODO: Ya dun goofed
        }
    }

    public void unloadGroup(String groupName)
    {
        Array<Asset> assets = groups.get(groupName, null);

        if(assets != null)
        {
            for(Asset asset : assets)
            {
                if(assetManager.isLoaded(asset.path, asset.type))
                {
                    assetManager.unload(asset.path);
                }
            }
        }
        else
        {
            // TODO: Ya dun goofed
        }
    }

    public synchronized <T> T get(String fileName)
    {
        return assetManager.get(fileName);
    }

    public synchronized <T> T get(String fileName, Class<T> type)
    {
        return assetManager.get(fileName, type);
    }

    public <T> boolean isLoaded(String fileName, Class<T> type)
    {
        return assetManager.isLoaded(fileName, type);
    }

    public boolean update()
    {
        return assetManager.update();
    }

    public void finishLoading()
    {
        assetManager.finishLoading();
    }

    public float getProgress()
    {
        return assetManager.getProgress();
    }

    @Override
    public void dispose()
    {
        assetManager.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
        logger.error("Error loading " + asset.fileName + ". Error: " + throwable.getMessage());
    }

    public void loadGroups(String assetFile)
    {
        groups = new ObjectMap<>();

        Json json = new Json();
        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(Gdx.files.internal(assetFile));

        JsonValue.JsonIterator groupIterator = root.iterator();

        while(groupIterator.hasNext())
        {
            JsonValue groupValue = groupIterator.next();

            if(groups.containsKey(groupValue.name))
            {
                logger.error("Already loaded group " + groupValue.name + ".");
                continue;
            }

            Array<Asset> assets = new Array<>();

            JsonValue.JsonIterator assetIterator = groupValue.iterator();

            while(assetIterator.hasNext())
            {
                JsonValue assetValue = assetIterator.next();

                Asset asset = json.fromJson(Asset.class, assetValue.toString());
                assets.add(asset);
            }

            groups.put(groupValue.name, assets);
        }
    }
}
