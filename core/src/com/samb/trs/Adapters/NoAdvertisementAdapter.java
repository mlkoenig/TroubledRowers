package com.samb.trs.Adapters;

import com.badlogic.gdx.Gdx;

public class NoAdvertisementAdapter implements AdvertisementAdapter {
    @Override
    public void loadInterstitial() {
        Gdx.app.log("NOADS", "Loading Interstitial Ad");
    }

    @Override
    public void showInterstitial() {
        Gdx.app.log("NOADS", "Showing Interstitial Ad");
    }

    @Override
    public boolean isLoaded() {
        Gdx.app.log("NOADS", "Interstitial Ad is loaded");
        return true;
    }

    @Override
    public boolean isLoading() {
        return false;
    }
}
