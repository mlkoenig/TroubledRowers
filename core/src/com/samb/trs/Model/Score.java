package com.samb.trs.Model;


import com.samb.trs.Controllers.MainController;

import java.io.Serializable;

public class Score implements Serializable {
    private static final long serialVersionUID = 6416505909906311271L;
    private MainController mainController;
    private long score;
    private int boosts;
    private float time;
    private int collected_coins;

    public Score(MainController mainController) {
        this.mainController = mainController;
        score = 0;
        boosts = 3;
        collected_coins = 0;
    }

    public void update(float dt){
        time += dt;
        score = (long) time;
        updateAccountInfo();
    }


    public void updateAccountInfo() {
        Account account = mainController.getSaveController().getAccount();
        account.setHighScore(Math.max(score, account.getHighScore()));
        account.setPlayTime(account.getPlayTime() + score);
    }


    public void increaseBoosts(){
        boosts++;
    }

    public void increaseCollectedCoins(){
        collected_coins++;
        mainController.getSaveController().getAccount().increaseCoins();
    }

    public void reset(){
        score = 0;
        boosts = 3;
        time = 0;
        collected_coins = 0;
    }

    public long getScore(){
        return score;
    }

    public int getBoosts() {
        return boosts;
    }

    public void decreaseBoosts(){
        if(boosts > 0) boosts--;
    }

    public float getTime() {
        return time;
    }

    public int getCollected_coins() {
        return collected_coins;
    }
}
