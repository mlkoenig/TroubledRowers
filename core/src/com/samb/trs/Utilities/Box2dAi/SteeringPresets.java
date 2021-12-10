package com.samb.trs.Utilities.Box2dAi;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SteeringPresets {

    public static Wander<Vector2> getWander(Steerable<Vector2> scom) {
        Wander<Vector2> wander = new Wander<Vector2>(scom)
                .setFaceEnabled(false)
                .setWanderOffset(5)
                .setWanderOrientation(0)
                .setWanderRadius(3)
                .setWanderRate(MathUtils.PI2 * 2);
        return wander;
    }

    public static Seek<Vector2> getSeek(Steerable<Vector2> seeker, Location<Vector2> target) {
        Seek<Vector2> seek = new Seek<Vector2>(seeker,target);
        return seek;
    }

    public static Flee<Vector2> getFlee(Steerable<Vector2> runner, Location<Vector2> fleeingFrom) {
        Flee<Vector2> seek = new Flee<Vector2>(runner,fleeingFrom);
        return seek;
    }

    public static Arrive<Vector2> getArrive(Steerable<Vector2> runner, Location<Vector2> target) {
        Arrive<Vector2> arrive = new Arrive<>(runner, target)
                .setTimeToTarget(0.1f) // default 0.1f
                .setArrivalTolerance(7f) //
                .setDecelerationRadius(10f);

        return arrive;
    }

    public static Pursue<Vector2> getPursue(Steerable<Vector2> seeker, Steerable<Vector2> target) {
        return new Pursue<>(seeker, target)
                .setMaxPredictionTime(.2f);
    }

    public static CollisionAvoidance<Vector2> getCollisionAvoidance(Steerable<Vector2> owner, World world, float detectionRadius) {
        return new CollisionAvoidance<>(owner, new Box2dRadiusProximity(owner, world, detectionRadius));
    }

    public static RaycastObstacleAvoidance<Vector2> getRayCastObstacleAvoidance(Steerable<Vector2> owner, World world, int rayConfigurationIndex) {
        @SuppressWarnings("unchecked")
        RayConfigurationBase<Vector2>[] localRayConfigurations = new RayConfigurationBase[] {
                new SingleRayConfiguration<Vector2>(owner, 50),
                new ParallelSideRayConfiguration<Vector2>(owner, 50, owner.getBoundingRadius()),
                new CentralRayWithWhiskersConfiguration<Vector2>(owner, 50, 25, 35 * MathUtils.degreesToRadians)};
        RaycastCollisionDetector<Vector2> raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        return new RaycastObstacleAvoidance<Vector2>(owner, localRayConfigurations[rayConfigurationIndex],
                raycastCollisionDetector, 4);
    }
}