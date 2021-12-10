package com.samb.trs.Controllers;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Interfaces.Updatable;
import com.samb.trs.Model.Score;
import com.samb.trs.Systems.*;

public class EngineController extends BaseController implements Updatable {
    private PooledEngine engine;
    private World world;
    private EventInputProcessor eventInputProcessor;
    private Score score;
    private GameLogicSystem gameLogicSystem;

    public EngineController(MainController mainController) {
        super(mainController);
        this.score = new Score(mainController);
        this.world = new World(new Vector2(), true);
        world.setContactListener(new CollisionContactListener());

        this.engine = new PooledEngine(30, 50, 30, 50);
        this.eventInputProcessor = new EventInputProcessor(engine, mainController.getRenderController().getDynamicCamera(), mainController.getRenderController().getDynamicViewport());

        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new CameraSystem(mainController.getRenderController().getDynamicCamera()));
        engine.addSystem(new SpawnSystem(mainController, engine, world, mainController.getRenderController().getDynamicViewport()));
        engine.addSystem(new ShoreSystem());
        engine.addSystem(new MouseControlSystem(mainController, eventInputProcessor));
        engine.addSystem(new AttachedSystem());
        engine.addSystem(new ShieldSystem(mainController, eventInputProcessor, score));
        engine.addSystem(new PlayerControlSystem(eventInputProcessor));
        engine.addSystem(new CheckOutsideSystem());
        engine.addSystem(new SteeringSystem());
        engine.addSystem(new RowingSystem(mainController));
        engine.addSystem(new CollisionSystem(mainController, score));
        engine.addSystem(new CollectSystem());
        engine.addSystem(new DeathSystem());
        engine.addSystem(new RenderingSystem(mainController));
        engine.addSystem(new PhysicsDebugSystem(mainController, world));

        gameLogicSystem = new GameLogicSystem();
        engine.addSystem(gameLogicSystem);
        gameLogicSystem.setGameState(GameLogicSystem.GameState.LOADING);
    }

    @Override
    public void update(float dt) {
        world.step(dt, 6, 3);
        eventInputProcessor.update(dt);
        engine.update(dt);
    }

    public PooledEngine getEngine() {
        return engine;
    }

    public World getWorld() {
        return world;
    }

    public EventInputProcessor getEventInputProcessor() {
        return eventInputProcessor;
    }

    public GameLogicSystem getGameLogicSystem() {
        return gameLogicSystem;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public void dispose() {
        world.dispose();
        engine.clearPools();
    }
}
