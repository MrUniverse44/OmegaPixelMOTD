package me.blueslime.pixelmotd.utils.internal.events;

public enum EventFormatEnum {
    FIRST,
    SECOND,
    THIRD;

    public static EventFormatEnum fromText(String format) {
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