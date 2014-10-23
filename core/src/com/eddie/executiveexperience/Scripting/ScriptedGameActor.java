package com.eddie.executiveexperience.Scripting;

import com.eddie.executiveexperience.Entity.GameActor;
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
            System.out.println(script.executeFunction("act", String.class, this));
        }

        super.act(delta);
    }

    public void reloadScript()
    {
        script.reloadScript();
    }
}
