package com.samb.trs.Utilities.Box2dAi;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector;

public class CustomPrioritySteering<T extends Vector<T>> extends PrioritySteering<T> {
    private Steerable<T> player;
    private Pursue<T> pursue;
    private Seek<T> seek;

    public CustomPrioritySteering(Steerable<T> owner, Steerable<T> player) {
        super(owner);
        this.player = player;
    }

    public Steerable<T> getPlayer() {
        return player;
    }

    @Override
    public PrioritySteering<T> add(SteeringBehavior<T> behavior) {
        if(behavior instanceof Pursue) {
            pursue = ((Pursue<T>) behavior);
        }else if(behavior instanceof Seek){
            seek = ((Seek<T>) behavior);
        }
        return super.add(behavior);
    }

    public Pursue<T> getPursue(){
        return pursue;
    }

    public Seek<T> getSeek() {
        return seek;
    }
}