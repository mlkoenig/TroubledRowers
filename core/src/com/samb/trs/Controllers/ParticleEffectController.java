package com.samb.trs.Controllers;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.samb.trs.Resources.Particles;

import java.util.HashMap;

public class ParticleEffectController extends BaseController{

    private HashMap<Particles, ParticleEffect> particleEffects;
    private HashMap<Particles, ParticleEffectPool> pool;

    /**
     *  Particle Effect Manager for controlling creating pools and dispensing particle effects
     */
    public ParticleEffectController(MainController mainController){
        super(mainController);
        particleEffects = new HashMap<>();
        pool = new HashMap<>();
    }

    /** Create a particle effect pool for type  with default values (scale 1, pool init capacity 5, max capacity 20)
     * @param type int id of particle effect
     * @param party the particle effect
     */
    public void addParticleEffect(Particles type, ParticleEffect party){
        addParticleEffect(type, party, 1);
    }

    /** Create a particle effect pool for type with scale and default pool sizes
     * @param type int id of particle effect
     * @param party the particle effect
     * @param scale The particle effect scale
     */
    public void addParticleEffect(Particles type, ParticleEffect party, float scale ){
        addParticleEffect(type, party, scale,5,20);

    }

    /** Create a particle effect pool for type
     * @param type int id of particle effect
     * @param party the particle effect
     * @param scale The particle effect scale
     * @param startCapacity pool initial capacity
     * @param maxCapacity pool max capacity
     */
    public void addParticleEffect(Particles type, ParticleEffect party, float scale, int startCapacity, int maxCapacity){
        party.scaleEffect(scale);
        particleEffects.put(type, party);
        pool.put(type,new ParticleEffectPool(party, startCapacity, maxCapacity));

    }

    /**
     *  Get a particle effect of type type
     * @param type the type to get
     * @return The pooled particle effect
     */
    public ParticleEffectPool.PooledEffect getPooledParticleEffect(Particles type){
        return pool.get(type).obtain();
    }

    @Override
    public void dispose() {
        for (ParticleEffect p : particleEffects.values())
            p.dispose();

        for (ParticleEffectPool p : pool.values())
            p.clear();

        particleEffects.clear();
        pool.clear();
    }
}
