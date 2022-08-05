package me.blueslime.pixelmotd.utils;

public enum OnlineList {
    CONTAINS,
    NAME;

    private String key;

    public static OnlineList fromText(String key, String text) {
        OnlineList mode;
        if (text.contains("CONTAIN")) {
            mode = OnlineList.CONTAINS;
        } else {
            mode = OnlineList.NAME;
        }
        mode.setKey(key);
        return mode;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
