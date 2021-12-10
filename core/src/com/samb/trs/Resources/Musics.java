package com.samb.trs.Resources;

import com.badlogic.gdx.audio.Music;

public enum Musics implements Resource<Music> {
    RIVER("Audio/Music/river.wav", 0.5f), SHIELD("Audio/Music/shield.wav", 0.7f), HANGIN_TEN("Audio/Music/Hangin-Ten_Looping.mp3"),
    GAMEOVER("Audio/Music/gameover.wav");

    private final String id;
    private final float volume;

    Musics(String id, float volume) {
        this.id = id;
        this.volume = volume;
    }

    Musics(String id){
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
    public Class<Music> getType() {
        return Music.class;
    }
}