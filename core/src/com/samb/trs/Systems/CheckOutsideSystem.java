package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Components.CheckOutsideComponent;
import com.samb.trs.Components.TextureComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Utilities.Mappers;

public class CheckOutsideSystem extends IteratingSystem {
    private Array<Entity> entityQueue;

    public CheckOutsideSystem() {
        super(Family.all(CheckOutsideComponent.class, TransformComponent.class).get());
        this.entityQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entityQueue) {
            TransformComponent tc = Mappers.transform.get(entity);
            CheckOutsideComponent coc = Mappers.checkOutside.get(entity);
            TextureComponent tex = Mappers.texture.get(entity);

            if (!isInside(tc.position.x, tc.position.y, tex.width, tex.height, coc.offsetX, coc.offsetY, coc.viewport)) {
                getEngine().removeEntity(entity);
            }
        }

        entityQueue.clear();
    }

    private boolean isInside(float x, float y, float width, float height, Vector2 offsetX, Vector2 offsetY, Viewport viewport) {
        return y + height / 2f >= -viewport.getWorldHeight() / 2f + viewport.getCamera().position.y + offsetY.x
                && y - height / 2f <= viewport.getWorldHeight() / 2f + viewport.getCamera().position.y + offsetY.y
                && x - width / 2f <= viewport.getWorldWidth() / 2f + viewport.getCamera().position.x + offsetX.y
                && x + width / 2f >= -viewport.getWorldWidth() / 2f + viewport.getCamera().position.x + offsetX.x;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entityQueue.add(entity);
    }
}
