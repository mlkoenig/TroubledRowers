package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {
    public Vector2 position;
    public float rotation;
    public float z;
    public boolean isHidden;

    public TransformComponent() {
        this.position = new Vector2();
    }

    @Override
    public void reset() {
        position.setZero();
        rotation = 0;
        z = 0;
        isHidden = false;
    }
}
