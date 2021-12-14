package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class ParticleEffectComponent implements Component, Pool.Poolable {
    public ParticleEffectPool.PooledEffect particleEffect;
    public float timeTilDeath = 5f; // add a 1 second delay
    public boolean isDead = false;

    @Override
    public void reset() {
        particleEffect.free(); // free the pooled effect
        particleEffect = null; // empty this component's particle effect
        isDead = false;
        timeTilDeath = 5f;
    }
}