package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Utilities.Mappers;

public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1 / 60f;
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 12, 6);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                TransformComponent tfm = Mappers.transform.get(entity);
                BodyComponent bodyComp = Mappers.body.get(entity);
                if (bodyComp.body != null) {
                    Vector2 position = bodyComp.body.getPosition();
                    tfm.position.x = position.x * Constants.Rendering.PPM;
                    tfm.position.y = position.y * Constants.Rendering.PPM;
                    tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;

                    // Update Body values in case the body is already removed
                    bodyComp.linear_vel.set(bodyComp.body.getLinearVelocity());
                    bodyComp.linear_damp = bodyComp.body.getLinearDamping();
                    bodyComp.angular_damp = bodyComp.body.getAngularDamping();
                    bodyComp.angular_vel = bodyComp.body.getAngularVelocity();
                }
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}