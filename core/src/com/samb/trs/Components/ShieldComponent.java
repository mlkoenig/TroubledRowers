package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.samb.trs.Model.Timer;

public class ShieldComponent implements Component, Pool.Poolable {
    public Entity shieldEntity, gridEntity;
    public boolean isProtected;
    public float duration;
    public Timer shieldTimer;

    public ShieldComponent() {
        shieldTimer = new Timer();
    }

    @Override
    public void reset() {
        shieldEntity = null;
        gridEntity = null;
        isProtected = false;
        duration = 0;
        shieldTimer.reset();
    }
}
