package com.samb.trs.Resources;

import com.badlogic.gdx.graphics.Texture;

public enum Textures implements Resource<Texture> {
    KANU("Textures/CropBoat/kanu0.png");

    private final String id;

    Textures(String id){
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Class<Texture> getType() {
        return Texture.class;
    }
}
