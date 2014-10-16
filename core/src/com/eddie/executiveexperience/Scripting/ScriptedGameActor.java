package com.eddie.executiveexperience.Scripting;

import com.eddie.executiveexperience.GameActor;
import com.eddie.executiveexperience.GameStage;

public abstract class ScriptedGameActor extends GameActor
{
    JSScript script;

    public ScriptedGameActor(GameStage gameStage)
    {
        super(gameStage);

        try
        {
            script = new JSScript("assets/scripts/" + getClass().getSimpleName() + ".js");
        }
        catch(Exception e)
        {
            script = null;
        }
    }

    @Override
    public void act(float delta)
    {
        if(script != null)
        {
            script.executeFunction("act", this);
        }

        super.act(delta);
    }
}
