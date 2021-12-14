package com.samb.trs.Controllers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.samb.trs.Components.AttachedComponent;
import com.samb.trs.Components.ParticleEffectComponent;
import com.samb.trs.Utilities.Mappers;

import java.util.HashMap;

public class AttachedController extends BaseController implements EntityListener {
    private HashMap<Entity, Array<Entity>> attachedMap;
    private HashMap<Entity, Entity> reverseMap;

    public AttachedController(MainController mainController) {
        super(mainController);
        attachedMap = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public void addAttachedEntity(Entity entity, Entity attachedTo) {
        // Check if entity already attached
        assert Mappers.attached.has(entity);

        AttachedComponent ac = Mappers.attached.get(entity);

        // Add attached entity to list
        if (reverseMap.containsKey(entity) && attachedMap.containsKey(reverseMap.get(entity))){
            // Remove old association
            Array<Entity> attachedList = attachedMap.get(reverseMap.get(entity));
            attachedList.removeValue(entity, true);

            if (attachedList.isEmpty()) attachedMap.remove(reverseMap.get(entity));
        }

        reverseMap.put(entity, attachedTo);

        // Create list if needed
        if (!attachedMap.containsKey(attachedTo)) {
            attachedMap.put(attachedTo, new Array<Entity>());
        }

        // Update list
        if (!attachedMap.get(attachedTo).contains(entity, true))
            attachedMap.get(attachedTo).add(entity);
        else
            throw new GdxRuntimeException("AttachedEntity attached more than once");

        ac.isAttached = true;
        ac.attachedTo = attachedTo;
    }

    @Override
    public void entityAdded(Entity entity) {
        if (Mappers.attached.has(entity)) {
            AttachedComponent ac = Mappers.attached.get(entity);
            if(!ac.isAttached && ac.attachedTo != null)  addAttachedEntity(entity, ac.attachedTo);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (reverseMap.containsKey(entity) && attachedMap.containsKey(reverseMap.get(entity))){
            // Remove old association
            Array<Entity> attachedList = attachedMap.get(reverseMap.get(entity));
            attachedList.removeValue(entity, true);

            if (attachedList.isEmpty()) attachedMap.remove(reverseMap.get(entity));
        }

        if (attachedMap.containsKey(entity)) {
            // Remove attachment
            for (Entity e : attachedMap.get(entity)) {
                AttachedComponent ac = Mappers.attached.get(e);
                ac.attachedTo = null;
                ac.isAttached = false;

                if (Mappers.peCom.has(e)) {
                    ParticleEffectComponent pec = Mappers.peCom.get(e);
                    pec.isDead = true;
                }
            }
        }
    }

    public void reset() {
        attachedMap.clear();
        reverseMap.clear();
    }

    @Override
    public void dispose() {
        attachedMap.clear();
        reverseMap.clear();
    }
}
