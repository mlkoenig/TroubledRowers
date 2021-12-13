package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.samb.trs.Components.AttachedComponent;
import com.samb.trs.Components.ParticleEffectComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Utilities.Mappers;

public class ParticleEffectSystem extends IteratingSystem {

    public ParticleEffectSystem() {
        super(Family.all(ParticleEffectComponent.class, AttachedComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleEffectComponent pec = Mappers.peCom.get(entity);
        AttachedComponent ac = Mappers.attached.get(entity);

        if(pec.isDead){
            pec.timeTilDeath -= deltaTime;
        }

        // Move PE if attached
        if (ac.isAttached && ac.attachedTo != null) {
            TransformComponent tca = Mappers.transform.get(ac.attachedTo);
            pec.particleEffect.setPosition(
                    tca.position.x + ac.offset.x,
                    tca.position.y + ac.offset.y
            );
        }

        // free PE if completed
        if(pec.particleEffect.isComplete() || pec.timeTilDeath <= 0){
            getEngine().removeEntity(entity);
        }
    }
}