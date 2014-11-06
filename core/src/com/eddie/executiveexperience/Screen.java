package com.eddie.executiveexperience;

import com.badlogic.gdx.ScreenAdapter;

public abstract class Screen extends ScreenAdapter
{
    public abstract void processInput(InputManager inputManager);
}
