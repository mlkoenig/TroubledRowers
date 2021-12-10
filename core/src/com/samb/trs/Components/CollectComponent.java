package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class CollectComponent implements Component, Pool.Poolable {
    public boolean isCollected;

    public CollectComponent() {
        this.isCollected = false;
    }

    @Override
    public void reset() {
        isCollected = false;
    }
}
