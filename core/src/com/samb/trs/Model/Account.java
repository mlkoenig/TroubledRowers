package com.samb.trs.Model;

import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Bundles;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private long coins;

    private long boosts;
    private long highScore;
    private String gamertag;

    private long rocksDestroyed;
    private long fishDestroyed;
    private long usedShields;
    private long gamesPlayed;
    private long playTime;

    public Account(MainController mainController) {
        coins = 0;
        boosts = 0;
        highScore = 0;
        rocksDestroyed = 0;
        fishDestroyed = 0;
        usedShields = 0;
        gamesPlayed = 0;
        playTime = 0;
        gamertag = mainController.getGameServiceController().getGameServiceClient().getPlayerDisplayName();
        if (gamertag == null) {
            mainController.getAssetController().getAssetManager().finishLoadingAsset(Bundles.GENERAL.getIdentifier());
            gamertag = mainController.getAssetController().getAsset(Bundles.GENERAL).get("unknownPlayer");
        }
    }

    public void increaseRocksDestroyed() {
        rocksDestroyed++;
    }

    public void increaseFishDestroyed() {
        fishDestroyed++;
    }

    public void increaseGamesPlayed() {
        gamesPlayed++;
    }

    public void increaseUsedShields() {
        usedShields++;
    }

    public void increaseCollectedBoosts() {
        boosts++;
    }

    public long getBoosts() {
        return boosts;
    }

    public long getRocksDestroyed() {
        return rocksDestroyed;
    }

    public void setRocksDestroyed(long rocksDestroyed) {
        this.rocksDestroyed = rocksDestroyed;
    }

    public long getFishDestroyed() {
        return fishDestroyed;
    }

    public void setFishDestroyed(long fishDestroyed) {
        this.fishDestroyed = fishDestroyed;
    }

    public long getUsedShields() {
        return usedShields;
    }

    public void setUsedShields(long usedShields) {
        this.usedShields = usedShields;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public void increaseCoins() {
        coins++;
    }

    public long getHighScore() {
        return highScore;
    }

    public void setHighScore(long highScore) {
        this.highScore = highScore;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }
}
