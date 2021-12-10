package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.samb.trs.Components.CollisionComponent;
import com.samb.trs.Utilities.Mappers;

public class CollisionContactListener implements ContactListener {
    public CollisionContactListener() {
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity) fa.getBody().getUserData();
            entityCollision(ent, fb, contact);
        } else if (fb.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity) fb.getBody().getUserData();
            entityCollision(ent, fa, contact);
        }
    }

    private void entityCollision(Entity ent, Fixture fb, Contact contact) {
        if (fb.getBody().getUserData() instanceof Entity) {
            Entity colEnt = (Entity) fb.getBody().getUserData();

            CollisionComponent col = Mappers.collision.get(ent);
            CollisionComponent colb = Mappers.collision.get(colEnt);

            if (col != null) {
                col.collisionEntity = colEnt;
                col.contact = contact;
            } else if (colb != null) {
                colb.collisionEntity = ent;
                colb.contact = contact;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}