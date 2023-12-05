package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Components.MouseComponent;
import com.samb.trs.Components.TextureComponent;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Utilities.Mappers;

public class MouseControlSystem extends IteratingSystem {
    private MainController mainController;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Array<Entity> entityQueue;
    private EventInputProcessor eip;

    public MouseControlSystem(MainController mainController, EventInputProcessor eventInputProcessor) {
        super(Family.all(MouseComponent.class).get());
        this.mainController = mainController;
        this.eip = eventInputProcessor;
        this.camera = mainController.getRenderController().getDynamicCamera();
        this.viewport = mainController.getRenderController().getDynamicViewport();
        entityQueue = new Array<>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entityQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entityQueue) {
            MouseComponent mc = Mappers.mouse.get(entity);
            TextureComponent tc = Mappers.texture.get(entity);
            if (eip.getTarget().y + mc.offset.y >= (camera.position.y - Constants.Rendering.WorldHeight / 2f + tc.height / 2f)) {
                mc.mouseJoint.setTarget(eip.getTarget().add(mc.offset));
                eip.getTarget().sub(mc.offset);
            } else {
                float x = eip.getTarget().x + mc.offset.x;
                mc.mouseJoint.setTarget(eip.getTarget().set(x, (camera.position.y - Constants.Rendering.WorldHeight / 2f + tc.height / 2f)));
                eip.getTarget().sub(mc.offset.x, 0);
                mc.offset.set(mc.offset.x, 0);
            }

            if (eip.getTarget().y + mc.offset.y <= (camera.position.y + Constants.Rendering.WorldHeight / 2f - tc.height / 2f)) {
                mc.mouseJoint.setTarget(eip.getTarget().add(mc.offset));
                eip.getTarget().sub(mc.offset);
            } else {
                float x = eip.getTarget().x + mc.offset.x;
                mc.mouseJoint.setTarget(eip.getTarget().set(x, (camera.position.y + Constants.Rendering.WorldHeight / 2f - tc.height / 2f)));
                eip.getTarget().sub(mc.offset.x, 0);
                mc.offset.set(mc.offset.x, 0);
            }
        }

        entityQueue.clear();
    }

    private Vector2 getMouseTarget() {
        Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        input = camera.unproject(input, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        return new Vector2(RenderController.s2w(input.x), RenderController.s2w(input.y));
    }
}