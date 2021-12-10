package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class RowingComponent implements Component, Pool.Poolable {
    public Entity paddle, man;
    public PaddleDirection direction;
    public float frequency;
    public boolean paddleSoundActive;
    public Array<Float> posX;

    public RowingComponent() {
        direction = PaddleDirection.MIDDLE;
        frequency = 0;
        paddleSoundActive = false;
        posX = new Array<>();
    }

    @Override
    public void reset() {
        paddle = null;
        man = null;
        direction = PaddleDirection.MIDDLE;
        frequency = 0;
        paddleSoundActive = false;
        posX.clear();
    }

    public enum PaddleDirection {RIGHT, LEFT, MIDDLE}
}
