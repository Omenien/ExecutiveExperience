package com.eddie.executiveexperience.Entity;

import com.eddie.executiveexperience.Constants;

public enum EnemyType
{
    SLIME_SMALL(0.5f, 0.5f, Constants.ENEMY_X, Constants.SLIME_SMALL_Y, Constants.ENEMY_DENSITY);

    private float width;
    private float height;
    private float x;
    private float y;
    private float density;

    EnemyType(float width, float height, float x, float y, float density)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.density = density;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getDensity()
    {
        return density;
    }
}
