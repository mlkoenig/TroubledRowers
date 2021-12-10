package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.ShieldComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Score;
import com.samb.trs.Resources.Musics;
import com.samb.trs.Utilities.Mappers;

public class ShieldSystem extends IteratingSystem {
    private Array<Entity> entities;
    private EventInputProcessor eip;
    private Score score;
    private MainController mainController;

    public ShieldSystem(MainController mainController, EventInputProcessor eventInputProcessor, Score score) {
        super(Family.all(ShieldComponent.class).get());
        entities = new Array<>();
        this.eip = eventInputProcessor;
        this.score = score;
        this.mainController = mainController;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            ShieldComponent sc = Mappers.shield.get(entity);

            // Set protection with timer
            if (sc.isProtected && sc.shieldTimer.passed(sc.duration)) {
                sc.shieldTimer.reset();
                sc.isProtected = false;
                mainController.getSoundController().getMusic(Musics.SHIELD).stop();
            } else {
                sc.shieldTimer.update(deltaTime);
            }

            if ((!sc.isProtected || sc.shieldTimer.passedAbsolute(4 * sc.duration / 5f)) && eip.isSpaceDown() && score.getBoosts() > 0) {
                sc.isProtected = true;
                sc.shieldTimer.reset();
                score.decreaseBoosts();
                mainController.getSoundController().getMusic(Musics.SHIELD).play();
            }

            // Hide Entities if not protected
            TransformComponent tcShield = Mappers.transform.get(sc.shieldEntity);
            TransformComponent tcGrid = Mappers.transform.get(sc.gridEntity);

            fixturesToSensors(Mappers.body.get(sc.shieldEntity).body, !sc.isProtected);
            tcShield.isHidden = !sc.isProtected;
            tcGrid.isHidden = !sc.isProtected;
        }

        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

    private void fixturesToSensors(Body body, boolean sensor) {
        for (Fixture f : body.getFixtureList()) {
            f.setSensor(sensor);
        }
    }
}
