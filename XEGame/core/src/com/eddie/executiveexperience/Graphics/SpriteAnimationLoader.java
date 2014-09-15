package com.eddie.executiveexperience.Graphics;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.badlogic.gdx.utils.Logger;
import com.eddie.executiveexperience.Env;

public class SpriteAnimationLoader extends AsynchronousAssetLoader<SpriteAnimationData, SpriteAnimationLoader.AnimationParameter>
{
    static public class AnimationParameter extends AssetLoaderParameters<SpriteAnimationData>
    {
    }

    private SpriteAnimationData animationData = null;
    private Logger logger;

    /**
     * Creates a new AnimationLoader
     *
     * @param resolver file resolver to be used
     */
    public SpriteAnimationLoader(FileHandleResolver resolver)
    {
        super(resolver);

        animationData = null;
        logger = new Logger("Animation", Env.debugLevel);
    }

    /**
     * Aynchronously loads the animation data animations
     */
    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter)
    {
        logger.info("loading " + fileName);

        animationData = new SpriteAnimationData();

        // Retrieve texture
        animationData.texture = manager.get(stripExtension(fileName) + ".png", Texture.class);

        try
        {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);

            animationData.texture = manager.get(root.getString("textureFile"), Texture.class);
            animationData.frameDuration = root.getFloat("frameDuration");

            JsonValue animations = root.get("animations");
            JsonIterator animationsIt = animations.iterator();
            boolean first = true;

            while(animationsIt.hasNext())
            {
                JsonValue animationValue = animationsIt.next();

                String name = animationValue.getString("name");

                boolean createReverse = animationValue.getBoolean("createReverse");

                JsonValue frames = animationValue.get("frames");
                JsonIterator framesIt = frames.iterator();

                Animation animation = new Animation(animationData.frameDuration,
                        getAnimationFrames(animationData.texture, framesIt),
                        getPlayMode(animationValue.getString("mode", "normal")));
                animationData.animations.put(name, animation);

                logger.info("Loaded animation " + name + " from " + fileName + ".");

                if(first)
                {
                    animationData.defaultAnimation = animation;
                    first = false;
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Error loading " + fileName + ".");

            e.printStackTrace();
        }
    }

    /**
     * Retrieves the animation data as it is (without loading anything, this is strictly asynchronous)
     */
    @Override
    public SpriteAnimationData loadSync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter)
    {
        return animationData;
    }

    /**
     * Gets animation data dependencies, this is, the spreadsheet texture to load
     */
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationParameter parameter)
    {
        Array<AssetDescriptor> dependencies = new Array<>();
        dependencies.add(new AssetDescriptor<>(stripExtension(fileName) + ".png", Texture.class));

        return dependencies;
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

    private PlayMode getPlayMode(String mode)
    {
        switch(mode)
        {
            case "normal":
                return PlayMode.NORMAL;
            case "loop":
                return PlayMode.LOOP;
            case "loop_pingpong":
                return PlayMode.LOOP_PINGPONG;
            case "loop_random":
                return PlayMode.LOOP_RANDOM;
            case "loop_reversed":
                return PlayMode.LOOP_REVERSED;
            case "reversed":
                return PlayMode.REVERSED;
            default:
                return PlayMode.NORMAL;
        }
    }

    private Array<TextureRegion> getAnimationFrames(Texture texture, JsonIterator framesIt)
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
}