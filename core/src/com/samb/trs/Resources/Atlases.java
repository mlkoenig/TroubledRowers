package com.samb.trs.Resources;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum Atlases implements Resource<TextureAtlas> {
    TEXTURES("Textures/Spritesheets/textures.atlas"), BOAT("Textures/Spritesheets/boat.atlas"), BACKGROUNDS("Textures/Spritesheets/backgrounds.atlas");
    private final String id;

    Atlases(String id){
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Class<TextureAtlas> getType() {
        return TextureAtlas.class;
    }


}