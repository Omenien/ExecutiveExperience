package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eddie.executiveexperience.Game;
import com.uwsoft.editor.renderer.SceneLoader;

public class HomeStage extends Stage
{
    public HomeStage()
    {
        Game.instance.getInputMultiplexer().addProcessor(this);

        SceneLoader menuLoader = new SceneLoader();

        menuLoader.loadScene("UIScene");

        HomeScreenScript menuScript = new HomeScreenScript();

        menuLoader.sceneActor.addScript(menuScript);

        addActor(menuLoader.sceneActor);
    }
}
