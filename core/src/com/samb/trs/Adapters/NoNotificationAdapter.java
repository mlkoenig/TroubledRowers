package com.samb.trs.Adapters;

import com.badlogic.gdx.Gdx;

public class NoNotificationAdapter implements NotificationAdapter {
    @Override
    public void show(String title, String text, int id) {
        Gdx.app.log("NoNotification", "Title: " + title + ", Text: " + text + ", id: " + id);
    }
}
