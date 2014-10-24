package com.eddie.executiveexperience.Scripting;

import com.badlogic.gdx.Gdx;
import org.mozilla.javascript.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSScript
{
    protected String name;
    protected String filePath;
    protected String script;

    public JSScript(String filePath)
    {
        this.filePath = filePath;

        loadScript(filePath);
    }

    private void loadScript(String filePath)
    {
        StringBuilder sb = new StringBuilder();

        try
        {
            InputStream is = Gdx.files.internal(filePath).read();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while(line != null)
            {
                sb.append(line).append("\n");
                line = br.readLine();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        script = sb.toString();
    }

    public void executeFunction(String functionName, Object... objs)
    {
        ScriptCore.getInstance().executeFunction(script, functionName, objs);
    }

    public Object executeFunction(String functionName, Class returnType, Object... objs)
    {
        Object result = ScriptCore.getInstance().executeFunction(script, functionName, returnType, objs);

        return Context.jsToJava(result, returnType);
    }
}
