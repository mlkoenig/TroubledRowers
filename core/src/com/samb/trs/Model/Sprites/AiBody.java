package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.BodyEditorLoader;
import com.samb.trs.Utilities.Box2dAi.Box2dLocation;
import com.samb.trs.Utilities.Box2dAi.Box2dSteeringUtils;

public abstract class AiBody extends SpriteBody implements Steerable<Vector2> {

    public enum SteeringState {WANDER,SEEK,FLEE,ARRIVE,NONE} 	// a list of possible behaviours
    public SteeringState currentMode = SteeringState.WANDER; 	// stores which state the entity is currently in

    // Steering data
    protected float maxLinearSpeed = 2f;
    protected float maxLinearAcceleration = 5f;
    protected float maxAngularSpeed =50f;
    protected float maxAngularAcceleration = 5f;
    protected float zeroThreshold = 0.1f;

    protected SteeringBehavior<Vector2> steeringBehavior;
    protected SteeringAcceleration<Vector2> steeringAcceleration = new SteeringAcceleration<Vector2>(new Vector2());
    protected float boundingRadius;
    protected boolean tagged = true;
    protected boolean independentFacing = false;

    public AiBody(Texture texture, World world, BodyEditorLoader bodyEditorLoader, float px, float py, float pwidth, float pheight){
        super(texture, world, bodyEditorLoader, px, py, pwidth, pheight);
        boundingRadius = pwidth / PPM / 2f;
    }

    public AiBody(TextureRegion region, World world, BodyEditorLoader bodyEditorLoader, float px, float py, float pwidth, float pheight){
        super(region, world, bodyEditorLoader, px, py, pwidth, pheight);
        boundingRadius = pwidth / PPM / 2f;
    }

    public AiBody(MainController mainController, TextureRegions region, World world, float px, float py, float pwidth, float pheight) {
        super(mainController, region, world, px, py, pwidth, pheight);
        boundingRadius = pwidth / PPM / 2f;
    }

    public void reset() {
        super.reset();
        currentMode = SteeringState.NONE;
        steeringBehavior = null;
    }

    public boolean isIndependentFacing () {
        return independentFacing;
    }

    public void setIndependentFacing (boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    /** Call this to update the steering behaviour (per frame)
     * @param delta delta time between frames
     */
    public void update (float delta) {
        super.update(delta);
        if (steeringBehavior != null && !isRemoved()) {
            steeringBehavior.calculateSteering(steeringAcceleration);
            applySteering(steeringAcceleration, delta);
        }
    }

    /** apply steering to the Box2d body
     * @param steering the steering vector
     * @param deltaTime teh delta time
     */
    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringAcceleration.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(steeringAcceleration.linear, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringAcceleration.angular != 0) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringAcceleration.angular, true);
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
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
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
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
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

    public static float calculateOrientationFromLinearVelocity (Steerable<Vector2> character) {
        // If we haven't got any velocity, then we can do nothing.
        if (character.getLinearVelocity().isZero(character.getZeroLinearSpeedThreshold()))
            return character.getOrientation();

        return character.vectorToAngle(character.getLinearVelocity());
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }
    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringAcceleration(SteeringAcceleration<Vector2> steeringAcceleration) {
        this.steeringAcceleration = steeringAcceleration;
    }

    public SteeringAcceleration<Vector2> getSteeringAcceleration() {
        return steeringAcceleration;
    }
}
