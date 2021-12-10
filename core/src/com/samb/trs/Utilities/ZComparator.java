package com.samb.trs.Utilities;

import com.badlogic.ashley.core.Entity;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {

    @Override
    public int compare(Entity entityA, Entity entityB) {
        float az = getZ(entityA);
        float bz = getZ(entityB);
        int res = 0;
        if (az > bz) {
            res = 1;
        } else if (az < bz) {
            res = -1;
        }
        return res;
    }

    private float getZ(Entity entity) {
        if (Mappers.transform.has(entity)) {
            return Mappers.transform.get(entity).z;
        }

        throw new IllegalArgumentException("Entity must have component TransformComponent or ParticleEffectComponent!");
    }
}