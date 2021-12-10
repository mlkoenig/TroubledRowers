package com.samb.trs.Resources;

public enum LeaderboardId implements GameServiceId {
    LEADERBOARD("leaderboard_id", "CgkI093fotUGEAIQAQ");

    private final String id, androidId;

    LeaderboardId(String id, String androidId) {
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
