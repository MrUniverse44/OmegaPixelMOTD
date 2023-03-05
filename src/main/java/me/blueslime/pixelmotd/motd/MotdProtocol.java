package me.blueslime.pixelmotd.motd;

import java.util.Locale;

public enum MotdProtocol {
    ALWAYS_POSITIVE,
    ALWAYS_NEGATIVE(-1),
    DEFAULT;

    MotdProtocol() {
        code = 0;
    }

    MotdProtocol(int code) {
        this.code = code;
    }

    private int code;

    public MotdProtocol setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    @Deprecated
    public static MotdProtocol getFromText(String paramText, int code) {
        return fromString(paramText, code);
    }

    public static MotdProtocol fromOther(MotdProtocol protocol) {
        for (MotdProtocol p : values()) {
            if (p == protocol) {
                return p;
            }
        }
        return protocol;
    }

    public static MotdProtocol fromString(String paramText, int code) {
        paramText = paramText.toLowerCase(Locale.ENGLISH);

        switch (paramText) {
            case "always_negative":
            case "negative":
            case "2":
                return ALWAYS_NEGATIVE;
            default:
            case "always_positive":
            case "positive":
            case "1":
                return ALWAYS_POSITIVE.setCode(code);
            case "default":
            case "0":
            case "-1":
                return DEFAULT.setCode(code);
        }
    }
}
