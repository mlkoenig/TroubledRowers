package com.samb.trs.Adapters;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

public class MainAdapter {
    private AdvertisementAdapter advertisementAdapter;
    private IGameServiceClient gameServiceClient;
    private NotificationAdapter notificationAdapter;
    private SoundAdapter soundAdapter;

    public MainAdapter() {
        gameServiceClient = new NoGameServiceClient();
        advertisementAdapter = new NoAdvertisementAdapter();
        notificationAdapter = new NoNotificationAdapter();
        soundAdapter = new NoSoundAdapter();
    }

    public MainAdapter initialize(IGameServiceClient gameServiceClient, AdvertisementAdapter advertisementAdapter, NotificationAdapter notificationAdapter, SoundAdapter soundAdapter) {
        setGameServiceClient(gameServiceClient);
        setAdvertisementAdapter(advertisementAdapter);
        setNotificationAdapter(notificationAdapter);
        setSoundAdapter(soundAdapter);
        return this;
    }

    public AdvertisementAdapter getAdvertisementAdapter() {
        return advertisementAdapter;
    }

    public SoundAdapter getSoundAdapter() {
        return soundAdapter;
    }

    public void setAdvertisementAdapter(AdvertisementAdapter advertisementAdapter) {
        this.advertisementAdapter = advertisementAdapter;
    }

    public NotificationAdapter getNotificationAdapter() {
        return notificationAdapter;
    }

    public void setNotificationAdapter(NotificationAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    public IGameServiceClient getGameServiceClient() {
        return gameServiceClient;
    }

    public void setGameServiceClient(IGameServiceClient gameServiceClient) {
        this.gameServiceClient = gameServiceClient;
    }

    public void setSoundAdapter(SoundAdapter soundAdapter) {
        this.soundAdapter = soundAdapter;
    }
}
