package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.TypeComponent;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Utilities.Mappers;

public class ShoreSystem extends IteratingSystem {
    private Array<BodyComponent> bodyComponents;

    public ShoreSystem() {
        super(Family.all(TypeComponent.class).get());
        bodyComponents = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (BodyComponent bc : bodyComponents) {
            if (bc.body.getPosition().y <= (-Constants.Rendering.WorldHeight + getEngine().getSystem(CameraSystem.class).getCamera().position.y)) {
                bc.body.setTransform(bc.body.getPosition().x, bc.body.getPosition().y + Constants.Rendering.WorldHeight * 2, bc.body.getAngle());
            }
        }
        bodyComponents.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Mappers.type.get(entity).type == TypeComponent.SHORE) {
            bodyComponents.add(Mappers.body.get(entity));
        }
    }
}
