package com.eddie.executiveexperience.Scripting;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JythonFactory
{
    protected PyObject object;

    public JythonFactory(String moduleName, String className)
    {
        PythonInterpreter interpreter = new PythonInterpreter();
    }
}

