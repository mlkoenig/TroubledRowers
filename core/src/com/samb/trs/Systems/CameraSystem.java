package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.CameraComponent;
import com.samb.trs.Model.Timer;
import com.samb.trs.Utilities.Mappers;

import java.util.Locale;

import static com.samb.trs.Resources.Constants.*;

public class CameraSystem extends IteratingSystem {
    private OrthographicCamera camera;
    private Entity cameraEntity;
    private Timer velocityIncreaseTimer;
    private float velocity;
    private float diff;

    public CameraSystem(OrthographicCamera camera) {
        super(Family.all(CameraComponent.class, BodyComponent.class).get());
        this.camera = camera;
        velocity = Camera.START_VELOCITY;
        velocityIncreaseTimer = new Timer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        increaseVelocityOverTime(deltaTime);
        BodyComponent bodyComponent = Mappers.body.get(cameraEntity);
        bodyComponent.body.setLinearVelocity(0, velocity);
        float y = camera.position.y;
        camera.position.set(bodyComponent.body.getPosition().x * Rendering.PPM, bodyComponent.body.getPosition().y * Rendering.PPM, 0);
        diff = camera.position.y - y;
    }

    public void increaseVelocityOverTime(float dt) {
        velocityIncreaseTimer.update(dt);
        if (velocityIncreaseTimer.passed(Camera.VELOCITY_INCREASE_TIMER)) {
            velocity = Math.min(Camera.MAX_VELOCITY, velocity + Camera.VELOCITY_INCREASE);
            Gdx.app.log("Game", String.format(Locale.getDefault(), "Increased velocity to %f", velocity));
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        cameraEntity = entity;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getDiff() {
        return diff;
    }

    public Timer getVelocityIncreaseTimer() {
        return velocityIncreaseTimer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Entity getCameraEntity() {
        return cameraEntity;
    }
}
