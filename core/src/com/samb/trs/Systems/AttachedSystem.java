package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.AttachedComponent;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Controllers.AttachedController;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Utilities.Mappers;

public class AttachedSystem extends IteratingSystem {

    private Array<Entity> entityQueue;

    @SuppressWarnings("unchecked")
    public AttachedSystem() {
        super(Family.all(AttachedComponent.class).get());
        this.entityQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //Entity Queue
        for (Entity entity : entityQueue) {
            Entity attachedTo = Mappers.attached.get(entity).attachedTo;
            Vector2 offset = Mappers.attached.get(entity).offset;
            boolean isAttached = Mappers.attached.get(entity).isAttached;
            if (isAttached) {
                if (Mappers.body.has(entity)) {
                    BodyComponent bodyComp = Mappers.body.get(entity);
                    if (Mappers.body.has(attachedTo)) {
                        BodyComponent bc = Mappers.body.get(attachedTo);
                        bodyComp.body.setTransform(bc.body.getPosition().x + offset.x / Constants.Rendering.PPM, bc.body.getPosition().y + offset.y / Constants.Rendering.PPM, bodyComp.body.getAngle());
                        bodyComp.body.setLinearVelocity(bc.body.getLinearVelocity());
                        bodyComp.body.setLinearDamping(bc.body.getLinearDamping());
                        bodyComp.body.setAngularVelocity(bc.body.getAngularVelocity());
                        bodyComp.body.setAngularDamping(bc.body.getAngularDamping());
                    } else if (Mappers.transform.has(attachedTo)) {
                        TransformComponent tc = Mappers.transform.get(attachedTo);
                        bodyComp.body.setTransform(tc.position.x / Constants.Rendering.PPM, tc.position.y / Constants.Rendering.PPM, bodyComp.body.getAngle());
                    }
                } else if (Mappers.transform.has(attachedTo)) {
                    TransformComponent transComp = Mappers.transform.get(attachedTo);
                    TransformComponent tc = Mappers.transform.get(entity);
                    tc.position.set(transComp.position).add(offset);
                } else if (Mappers.body.has(attachedTo)) {
                    BodyComponent bc = Mappers.body.get(attachedTo);
                    TransformComponent tc = Mappers.transform.get(entity);
                    tc.position.set(bc.body.getPosition().scl(Constants.Rendering.PPM));
                }
            }
        }
        entityQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entityQueue.add(entity);
    }
}