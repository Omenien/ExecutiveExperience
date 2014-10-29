package com.uwsoft.editor.renderer;


import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.resources.ResourceManager;

/**
 * Default Stage for Overlap2D that will take care of
 * Creating default scene loader, loading and simulating physics,
 * loading and simulating light system and creating skeleton renderer for spine animations
 * This class is intended to be extended
 *
 * @author Avetis Zakharyan | http://www.overlap2d.com
 */
public class Overlap2DStage extends Stage
{

    public SceneLoader sceneLoader;

    public Essentials essentials;

    private float timeAcc = 0;

    public boolean physiscStopped = false;

    public Overlap2DStage()
    {
        super();
        initStage();
    }

    public Overlap2DStage(Viewport viewPort)
    {
        super(viewPort);
        initStage();
    }

    /**
     * initializes empty holders for future data like essentials
     * to hold RayHandler for lights, and skeletonRenderer for spine animations
     */
    protected void initStage()
    {
        essentials = new Essentials();
    }

    /**
     * Loads particular scene into Actor as well as
     * initializes ambient light, physics bodies and adds root actor to the stage for instant rendering
     * It is recommended that you override this
     *
     * @param name - scene name as it is in file system without ".dt" extension
     */
    public void loadScene(String name)
    {
        sceneLoader.loadScene(name);

        addActor(sceneLoader.getRoot());
    }

    /**
     * Currently creates the Default ResourceManager,
     * loads all the assets that can possible be on every possible scene
     * And does it for default "orig" resolution only.
     * <p/>
     * IMPORTANT
     * This is a very default solution and it is extremely recommended
     * for you to override this for non test projects
     *
     * @see SceneLoader
     * @see com.uwsoft.editor.renderer.resources.ResourceManager
     */
    public void initSceneLoader()
    {
        ResourceManager rm = new ResourceManager();

        // Loading all assets/resources into memory
        rm.initAllResources();

        sceneLoader = new SceneLoader(essentials);
        essentials.rm = rm;
    }

    @Override
    public void act(float delta)
    {
        if(essentials.world != null && !physiscStopped)
        {
            // physics is enabled
            while(timeAcc < delta)
            {
                timeAcc += 1f / 60;
                essentials.world.step(1f / 60, 10, 10);
            }
            timeAcc -= delta;
        }

        super.act(delta);
    }

    @Override
    public void draw()
    {
        super.draw();
    }

    /**
     * @return Physics World
     */
    public World getWorld()
    {
        return essentials.world;
    }
}
