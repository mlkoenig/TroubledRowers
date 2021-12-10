package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.samb.trs.Model.Timer;

public class FollowComponent implements Component, Pool.Poolable {
    public Timer followTimer;
    public float maxFollowTime;
    public boolean followed;

    public FollowComponent() {
        followTimer = new Timer();
        maxFollowTime = 0;
        followed = false;
    }

    @Override
    public void reset() {
        followTimer.reset();
        maxFollowTime = 0;
        followed = false;
    }
}
