package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DeathComponent implements Component, Pool.Poolable {
    public boolean isDead;

    @Override
    public void reset() {
        isDead = false;
    }
}
