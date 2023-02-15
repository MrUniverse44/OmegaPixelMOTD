package me.blueslime.pixelmotd.motd;

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

    public static MotdProtocol getFromText(String paramText, int code) {

        if (paramText.equalsIgnoreCase("ALWAYS_NEGATIVE")) {
            return ALWAYS_NEGATIVE;
        }

        paramText = paramText.toLowerCase();

        if (paramText.contains("positive")) {
            return ALWAYS_POSITIVE.setCode(code);
        }

        if (paramText.contains("negative")) {
            return ALWAYS_NEGATIVE;
        }

        return DEFAULT.setCode(code);
    }
}
