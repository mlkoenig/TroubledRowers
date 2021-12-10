package com.samb.trs.Resources;

import com.badlogic.gdx.utils.I18NBundle;

public enum Bundles implements Resource<I18NBundle> {
    MENU("Localization/Menu"), HUD("Localization/Hud"), GAMEOVER("Localization/GameOver"), PAUSE("Localization/Pause"), TUTORIAL("Localization/Tutorial"),
    GENERAL("Localization/General");
    private final String id;

    Bundles(String id){
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Class<I18NBundle> getType() {
        return I18NBundle.class;
    }


}