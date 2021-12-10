package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class QuadrantComponent implements Component, Pool.Poolable {
    public float width, height;

    @Override
    public void reset() {
        width = 0;
        height = 0;
    }
}
