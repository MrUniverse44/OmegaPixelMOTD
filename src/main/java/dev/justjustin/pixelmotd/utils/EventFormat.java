package dev.justjustin.pixelmotd.utils;

public enum EventFormat {
    FIRST,
    SECOND,
    THIRD;

    public static EventFormat fromText(String format) {
        switch (format.toLowerCase()) {
            case "first":
            case "1":
                return FIRST;
            case "second":
            case "2":
                return SECOND;
            default:
                return THIRD;
        }
    }
}