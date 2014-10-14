package com.eddie.executiveexperience.Scripting;

import com.badlogic.gdx.utils.Disposable;
import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

public abstract class ScriptedGameActor extends GameActor implements Disposable
{
    LUAScript script;

    public ScriptedGameActor(GameStage gameStage, String scriptFile)
    {
        super(gameStage);

        script = new LUAScript(scriptFile);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        script.update();

        script.runScriptFunction("act", this);
    }
}
