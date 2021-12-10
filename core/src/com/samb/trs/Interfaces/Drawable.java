package com.samb.trs.Interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Drawable {
    void drawShadow(Batch batch);

    void drawUnder(Batch batch);

    void drawBase(Batch batch);

    void drawOver(Batch batch);
}
