package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.PlayerComponent;

public class PlayerControlSystem extends IteratingSystem {
    private Array<Entity> entities;
    private EventInputProcessor eip;

    public PlayerControlSystem(EventInputProcessor eventInputProcessor) {
        super(Family.all(PlayerComponent.class).get());
        entities = new Array<>();
        this.eip = eventInputProcessor;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }
}
