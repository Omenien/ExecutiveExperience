package com.eddie.executiveexperience.Scripting;

import org.python.util.PythonInterpreter;

public class ScriptCore
{
    protected static ScriptCore instance;

    protected PythonInterpreter pythonInterpreter = new PythonInterpreter();

    public ScriptCore()
    {
    }

    public static ScriptCore getInstance()
    {
        if (instance == null)
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

    public PythonInterpreter getPythonInterpreter()
    {
        return pythonInterpreter;
    }
}
