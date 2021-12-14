package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.DeathComponent;
import com.samb.trs.Factories.EntityFactory;
import com.samb.trs.Resources.Particles;
import com.samb.trs.Utilities.Mappers;

import static com.samb.trs.Resources.Constants.Collision.FISH_BIT;


public class DeathSystem extends IteratingSystem {
    private Array<Entity> entities;
    private EntityFactory entityFactory;

    public DeathSystem() {
        super(Family.all(DeathComponent.class).get());
        entities = new Array<>();
        entityFactory = EntityFactory.getInstance();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            if (Mappers.death.get(entity).isDead) {
                entity.removeAll();
                getEngine().removeEntity(entity);
            }
        }

        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }
}
