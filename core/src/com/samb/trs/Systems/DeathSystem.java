package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.DeathComponent;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Factories.EntityFactory;
import com.samb.trs.Resources.Particles;
import com.samb.trs.Utilities.Mappers;
import static com.samb.trs.Components.TypeComponent.*;


public class DeathSystem extends IteratingSystem {
    private Array<Entity> entities;
    private EntityFactory entityFactory;

    private MainController mainController;

    public DeathSystem(MainController mainController) {
        super(Family.all(DeathComponent.class).get());
        this.mainController = mainController;
        entities = new Array<>();
        entityFactory = EntityFactory.getInstance();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            if (Mappers.death.get(entity).isDead) {

                switch (Mappers.type.get(entity).type) {
                    case FISH:
                        mainController.getSaveController().getAccount().increaseFishDestroyed();
                        break;
                    case ROCK:
                        mainController.getSaveController().getAccount().increaseRocksDestroyed();
                }

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
