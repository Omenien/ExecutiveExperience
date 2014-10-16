package com.eddie.executiveexperience.Scripting;

import com.badlogic.gdx.Gdx;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSScript
{
    protected String name;
    protected String filePath;
    protected String script;

    protected Context cx;
    protected Scriptable scope;

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
        try
        {
            // Create and enter a Context. A Context stores information about the
            // execution environment of a script.
            cx = Context.enter();

            // Initialize the standard objects (Object, Function, etc.). This must be done before
            // scripts can be executed. The null parameter tells initStandardObjects to
            // create and return a scope object that we usein later calls.
            scope = cx.initStandardObjects();

            cx.evaluateString(scope, script, "script", 1, null);

            Function function = (Function) scope.get(functionName, scope);
            function.call(cx, scope, scope, objs);
        }
        finally
        {
            cx.exit();
        }
    }

    public Object executeFunction(String functionName, Class returnType, Object... objs)
    {
        try
        {
            // Create and enter a Context. A Context stores information about the
            // execution environment of a script.
            cx = Context.enter();

            // Initialize the standard objects (Object, Function, etc.). This must be done before
            // scripts can be executed. The null parameter tells initStandardObjects to
            // create and return a scope object that we usein later calls.
            scope = cx.initStandardObjects();

            cx.evaluateString(scope, script, "script", 1, null);

            Function function = (Function) scope.get(functionName, scope);
            Object result = function.call(cx, scope, scope, objs);
            System.out.println(Context.jsToJava(result, returnType));
        }
        finally
        {
            cx.exit();
        }

        return null;
    }
}
