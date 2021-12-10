package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CheckOutsideComponent implements Component, Pool.Poolable {
    public Viewport viewport;
    public Vector2 offsetX;
    public Vector2 offsetY;

    public CheckOutsideComponent() {
        this.offsetX = new Vector2();
        this.offsetY = new Vector2();
    }

    @Override
    public void reset() {
        viewport = null;
        offsetX.setZero();
        offsetY.setZero();
    }
}
