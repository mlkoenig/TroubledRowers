package com.samb.trs.Resources;

import com.badlogic.gdx.audio.Sound;

public enum Sounds implements Resource<Sound> {

    PADDLE("Audio/Sounds/paddle.wav", 0.1f), COIN("Audio/Sounds/coin.wav"), ROCK("Audio/Sounds/rock.wav"), BOOST("Audio/Sounds/boost.wav"),
    COIN_DROP("Audio/Sounds/coin-drop.wav"), CRASH("Audio/Sounds/crash.wav", 0.5f), SPLASH("Audio/Sounds/splash.wav"), SHIELD_HIT("Audio/Sounds/shield-hit.wav"),
    GAMEOVER("Audio/Sounds/gameover.wav");

    private final String id;
    private final float volume;

    Sounds(String id, float volume) {
        this.id = id;
        this.volume = volume;
    }

    Sounds(String id){
        this.id = id;
        this.volume = 1;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    public float getVolume() {
        return volume;
    }

    @Override
    public Class<Sound> getType() {
        return Sound.class;
    }
}