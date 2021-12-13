package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.CollectComponent;
import com.samb.trs.Factories.EntityFactory;
import com.samb.trs.Resources.Particles;
import com.samb.trs.Utilities.Mappers;

public class CollectSystem extends IteratingSystem {
    private Array<Entity> entities;

    public CollectSystem() {
        super(Family.all(CollectComponent.class).get());
        entities = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            if (Mappers.collect.get(entity).isCollected) {
                if (Mappers.death.has(entity)) {
                    Mappers.death.get(entity).isDead = true;
                }
            }
        }

        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }
}
