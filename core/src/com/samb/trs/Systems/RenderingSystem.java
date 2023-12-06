package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Components.ParticleEffectComponent;
import com.samb.trs.Components.TextureComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.Shaders;
import com.samb.trs.Utilities.Mappers;
import com.samb.trs.Utilities.ZComparator;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {

    private MainController mainController;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;

    @SuppressWarnings("unchecked")
    public RenderingSystem(MainController mainController) {

        super(Family.one(TextureComponent.class, ParticleEffectComponent.class).get(), new ZComparator());

        this.mainController = mainController;

        comparator = new ZComparator();

        renderQueue = new Array<>();

        camera = mainController.getRenderController().getDynamicCamera();
        viewport = mainController.getRenderController().getDynamicViewport();
        batch = mainController.getRenderController().getBatch();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // sort the renderQueue based on z index
        renderQueue.sort(comparator);

        // update camera and sprite batch
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.disableBlending();

        //Draw background
        drawEntity(Mappers.texture.get(renderQueue.first()), Mappers.transform.get(renderQueue.first()));
        renderQueue.removeIndex(0);

        batch.setShader(null);
        batch.enableBlending();

        for (Entity entity : renderQueue) {
            if(Mappers.peCom.has(entity)) {
                ParticleEffectComponent pec = Mappers.peCom.get(entity);

                TransformComponent transformComponent = Mappers.transform.get(entity);

                if (transformComponent.isHidden) {
                    continue;
                }

                if (!pec.particleEffect.isComplete() && pec.timeTilDeath >= 0) {
                    pec.particleEffect.draw(batch, deltaTime);
                }
            }
        }

        // Draw shadows

        batch.setProjectionMatrix(camera.combined);
        batch.setShader(mainController.getRenderController().getShader(Shaders.SHADOW));

        for (Entity entity : renderQueue) {
            if (Mappers.texture.has(entity)) {
                TextureComponent textureComponent = Mappers.texture.get(entity);
                TransformComponent transformComponent = Mappers.transform.get(entity);

                if (textureComponent.textureRegion == null || transformComponent.isHidden || !textureComponent.hasShadow) {
                    continue;
                }

                drawEntity(textureComponent, transformComponent);
            }
        }

        batch.setShader(null);

        // Draw sprites

        for (Entity entity : renderQueue) {
            if (Mappers.texture.has(entity)) {
                TextureComponent textureComponent = Mappers.texture.get(entity);
                TransformComponent transformComponent = Mappers.transform.get(entity);

                if (textureComponent.textureRegion == null || transformComponent.isHidden) {
                    continue;
                }

                drawEntity(textureComponent, transformComponent);
            }
        }

        batch.end();
        renderQueue.clear();
    }

    private void drawEntity(TextureComponent textureComponent, TransformComponent transformComponent) {
        if (!transformComponent.isHidden) {
            float width = textureComponent.width;
            float height = textureComponent.height;

            float originX = width / 2f;
            float originY = height / 2f;

            batch.draw(textureComponent.textureRegion,
                    transformComponent.position.x - originX,
                    transformComponent.position.y - originY,
                    originX, originY,
                    width, height,
                    1, 1,
                    transformComponent.rotation);
        }
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

}