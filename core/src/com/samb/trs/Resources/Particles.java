package com.samb.trs.Resources;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum Particles {
    BOOST("Particles/boost.p", Atlases.TEXTURES), COIN("Particles/coin.p", Atlases.TEXTURES), FISH("Particles/fish.p", Atlases.TEXTURES), ROCK("Particles/rock.p", Atlases.TEXTURES),
    STAGE("Particles/stage.p", Atlases.TEXTURES), TAIL("Particles/tail.p", Atlases.TEXTURES), WATER("Particles/water.p", Atlases.TEXTURES),
    ROCK_WATER("Particles/rock_water.p", Atlases.TEXTURES), FISH_WATER("Particles/fish_water.p", Atlases.TEXTURES);

    private final String id;
    private final Atlases atlas;

    Particles(String id) {
        this(id, Atlases.TEXTURES);
    }

    Particles(String id, Atlases a) {
        this.id = id;
        this.atlas = a;
    }

    public String getIdentifier() {
        return id;
    }

    public Atlases getAtlas() {
        return atlas;
    }
}
