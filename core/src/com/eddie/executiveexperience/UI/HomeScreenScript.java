package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eddie.executiveexperience.Game;
import com.eddie.executiveexperience.GameScreen;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.ImageItem;
import com.uwsoft.editor.renderer.script.IScript;

public class HomeScreenScript implements IScript
{
    public HomeScreenScript()
    {
    }

    @Override
    public void init(CompositeItem item)
    {
        ImageItem playBtn = item.getImageById("playBtn");

        playBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(Game.instance.gameScreen == null)
                {
                    Game.instance.gameScreen = new GameScreen();
                }

                Game.instance.setScreen(Game.instance.getGameScreen());
            };
        });
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public void act(float delta)
    {
    }
}
