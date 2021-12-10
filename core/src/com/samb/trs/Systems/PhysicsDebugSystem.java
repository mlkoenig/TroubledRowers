package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;

public class PhysicsDebugSystem extends IteratingSystem {

    private Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera camera;

    public PhysicsDebugSystem(MainController mainController, World world) {
        super(Family.all().get());
        this.debugRenderer = mainController.getRenderController().getBox2DDebugRenderer();
        this.world = world;
        this.camera = mainController.getRenderController().getDynamicCamera();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        camera.combined.scl(Constants.Rendering.PPM);
        if (Constants.General.DEBUGGING)
            debugRenderer.render(world, camera.combined);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
