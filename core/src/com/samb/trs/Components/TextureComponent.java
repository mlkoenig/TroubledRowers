package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.samb.trs.Resources.TextureRegions;

public class TextureComponent implements Component, Pool.Poolable {
    public TextureRegion textureRegion;
    public TextureRegions region;
    public float width, height;
    public boolean hasShadow;

    @Override
    public void reset() {
        textureRegion = null;
        region = null;
        width = 0;
        height = 0;
        hasShadow = false;
    }
}
