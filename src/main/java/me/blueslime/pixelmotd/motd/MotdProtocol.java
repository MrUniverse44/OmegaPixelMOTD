package me.blueslime.pixelmotd.motd;

public enum MotdProtocol {
    ALWAYS_POSITIVE,
    ALWAYS_NEGATIVE,
    DEFAULT;

    public int getCode() {
        if (this == ALWAYS_NEGATIVE) {
            return -1;
        }
        return 0;
    }

    public static MotdProtocol fromString(String paramText) {

        paramText = paramText.toLowerCase();

        if (paramText.contains("negative")) {
            return ALWAYS_NEGATIVE;
        }

        if (paramText.contains("positive")) {
            return ALWAYS_POSITIVE;
        }

        return DEFAULT;
    }
}
