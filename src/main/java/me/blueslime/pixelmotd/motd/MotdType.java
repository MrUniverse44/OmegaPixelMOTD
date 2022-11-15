package me.blueslime.pixelmotd.motd;

public enum MotdType {
    INVALID(-1),

    NORMAL(0),
    WHITELIST(1),
    BLACKLIST(2),
    OUTDATED_CLIENT(3),
    OUTDATED_SERVER(4),
    MOTD_BANNED_USER(5),
    TEMP_BANNED_USER(6);

    private final int id;

    private boolean hex;

    MotdType(int id) {
        this.hex = false;
        this.id  = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean isHexMotd() {
        return hex;
    }

    public MotdType switchToHex() {
        this.hex = !hex;
        return this;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public String original() {
        return super.toString();
    }

    public static MotdType parseMotd(int id) {
        for (MotdType type : MotdType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        return INVALID;
    }
}
