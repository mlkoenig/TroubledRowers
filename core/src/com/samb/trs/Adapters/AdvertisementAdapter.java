package com.samb.trs.Adapters;

public interface AdvertisementAdapter {
    void loadInterstitial();
    void showInterstitial();
    boolean isLoading();

    boolean isLoaded();
}
