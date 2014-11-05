package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.resources.ResourceManager;

public class HomeStage extends Stage
{
    private ResourceManager assetManager;

    public HomeStage()
    {
        Gdx.input.setInputProcessor(this);

        SceneLoader menuLoader = new SceneLoader();

        menuLoader.loadScene("UIScene");

        HomeScreenScript menuScript = new HomeScreenScript();

        menuLoader.sceneActor.addScript(menuScript);

        addActor(menuLoader.sceneActor);

    }
}
