package com.samb.trs.Controllers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.samb.trs.Resources.Musics;
import com.samb.trs.Resources.Preferences;
import com.samb.trs.Resources.Sounds;

import java.util.HashMap;
import java.util.Map;

public class SoundController extends BaseController{
    private Map<Musics, Music> musicMap;
    private Map<Sounds, Sound> soundMap;
    private Map<Object, Sounds> soundQueue;
    private float volumeSound;
    private float volumeMusic;

    public SoundController(MainController mainController) {
        super(mainController);

        volumeSound = mainController.getSaveController().getPreferences().getFloat(Preferences.VOLUME_SOUND, 1);
        volumeMusic = mainController.getSaveController().getPreferences().getFloat(Preferences.VOLUME_MUSIC, 1);

        musicMap = new HashMap<>();
        soundMap = new HashMap<>();
        soundQueue = new HashMap<>();

        initializeMaps();
    }

    public void clearSoundQueue() {
        soundQueue.clear();
    }

    /**
     * Needs to be initialized before usage
     */
    public void initializeMaps() {
        for (Sounds sound : Sounds.values()) {
            soundMap.put(sound, getMain().getAssetController().getAsset(sound));
        }

        for (Musics music : Musics.values()) {
            Music m = getMain().getAssetController().getAsset(music);
            m.setVolume(music.getVolume() * volumeMusic);
            musicMap.put(music, getMain().getAssetController().getAsset(music));
        }
    }

    public void queueSound(Object o, Sounds s) {
        soundQueue.put(o, s);
    }

    //TODO: Fix sound lag
    public void playSoundQueue() {
        for (Object o : soundQueue.keySet()) {
            defaultPlay(soundQueue.get(o));
        }
        soundQueue.clear();
    }

    /**
     * Play sound with default volume calculated by the product of the global sound volume and the given sound's volume.
     *
     * @param sound
     */
    public void defaultPlay(final Sounds sound) {
        getMain().getMainAdapter().getSoundAdapter().post(new Runnable() {
            @Override
            public void run() {
                getSound(sound).play(sound.getVolume() * volumeSound);
            }
        });
    }

    /**
     * Set the sound volume to volumeSound. Ater that all volumes of played sounds will be weighted with given value.
     *
     * @param volumeSound
     */
    public void setVolumeSound(float volumeSound) {
        this.volumeSound = volumeSound;
        getMain().getSaveController().getPreferences().putFloat(Preferences.VOLUME_SOUND, volumeSound);
        getMain().getSaveController().getPreferences().flush();
    }

    /**
     * Set the music volume for all music objects. Will be saved in Preferences for future launches.
     *
     * @param volumeMusic
     */
    public void setVolumeMusic(float volumeMusic) {
        this.volumeMusic = volumeMusic;
        getMain().getSaveController().getPreferences().putFloat(Preferences.VOLUME_MUSIC, volumeMusic);
        getMain().getSaveController().getPreferences().flush();
        for (Musics music : musicMap.keySet()) {
            musicMap.get(music).setVolume(volumeMusic * music.getVolume());
        }
    }

    public Music getMusic(Musics music) {
        if (!musicMap.containsKey(music))
            musicMap.put(music, getMain().getAssetController().getAsset(music));
        return musicMap.get(music);
    }

    public Sound getSound(Sounds sound) {
        if (!soundMap.containsKey(sound))
            soundMap.put(sound, getMain().getAssetController().getAsset(sound));
        return soundMap.get(sound);
    }

    @Override
    public void dispose() {

    }
}
