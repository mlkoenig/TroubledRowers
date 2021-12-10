package com.samb.trs.Resources;

public enum EventId implements GameServiceId {
    ROCK_DESTROYED("destroyed_rocks_id", "CgkI093fotUGEAIQAg"),
    FISH_DESTROYED("destroyed_fish_id", "CgkI093fotUGEAIQBA"),
    SHIELD_USED("used_shields_id", "CgkI093fotUGEAIQBg"),
    COIN_COLLECTED("collected_coins_id", "CgkI093fotUGEAIQBQ"),
    GAME_PLAYED("games_played_id", "CgkI093fotUGEAIQCA");

    private final String id, androidId;

    EventId(String id, String androidId) {
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
