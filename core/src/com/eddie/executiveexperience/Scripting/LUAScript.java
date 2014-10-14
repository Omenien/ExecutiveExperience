package com.eddie.executiveexperience.Scripting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class LUAScript
{
    public LuaState luaState;
    private String fileName;

    /**
     * Constructor
     *
     * @param fileName File name with Lua script.
     */
    public LUAScript(final String fileName)
    {
        this.luaState = LuaStateFactory.newLuaState();
        this.luaState.openLibs();
        FileHandle handle = Gdx.files.internal(fileName);
        String file = handle.readString();
        this.luaState.LdoString(file);
        this.fileName = fileName;
    }

    public void update()
    {
        FileHandle handle = Gdx.files.internal(this.fileName);
        String file = handle.readString();
        this.luaState.LdoString(file);
    }

    /**
     * Ends the use of Lua environment.
     */
    public void closeScript()
    {
        this.luaState.close();
    }

    /**
     * Call a Lua function inside the Lua script to insert
     * data into a Java object passed as parameter
     *
     * @param functionName Name of Lua function.
     * @param objs
     */
    public void runScriptFunction(String functionName, Object... objs)
    {
        this.luaState.getGlobal(functionName);

        for(Object obj : objs)
        {
            this.luaState.pushJavaObject(obj);
        }

        this.luaState.call(1, 0);
    }

    public String getGlobalString(String globalName)
    {
        this.luaState.getGlobal(globalName);

        return this.luaState.toString(luaState.getTop());
    }
}