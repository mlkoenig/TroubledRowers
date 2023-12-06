package com.samb.trs.Systems;

import static com.samb.trs.Resources.Constants.Rendering.WorldHeight;
import static com.samb.trs.Resources.Constants.Rendering.WorldWidth;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;

public class EventInputProcessor implements InputProcessor {
    private static final float BALANCE_FACTOR = 2.448f;

    private Camera camera;
    private Viewport viewport;
    private Vector2 lastTouch, offset;
    private Vector2 lastPos;
    private Vector2 input;
    private boolean isDown;
    private Vector2 target;
    private boolean spaceDown;
    private Engine engine;

    // Targeted Player to control
    private TransformComponent trf;

    public EventInputProcessor(Engine engine, OrthographicCamera camera, Viewport viewport) {
        this.camera = camera;
        this.viewport = viewport;
        this.engine = engine;
        lastTouch = new Vector2(RenderController.wperc(50), RenderController.hperc(60));
        lastPos = new Vector2();

        input = new Vector2();
        unproject(input, lastTouch);
        unproject(lastPos, lastTouch);

        offset = new Vector2();
        target = new Vector2();
        isDown = false;
    }

    private Vector2 getRelativePosition(Vector2 vec) {
        return new Vector2(vec.x - camera.position.x, camera.position.y - vec.y);
    }

    private Vector2 convertRelativePosition(Vector2 vec) {
        return new Vector2(vec.x + camera.position.x, vec.y + camera.position.y);
    }

    public void update(float dt) {
        unproject(input, Gdx.input.getX(0), Gdx.input.getY(0));
        if (isDown) {
            lastTouch.set(Gdx.input.getX(0), Gdx.input.getY(0));
            target.set(input.x + offset.x, input.y + offset.y);
        } else {
            unproject(lastPos, lastTouch);
            target.set(lastPos.x + offset.x, lastPos.y + offset.y);
        }
    }

    private void unproject(Vector2 out, float screenX, float screenY) {
        Vector3 temp = new Vector3(screenX, screenY, 0);
        temp = camera.unproject(temp);
        out.set(temp.x, temp.y);
    }

    private void unproject(Vector2 to, Vector2 un) {
        unproject(to, un.x, un.y);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) spaceDown = true;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) spaceDown = false;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        unproject(input, screenX, screenY);
        if (!isDown) {
            if (trf != null)
                offset.set(trf.position.x - input.x, trf.position.y - input.y + BALANCE_FACTOR * engine.getSystem(CameraSystem.class).getDiff());
            else
                Gdx.app.error("InputError", "TransformComponent for EventInputProcessor is not set! \n Player movement may not work properly.");
            isDown = true;
        } else {
            lastTouch.set(screenX, screenY);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDown = false;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    public Vector2 getTarget() {
        return target;
    }

    public void setTransformComponent(TransformComponent trf) {
        this.trf = trf;
    }

    public boolean isSpaceDown() {
        return spaceDown;
    }

    public void setSpaceDown(boolean spaceDown) {
        this.spaceDown = spaceDown;
    }

    public void reset() {
        isDown = false;
        spaceDown = false;
        offset.setZero();
        target.setZero();
        lastPos.setZero();
        lastTouch.set(RenderController.wperc(50), RenderController.hperc(60));
    }
}
