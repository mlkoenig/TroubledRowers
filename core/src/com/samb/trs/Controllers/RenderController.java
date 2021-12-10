package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.Shaders;

import static com.samb.trs.Resources.Constants.Rendering.WorldHeight;
import static com.samb.trs.Resources.Constants.Rendering.WorldWidth;

public class RenderController extends BaseController{

    private final SpriteBatch batch;
    private final FitViewport dynamicViewport;
    private final OrthographicCamera dynamicCamera;
    private final ShaderController shaderController;
    private final ShapeRenderer shapeRenderer;
    private final Box2DDebugRenderer box2DDebugRenderer;

    public RenderController(MainController mainController) {
        super(mainController);
        batch = new SpriteBatch(440);
        dynamicCamera = new OrthographicCamera();
        dynamicViewport = new FitViewport(WorldWidth, Constants.Rendering.WorldHeight, dynamicCamera);
        box2DDebugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shaderController = new ShaderController(mainController);
    }

    public void render(float dt) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
                | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        getMain().getGameWorldController().update(dt);
    }

    public void resize(int width, int height) {
        Constants.Rendering.WorldHeight = Constants.Rendering.calculateHeight(width, height);
        dynamicViewport.update(width, height);
    }

    public static float p2w(float percent) {
        return WorldWidth * percent / 100f;
    }

    public static float p2h(float percent) {
        return Constants.Rendering.WorldHeight * percent / 100f;
    }

    /**
     * Convert Screen pixels to virtual width
     *
     * @return virtual width
     */
    public static float cpw() {
        return WorldWidth / ((float) Gdx.graphics.getWidth());
    }

    /**
     * Convert Screen pixels to virtual height
     *
     * @return virtual height
     */
    public static float cph() {
        return Constants.Rendering.WorldHeight / ((float) Gdx.graphics.getHeight());
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getDynamicCamera() {
        return dynamicCamera;
    }

    public FitViewport getDynamicViewport() {
        return dynamicViewport;
    }

    public ShaderProgram getShader(Shaders s) {
        return shaderController.getShader(s);
    }

    public Box2DDebugRenderer getBox2DDebugRenderer() {
        return box2DDebugRenderer;
    }

    @Override
    public void dispose() {
        batch.dispose();
        box2DDebugRenderer.dispose();
        shapeRenderer.dispose();
        shaderController.dispose();
    }
}
