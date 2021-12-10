package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.Pool;

public class MouseComponent implements Component, Pool.Poolable {
    public World world;
    public MouseJoint mouseJoint;
    public Vector2 offset;

    public MouseComponent() {
        offset = new Vector2();
    }

    @Override
    public void reset() {
        if (world != null) world.destroyJoint(mouseJoint);
        mouseJoint = null;
        world = null;
        offset.setZero();
    }
}