package com.samb.trs.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;

public class CameraComponent implements Component, Pool.Poolable {
    public OrthographicCamera camera;

    @Override
    public void reset() {

    }
}
