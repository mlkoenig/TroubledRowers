package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.AttachedComponent;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.ParticleEffectComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Utilities.Mappers;

public class AttachedSystem extends IteratingSystem {

    private Array<Entity> tcQueue, particleQueue, bodyQueue;

    @SuppressWarnings("unchecked")
    public AttachedSystem() {
        super(Family.all(AttachedComponent.class).one(TransformComponent.class, BodyComponent.class, ParticleEffectComponent.class).get());
        this.tcQueue = new Array<>();
        this.particleQueue = new Array<>();
        this.bodyQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity bodyEntity : bodyQueue) {
            AttachedComponent ac = Mappers.attached.get(bodyEntity);
            BodyComponent bodyComp = Mappers.body.get(bodyEntity);

            if (Mappers.body.has(ac.attachedTo)) {
                BodyComponent bc = Mappers.body.get(ac.attachedTo);
                bodyComp.body.setTransform(
                        bc.body.getPosition().x + ac.offset.x,
                        bc.body.getPosition().y + ac.offset.y,
                        bodyComp.body.getAngle()
                );
                bodyComp.body.setLinearVelocity(bc.body.getLinearVelocity());
                bodyComp.body.setLinearDamping(bc.body.getLinearDamping());
                bodyComp.body.setAngularVelocity(bc.body.getAngularVelocity());
                bodyComp.body.setAngularDamping(bc.body.getAngularDamping());
            } else if(Mappers.transform.has(ac.attachedTo)) {
                TransformComponent tc = Mappers.transform.get(ac.attachedTo);
                bodyComp.body.setTransform(
                        (tc.position.x + ac.offset.x),
                        (tc.position.y + ac.offset.y),
                        bodyComp.body.getAngle()
                );
            }
        }

        for (Entity tfEntity : tcQueue) {
            AttachedComponent ac = Mappers.attached.get(tfEntity);
            TransformComponent transformComponent = Mappers.transform.get(tfEntity);

            if (Mappers.body.has(ac.attachedTo)) {
                BodyComponent bc = Mappers.body.get(ac.attachedTo);
                transformComponent.position.set(
                        bc.body.getPosition().x + ac.offset.x,
                        bc.body.getPosition().y + ac.offset.y
                );
            } else if (Mappers.transform.has(ac.attachedTo)) {
                TransformComponent tc = Mappers.transform.get(ac.attachedTo);
                transformComponent.position.set(new Vector2(
                        tc.position.x + ac.offset.x,
                        tc.position.y + ac.offset.y
                        )
                );
            }
        }

        for (Entity particle : particleQueue) {
            AttachedComponent ac = Mappers.attached.get(particle);
            ParticleEffectComponent pec = Mappers.peCom.get(particle);
            if (Mappers.body.has(ac.attachedTo)) {
                BodyComponent bc = Mappers.body.get(ac.attachedTo);
                pec.particleEffect.setPosition(
                        bc.body.getPosition().x + ac.offset.x,
                        bc.body.getPosition().y + ac.offset.y
                );
            } else if (Mappers.transform.has(ac.attachedTo)) {
                TransformComponent tc = Mappers.transform.get(ac.attachedTo);
                pec.particleEffect.setPosition(
                        tc.position.x + ac.offset.x,
                        tc.position.y + ac.offset.y
                );
            }
        }

        tcQueue.clear();
        bodyQueue.clear();
        particleQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Mappers.attached.get(entity).isAttached) {
            if(Mappers.transform.has(entity)) tcQueue.add(entity);
            if(Mappers.body.has(entity)) bodyQueue.add(entity);
            if(Mappers.peCom.has(entity)) particleQueue.add(entity);
        }
    }
}