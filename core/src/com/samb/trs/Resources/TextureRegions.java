package com.samb.trs.Resources;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TextureRegions implements Resource<TextureRegion> {
    BACK_BUTTON("Back-Button", Atlases.TEXTURES), TRUNK("Trunk", Atlases.TEXTURES), BOOST("Boost", Atlases.TEXTURES), BOOST_ANZEIGE("Boost-Anzeige", Atlases.TEXTURES), CHECK_BUTTON("Check-Button", Atlases.TEXTURES),
    COIN("Coin", Atlases.TEXTURES), COIN_ANZEIGE("Coin-Anzeige", Atlases.TEXTURES), GEAR_BUTTON("Gear-Button", Atlases.TEXTURES), KANU_MANN("Kanu-Mann", Atlases.TEXTURES), KANU_PADDEL("Kanu-Paddel", Atlases.TEXTURES),
    KANU_RUMPF("Kanu-Rumpf", Atlases.TEXTURES), MENU_BUTTON("Menu-Button", Atlases.TEXTURES), PAUSE_BUTTON("Pause-Button", Atlases.TEXTURES), PARTICLE_SQUARE("ParticleSquare", Atlases.TEXTURES),
    PARTICLE_WATER("ParticleWater", Atlases.TEXTURES), PreParticle("PreParticle", Atlases.TEXTURES), SCOREBOARD_SEPARATOR("Scoreboard-Separator", Atlases.TEXTURES), FISH("Fish", Atlases.TEXTURES), FISH2("Fish2", Atlases.TEXTURES),
    TEXTBOX("Textbox", Atlases.TEXTURES), PLAY_BUTTON("Play-Button", Atlases.TEXTURES), REWIND_BUTTON("Rewind-Button", Atlases.TEXTURES), ROCK("Rock", Atlases.TEXTURES), ROCK2("Rock2", Atlases.TEXTURES),
    SHIELD_BACKGROUND("Shield-Background", Atlases.TEXTURES), SHIELD_BUTTON("Shield-Button", Atlases.TEXTURES), SHIELD_GITTER("Shield-Gitter", Atlases.TEXTURES), SHOP_BUTTON("Shop-Button", Atlases.TEXTURES), X_BUTTON("X-Button", Atlases.TEXTURES),
    SCOREBOARD("Scoreboard", Atlases.TEXTURES), KANU0("kanu0", Atlases.BOAT), KANU1("kanu1", Atlases.BOAT), KANU2("kanu2", Atlases.BOAT), KANU3("kanu3", Atlases.BOAT),
    KANU4("kanu4", Atlases.BOAT), KANU5("kanu5", Atlases.BOAT), KANU6("kanu6", Atlases.BOAT), KANU7("kanu7", Atlases.BOAT), KANU10("kanu10", Atlases.BOAT),
    KANU11("kanu11", Atlases.BOAT), KANU12("kanu12", Atlases.BOAT), KANU13("kanu13", Atlases.BOAT), KANU14("kanu14", Atlases.BOAT), KANU15("kanu15", Atlases.BOAT),
    KANU8("kanu8", Atlases.BOAT), KANU9("kanu9", Atlases.BOAT), KANU16("kanu16", Atlases.BOAT), KANU17("kanu17", Atlases.BOAT), KANU18("kanu18", Atlases.BOAT),
    KANU19("kanu19", Atlases.BOAT), KANU20("kanu20", Atlases.BOAT), KANU21("kanu21", Atlases.BOAT), KANU22("kanu22", Atlases.BOAT), KANU23("kanu23", Atlases.BOAT),
    KANU24("kanu24", Atlases.BOAT), KANU25("kanu25", Atlases.BOAT), KANU26("kanu26", Atlases.BOAT), KANU27("kanu27", Atlases.BOAT), KANU28("kanu28", Atlases.BOAT),
    KANU29("kanu29", Atlases.BOAT), KANU30("kanu30", Atlases.BOAT), BACKGROUND("background", Atlases.BACKGROUNDS), BACKGROUND2("background2", Atlases.BACKGROUNDS),
    HINTERGRUND("hintergrund", Atlases.BACKGROUNDS), UFER_LINKS("ufer_links_bounds", Atlases.BACKGROUNDS), UFER_RECHTS("ufer_rechts_bounds", Atlases.BACKGROUNDS);

    private final String id;
    private final Atlases atlas;

    TextureRegions(String id){
        this(id, Atlases.TEXTURES);
    }

    TextureRegions(String id, Atlases a){
        this.id = id;
        this.atlas = a;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    public Atlases getAtlas() {
        return atlas;
    }

    @Override
    public Class<TextureRegion> getType() {
        return TextureRegion.class;
    }
}
