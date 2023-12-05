package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.*;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Utilities.Mappers;

import static com.samb.trs.Components.TypeComponent.FISH;

public class PhysicsDebugSystem extends IteratingSystem {

    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private World world;
    private OrthographicCamera camera;
    private Array<Entity> entities;

    public PhysicsDebugSystem(MainController mainController, World world) {
        super(Family.all(SteeringComponent.class).get());
        this.debugRenderer = mainController.getRenderController().getBox2DDebugRenderer();
        this.shapeRenderer = mainController.getRenderController().getShapeRenderer();
        this.world = world;
        this.camera = mainController.getRenderController().getDynamicCamera();
        this.entities = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        camera.combined.scl(RenderController.w2w(1.0f));
        if (Constants.General.DEBUGGING)
            debugRenderer.render(world, camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);
        for (Entity entity : entities) {
            SteeringComponent sc = Mappers.steering.get(entity);
            BodyComponent bc = Mappers.body.get(entity);

            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.ellipse(sc.getPosition().x - sc.getBoundingRadius(),
                    sc.getPosition().y - sc.getBoundingRadius(),
                    2*sc.getBoundingRadius(), 2*sc.getBoundingRadius(), 50);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.line(bc.body.getPosition(), bc.body.getPosition().cpy().add(bc.body.getLinearVelocity()));
            shapeRenderer.setColor(Color.BLUE);
            Vector2 rotvec = new Vector2(sc.getLinearVelocity()).rotate90(1).nor().scl(sc.getAngularVelocity());
            Vector2 lin = new Vector2(sc.getLinearVelocity()).nor().scl(sc.getBoundingRadius());
            shapeRenderer.line(new Vector2(sc.getPosition()).add(lin),
                    rotvec.add(new Vector2(sc.getPosition()).add(lin)));

            shapeRenderer.setColor(Color.YELLOW);
            if (bc.contact != null){
                shapeRenderer.line(bc.body.getPosition(), bc.contact);
                shapeRenderer.point(bc.contact.x, bc.contact.y, 0.0f);
                bc.contact = null;
            }
        }
        shapeRenderer.end();

        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }
}
