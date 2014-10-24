package com.eddie.executiveexperience.Scripting;

public class ScriptCore
{
    protected static ScriptCore instance;

    public ScriptCore()
    {
    }

    public static ScriptCore getInstance()
    {
        if(instance == null)
        {
            instance = new ScriptCore();
        }

        return instance;
    }

    public void executeFunction(String script, String functionName, Object... objs)
    {
    }

    public Object executeFunction(String script, String functionName, Class returnType, Object... objs)
    {
        return null;
    }
}
