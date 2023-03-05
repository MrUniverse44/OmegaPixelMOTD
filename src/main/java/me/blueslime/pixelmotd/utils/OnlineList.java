package me.blueslime.pixelmotd.utils;

import java.util.Locale;

public enum OnlineList {
    CONTAINS,
    NAME;

    private String key;

    public static OnlineList fromText(String key, String text) {
        OnlineList mode;
        if (text.toLowerCase(Locale.ENGLISH).contains("contain")) {
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
