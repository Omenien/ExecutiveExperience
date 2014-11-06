package com.eddie.executiveexperience.Entity;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;
import com.eddie.executiveexperience.Env;

public class EntityLoader extends AsynchronousAssetLoader<EntityData, EntityLoader.EntityParameter>
{
    private EntityData entityData = null;
    private Logger logger;

    public EntityLoader(FileHandleResolver resolver)
    {
        super(resolver);

        entityData = null;

        logger = new Logger("EntityLoader", Env.debugLevel);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, EntityParameter parameter)
    {
        Array<AssetDescriptor> dependencies = new Array<>();
        dependencies.add(new AssetDescriptor<>(stripExtension(fileName) + ".png", Texture.class));

        return dependencies;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, EntityParameter parameter)
    {
        logger.info("Loading " + fileName);

        entityData = new EntityData();

        // Retrieve texture
        entityData.texture = manager.get(stripExtension(fileName) + ".png", Texture.class);

        try
        {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);

            JsonValue graphicsRoot = root.get("graphics");

            entityData.texture = manager.get(graphicsRoot.getString("textureFile"), Texture.class);
            entityData.frameDuration = graphicsRoot.getFloat("frameDuration");

            JsonValue animations = graphicsRoot.get("animations");
            JsonValue.JsonIterator animationsIt = animations.iterator();
            boolean first = true;

            while(animationsIt.hasNext())
            {
                JsonValue animationValue = animationsIt.next();

                String name = animationValue.getString("name");

                JsonValue frames = animationValue.get("frames");
                JsonValue.JsonIterator framesIt = frames.iterator();

                boolean createReverse = animationValue.getBoolean("createReverse", false);

                if(createReverse)
                {
                    String rightName = name + "_right";
                    String leftName = name + "_left";

                    Animation rightAnimation = new Animation(entityData.frameDuration,
                            getAnimationFrames(entityData.texture, framesIt),
                            getPlayMode(animationValue.getString("mode", "normal")));

                    framesIt = frames.iterator();
                    Animation leftAnimation = new Animation(entityData.frameDuration,
                            getAnimationFrames(entityData.texture, framesIt, true),
                            getPlayMode(animationValue.getString("mode", "normal")));

                    entityData.animations.put(rightName, rightAnimation);
                    entityData.animations.put(leftName, leftAnimation);

                    if(first)
                    {
                        entityData.defaultAnimation = rightAnimation;
                        first = false;
                    }
                }
                else
                {
                    Animation animation = new Animation(entityData.frameDuration,
                            getAnimationFrames(entityData.texture, framesIt),
                            getPlayMode(animationValue.getString("mode", "normal")));

                    entityData.animations.put(name, animation);

                    if(first)
                    {
                        entityData.defaultAnimation = animation;
                        first = false;
                    }
                }

                logger.info("Loaded animation " + name + " from " + fileName + ".");
            }
        }
        catch(Exception e)
        {
            logger.error("Error loading " + fileName + ".");

            e.printStackTrace();
        }
    }

    @Override
    public EntityData loadSync(AssetManager manager, String fileName, FileHandle file, EntityParameter parameter)
    {
        return entityData;
    }

    private Animation.PlayMode getPlayMode(String mode)
    {
        switch(mode)
        {
            case "normal":
                return Animation.PlayMode.NORMAL;
            case "loop":
                return Animation.PlayMode.LOOP;
            case "loop_pingpong":
                return Animation.PlayMode.LOOP_PINGPONG;
            case "loop_random":
                return Animation.PlayMode.LOOP_RANDOM;
            case "loop_reversed":
                return Animation.PlayMode.LOOP_REVERSED;
            case "reversed":
                return Animation.PlayMode.REVERSED;
            default:
                return Animation.PlayMode.NORMAL;
        }
    }

    private Array<TextureRegion> getAnimationFrames(Texture texture, JsonValue.JsonIterator framesIt)
    {
        Array<TextureRegion> regions = new Array<>();

        while(framesIt.hasNext())
        {
            JsonValue frame = framesIt.next();

            JsonValue frameBounds = frame.get("frame");

            int x = frameBounds.getInt("x");
            int y = frameBounds.getInt("y");

            int width = frameBounds.getInt("w");
            int height = frameBounds.getInt("h");

            regions.add(new TextureRegion(texture, x, y, width, height));
        }

        return regions;
    }


    private Array<TextureRegion> getAnimationFrames(Texture texture, JsonValue.JsonIterator framesIt, boolean reverse)
    {
        Array<TextureRegion> regions = new Array<>();

        while(framesIt.hasNext())
        {
            JsonValue frame = framesIt.next();

            JsonValue frameBounds = frame.get("frame");

            int x = frameBounds.getInt("x");
            int y = frameBounds.getInt("y");

            int width = frameBounds.getInt("w");
            int height = frameBounds.getInt("h");

            TextureRegion frameTextureRegion = new TextureRegion(texture, x, y, width, height);

            if(reverse)
            {
                frameTextureRegion.flip(true, false);
            }

            regions.add(frameTextureRegion);
        }

        return regions;
    }

    private String stripExtension(String fileName)
    {
        if(fileName == null)
        {
            return null;
        }

        int pos = fileName.lastIndexOf(".");

        if(pos == -1)
        {
            return fileName;
        }

        return fileName.substring(0, pos);
    }

    static public class EntityParameter extends AssetLoaderParameters<EntityData>
    {
    }
}
