package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class AttachedComponent implements Component, Pool.Poolable {
    public Entity attachedTo;
    public Vector2 offset;
    public boolean isAttached;

    public AttachedComponent() {
        offset = new Vector2();
        isAttached = true;
    }

    @Override
    public void reset() {
        attachedTo = null;
        offset.setZero();
        isAttached = true;
    }
}
