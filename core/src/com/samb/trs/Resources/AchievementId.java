package com.samb.trs.Resources;

public enum AchievementId implements GameServiceId {
    ;

    private final String id, androidId;

    AchievementId(String id, String androidId) {
        this.id = id;
        this.androidId = androidId;
    }

    public final String getId() {
        return id;
    }

    public final String getAndroidId() {
        return androidId;
    }

    @Override
    public String getiOSId() {
        return null;
    }


}
