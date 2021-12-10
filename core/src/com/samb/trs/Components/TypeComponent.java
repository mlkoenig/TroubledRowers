package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class TypeComponent implements Component, Pool.Poolable {
    public static final short OTHER = 1;
    public static final short SHORE = 2;
    public static final short BOAT = 4;
    public static final short ROCK = 8;
    public static final short COIN = 16;
    public static final short SHIELD = 32;
    public static final short QUADRANT = 64;
    public static final short FISH = 128;
    public static final short BOOST = 256;
    public static final short TRUNK = 512;
    public static final short PADDEL = 1024;

    public short type;

    public TypeComponent() {
        this.type = OTHER;
    }

    @Override
    public void reset() {
        type = OTHER;
    }
}
