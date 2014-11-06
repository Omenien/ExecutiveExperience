package com.eddie.executiveexperience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class InputManager implements InputProcessor, ControllerListener
{
    public HashMap<Integer, Boolean> keysTyped = new HashMap<>();
    public HashMap<Integer, Boolean> keysDown = new HashMap<>();
    public HashMap<Integer, Boolean> buttonsPressed = new HashMap<>();

    protected Controller controller;
    protected boolean hasController;

    public InputManager()
    {
        hasController = false;

        if(Env.useController)
        {
            if(Controllers.getControllers().size > 0)
            {
                controller = Controllers.getControllers().first();

                hasController = true;

                Gdx.app.log("InputManager", "Using " + controller.getName());
            }
        }
    }

    public boolean isButtonPressed(int buttonCode, boolean makeFalse)
    {
        if(controller != null)
        {
            if(buttonsPressed.get(buttonCode) != null)
            {
                if(buttonsPressed.get(buttonCode) == true)
                {
                    if(makeFalse)
                    {
                        buttonsPressed.remove(buttonCode);
                        buttonsPressed.put(buttonCode, false);
                    }
                }
            }
            else
            {
                buttonsPressed.put(buttonCode, controller.getButton(buttonCode));
            }

            return buttonsPressed.get(buttonCode);
        }

        return false;
    }

    @Override
    public void connected(Controller controller)
    {
        if(this.controller == null)
        {
            this.controller = controller;

            hasController = true;

            Gdx.app.log("InputManager", "Using " + controller.getName());
        }
    }

    @Override
    public void disconnected(Controller controller)
    {
        Gdx.app.log("InputManager", "Controller disconnected");
    }

    @Override
    public boolean keyDown(int keycode)
    {
        keysTyped.remove(keycode);
        keysTyped.put(keycode, true);

        keysDown.remove(keycode);
        keysDown.put(keycode, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        keysDown.remove(keycode);

        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return true;
    }

    public boolean isKeyTyped(int keycode)
    {
        if(keysTyped.containsKey(keycode))
        {
            keysTyped.remove(keycode);

            return true;
        }
        return false;
    }

    public boolean isKeyDown(int keycode)
    {
        if(keysDown.containsKey(keycode))
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        if(controller.equals(this.controller))
        {
            buttonsPressed.put(buttonCode, true);
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        if(controller.equals(this.controller))
        {
            buttonsPressed.put(buttonCode, false);
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
