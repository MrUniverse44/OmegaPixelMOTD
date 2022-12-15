package me.blueslime.pixelmotd.utils.internal.events;

public enum EventFormatEnum {
    DISABLED,
    ZERO,
    FIRST,
    SECOND,
    THIRD,
    FOUR,
    FIVE,
    SIX;

    public static EventFormatEnum fromText(String format) {
        switch (format.toLowerCase()) {
            case "-1":
                return DISABLED;
            case "zero":
            case "0":
                return ZERO;
            case "first":
            case "1":
                return FIRST;
            case "second":
            case "2":
                return SECOND;
            case "third":
            case "3":
            default:
                return THIRD;
            case "four":
            case "4":
                return FOUR;
            case "five":
            case "5":
                return FIVE;
            case "six":
            case "6":
                return SIX;
        }
    }
}