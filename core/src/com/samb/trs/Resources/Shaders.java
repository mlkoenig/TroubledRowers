package com.samb.trs.Resources;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public enum Shaders implements Resource<ShaderProgram> {
    SHADOW("Shaders/shadow"),
    FISH("Shaders/fish"),
    SHIELD("Shaders/shield"),
    WATER("Shaders/water");

    private String identifier;

    Shaders(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Class<ShaderProgram> getType() {
        return ShaderProgram.class;
    }
}
