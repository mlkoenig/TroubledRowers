package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.math.Vector2;
import com.samb.trs.Components.FollowComponent;
import com.samb.trs.Components.SteeringComponent;
import com.samb.trs.Utilities.Box2dAi.CustomPrioritySteering;
import com.samb.trs.Utilities.Mappers;

public class SteeringSystem extends IteratingSystem {

    @SuppressWarnings("unchecked")
    public SteeringSystem() {
        super(Family.all(SteeringComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        GdxAI.getTimepiece().update(deltaTime);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SteeringComponent steer = Mappers.steering.get(entity);
        if (Mappers.follow.has(entity) && steer.steeringBehavior instanceof CustomPrioritySteering) {
            FollowComponent fc = Mappers.follow.get(entity);
            fc.followTimer.update(deltaTime);
            if (!fc.followed && ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPlayer() != null &&
                    ((SteeringComponent) ((CustomPrioritySteering<Vector2>) steer.steeringBehavior).getPlayer()).body.getPosition().dst2(steer.body.getPosition()) <= 150) {
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