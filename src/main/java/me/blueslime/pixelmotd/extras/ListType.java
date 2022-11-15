package me.blueslime.pixelmotd.extras;

public enum ListType {
    WHITELIST,
    BLACKLIST;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ListType fromString(String text) {
        switch (text.toLowerCase()) {
            case "bl":
            case "2":
            case "3":
            case "blacklist":
                return BLACKLIST;
            default:
            case "wl":
            case "0":
            case "1":
            case "whitelist":
                return WHITELIST;
        }
    }
}