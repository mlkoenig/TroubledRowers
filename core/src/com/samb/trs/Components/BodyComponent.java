package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class BodyComponent implements Component, Pool.Poolable {
    public Body body;
    public float linear_damp, angular_vel, angular_damp;
    public Vector2 linear_vel;
    public Vector2 contact;

    public BodyComponent() {
        linear_vel = new Vector2();
    }

    @Override
    public void reset() {
        // Remove body from the world on Component-Removal
        body.getWorld().destroyBody(body);
        body = null;
        linear_vel.setZero();
        linear_damp = 0;
        angular_damp = 0;
        angular_vel = 0;
    }
}
