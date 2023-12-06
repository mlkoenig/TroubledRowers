package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.FollowComponent;
import com.samb.trs.Components.SteeringComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Utilities.Box2dAi.CustomPrioritySteering;
import com.samb.trs.Utilities.Mappers;

import static com.samb.trs.Resources.Constants.Rendering.TIMESTEP;

public class PhysicsSystem extends IteratingSystem {
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;
    private Array<Entity> steeringQueue;

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<>();
        this.steeringQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIMESTEP) {

            // Update steering
            GdxAI.getTimepiece().update(TIMESTEP);
            updateSteering(TIMESTEP);

            // Take a step
            world.step(TIMESTEP, 8, 3);
            accumulator -= TIMESTEP;

            // Sync box2d with component system
            updateBodies();
        }

        steeringQueue.clear();
        bodiesQueue.clear();
    }

    private void updateSteering(float deltaTime) {
        for (Entity entity : steeringQueue){
            SteeringComponent steer = Mappers.steering.get(entity);
            if (Mappers.follow.has(entity) && steer.steeringBehavior instanceof CustomPrioritySteering) {
                FollowComponent fc = Mappers.follow.get(entity);
                fc.followTimer.update(deltaTime);
                if (!fc.followed && ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPlayer() != null &&
                        ((SteeringComponent) ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPlayer()).body.getPosition().dst2(steer.body.getPosition()) <= 36.0f) {
                    ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPursue().setEnabled(true);
                    fc.followed = true;
                    fc.followTimer.stop();
                }

                if (((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPursue() != null &&
                        ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPursue().isEnabled() && fc.followTimer.passed(fc.maxFollowTime)) {
                    ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPursue().setEnabled(false);
                }
            }

            steer.update(deltaTime);
        }
    }

    private void updateBodies() {
        for (Entity entity : bodiesQueue) {
            TransformComponent tfm = Mappers.transform.get(entity);
            BodyComponent bodyComp = Mappers.body.get(entity);
            if (bodyComp.body != null) {
                Vector2 position = bodyComp.body.getPosition();
                tfm.position.x = position.x;
                tfm.position.y = position.y;
                tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;

                // Update Body values in case the body is already removed
                bodyComp.linear_vel.set(bodyComp.body.getLinearVelocity());
                bodyComp.linear_damp = bodyComp.body.getLinearDamping();
                bodyComp.angular_damp = bodyComp.body.getAngularDamping();
                bodyComp.angular_vel = bodyComp.body.getAngularVelocity();
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);

        if (Mappers.steering.has(entity)) {
            steeringQueue.add(entity);
        }
    }
}