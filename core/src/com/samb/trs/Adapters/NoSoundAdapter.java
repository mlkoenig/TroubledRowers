package com.samb.trs.Adapters;

public class NoSoundAdapter implements SoundAdapter {
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
