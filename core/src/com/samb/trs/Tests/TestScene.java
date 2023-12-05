package com.samb.trs.Tests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Components.*;
import com.samb.trs.Controllers.EngineController;
import com.samb.trs.Controllers.GameWorldController;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Factories.EntityFactory;
import com.samb.trs.GameApplication;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Systems.CameraSystem;
import com.samb.trs.Systems.GameOverSystem;
import com.samb.trs.Systems.SpawnSystem;
import com.samb.trs.Utilities.Box2dAi.CustomPrioritySteering;
import com.samb.trs.Utilities.Box2dAi.SteeringPresets;
import com.samb.trs.Utilities.Mappers;

public class TestScene extends GameApplication {
    private MainController mainController;
    private GLProfiler glProfiler;
    private float drawCalls, textureBinds, fps, shaderSwitches;


    @Override
    public void create () {
        this.mainController = new MainController(this);
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();

        GameWorldController worldController = mainController.getGameWorldController();
        EngineController engineController = worldController.getEngineController();

        World world = worldController.getEngineController().getWorld();

        EntityFactory entityFactory = EntityFactory.getInstance();
        Entity fish = entityFactory.makeFishEntity(250, 400, mainController.getRenderController().getDynamicViewport());

        SteeringComponent stc = Mappers.steering.get(fish);
        stc.currentMode = SteeringComponent.SteeringState.WANDER;
        CustomPrioritySteering<Vector2> steering = new CustomPrioritySteering<>(stc, null);
        steering.setEpsilon(0.0001f);
        //steering.add(SteeringPresets.getCollisionAvoidance(stc, world, 3));
        //steering.add(SteeringPresets.getRayCastObstacleAvoidance(stc, world, 2));
        steering.add(SteeringPresets.getWander(stc));
        stc.steeringBehavior = steering;
        stc.setMaxLinearSpeed(13);
        stc.setMaxLinearAcceleration(10);
        stc.setMaxAngularAcceleration(2);
        stc.setMaxAngularSpeed(2);

        engineController.getEngine().addEntity(fish);

        engineController.getEngine().getSystem(CameraSystem.class).setProcessing(false);
        engineController.getEngine().getSystem(SpawnSystem.class).setProcessing(false);
        engineController.getEngine().getSystem(GameOverSystem.class).setProcessing(false);
    }

    @Override
    public void resize(int width, int height) {
        mainController.getRenderController().resize(width, height);
    }

    @Override
    public void render () {
        glProfiler.reset();
        //mainController.getRenderController().render(6.105E-4f);
        mainController.getRenderController().render(Gdx.graphics.getDeltaTime());
        drawCalls = glProfiler.getDrawCalls();
        textureBinds = glProfiler.getTextureBindings();
        fps = Gdx.graphics.getFramesPerSecond();
        shaderSwitches = glProfiler.getShaderSwitches();

//		if (Constants.General.DEBUGGING)
//			System.out.println("DrawCalls: " + drawCalls +
//					"\n TextureBinds: " + textureBinds +
//					"\n ShaderSwitches: " + shaderSwitches +
//					"\n FPS: " + fps);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose () {
        mainController.dispose();
        glProfiler.disable();
    }
}
