package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.samb.trs.Utilities.Box2dAi.Box2dLocation;
import com.samb.trs.Utilities.Box2dAi.Box2dSteeringUtils;

public class SteeringComponent implements Steerable<Vector2>, Component, Pool.Poolable {

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2()); // this is the actual steering vector for our unit
    public SteeringState currentMode = SteeringState.NONE;    // stores which state the entity is currently in
    public Body body;    // stores a reference to our Box2D body
    public float orientationOffset;
    // target location. This will cause problems as our entities travel pretty fast and can easily over or undershoot this.)
    public SteeringBehavior<Vector2> steeringBehavior; // stores the action behaviour
    public float boundingRadius = 1f;   // the minimum radius size for a circle required to cover whole object
    // Steering data
    float maxLinearSpeed = 2f;    // stores the max speed the entity can go
    float maxLinearAcceleration = 5f;    // stores the max acceleration
    float maxAngularSpeed = 50f;        // the max turning speed
    float maxAngularAcceleration = 5f;// the max turning acceleration
    float zeroThreshold = 0.1f;    // how accurate should checks be (0.0000001f will mean the entity must get within 0.0000001f of
    private boolean tagged = true;        // This is a generic flag utilized in a variety of ways. (never used this myself)
    private boolean independentFacing = false; // defines if the entity can move in a direction other than the way it faces

    @Override
    public void reset() {
        currentMode = SteeringState.NONE;
        body = null;
        orientationOffset = 0;
        steeringBehavior = null;

    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    /**
     * Call this to update the steering behaviour (per frame)
     *
     * @param delta delta time between frames
     */
    public void update(float delta) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
        }
    }

    /**
     * apply steering to the Box2d body
     *
     * @param steering  the steering vector
     * @param deltaTime teh delta time
     */
    protected void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            // If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                setOrientation(newOrientation);
            }
        }

        if (anyAccelerations) {
            // Cap the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }
            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle() - orientationOffset;
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation + orientationOffset);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public enum SteeringState {WANDER, SEEK, FLEE, ARRIVE, CUSTOM, NONE}    // a list of possible behaviours
}