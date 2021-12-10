package com.samb.trs.Controllers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Factories.EntityFactory;
import com.samb.trs.Interfaces.Updatable;
import com.samb.trs.Model.Score;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Systems.CameraSystem;
import com.samb.trs.Systems.EventInputProcessor;
import com.samb.trs.Systems.GameLogicSystem;
import com.samb.trs.Systems.SpawnSystem;
import com.samb.trs.Utilities.Mappers;

import static com.samb.trs.Resources.Constants.Camera.START_VELOCITY;

public class GameWorldController extends BaseController implements Updatable {
    private World world;
    private EngineController engineController;
    private PooledEngine engine;
    private GameLogicSystem gameLogicSystem;
    private Score score;
    private EventInputProcessor eventInputProcessor;

    public GameWorldController(MainController mainController) {
        super(mainController);
        this.engineController = new EngineController(mainController);
        this.world = engineController.getWorld();
        this.engine = engineController.getEngine();
        this.gameLogicSystem = engineController.getGameLogicSystem();
        this.score = engineController.getScore();
        this.eventInputProcessor = engineController.getEventInputProcessor();
        resetGameWorld();
    }

    public void resetGameWorld() {
        engine.removeAllEntities();
        score.reset();
        getMain().getRenderController().getDynamicCamera().position.set(0, 0, 0);
        EntityFactory entityFactory = EntityFactory.getInstance(getMain(), engine, world);

        Entity cameraEntity = entityFactory.makeCameraEntity(getMain().getRenderController().getDynamicCamera());

        engine.getSystem(SpawnSystem.class).initializeQuadrants(cameraEntity);

        engine.addEntity(cameraEntity);
        engine.addEntity(entityFactory.makeBackground(cameraEntity));

        engine.addEntity(entityFactory.makeLeftShoreEntity(0));
        engine.addEntity(entityFactory.makeLeftShoreEntity(Constants.Rendering.WorldHeight));
        engine.addEntity(entityFactory.makeRightShoreEntity(0));
        engine.addEntity(entityFactory.makeRightShoreEntity(Constants.Rendering.WorldHeight));

        Entity player = entityFactory.makePlayerEntity(0, 0);
        eventInputProcessor.setTransformComponent(Mappers.transform.get(player));
        eventInputProcessor.reset();

        Gdx.input.setInputProcessor(eventInputProcessor);

        engine.addEntity(player);
        engine.getSystem(SpawnSystem.class).setPlayer(player);
        engine.getSystem(CameraSystem.class).setVelocity(START_VELOCITY);
        engine.getSystem(CameraSystem.class).getVelocityIncreaseTimer().reset();
        engine.getSystem(SpawnSystem.class).getTimeController().reset();
    }

    public void update(float dt) {
        engineController.update(dt);
    }

    @Override
    public void dispose() {
        engineController.dispose();
        eventInputProcessor.reset();
    }
}
