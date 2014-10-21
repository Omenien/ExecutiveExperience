package com.eddie.executiveexperience.Scripting;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class ScriptCore
{
    protected static ScriptCore instance;

    protected Context cx;
    protected Scriptable scope;

    public ScriptCore()
    {
        // Create and enter a Context. A Context stores information about the
        // execution environment of a script.
        cx = Context.enter();

        // Initialize the standard objects (Object, Function, etc.). This must be done before
        // scripts can be executed. The null parameter tells initStandardObjects to
        // create and return a scope object that we usein later calls.
        scope = cx.initStandardObjects();
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
        cx.evaluateString(scope, script, "script", 1, null);

        Function function = (Function) scope.get(functionName, scope);
        function.call(cx, scope, scope, objs);
    }

    public Object executeFunction(String script, String functionName, Class returnType, Object... objs)
    {
        cx.evaluateString(scope, script, "script", 1, null);

        Function function = (Function) scope.get(functionName, scope);
        Object result = function.call(cx, scope, scope, objs);
        return Context.jsToJava(result, returnType);
    }
}
