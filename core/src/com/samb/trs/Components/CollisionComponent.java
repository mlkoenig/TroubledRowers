package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Pool;

public class CollisionComponent implements Component, Pool.Poolable {
    public Entity collisionEntity;
    public Contact contact;

    @Override
    public void reset() {
        contact = null;
        collisionEntity = null;
    }
}
