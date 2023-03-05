package me.blueslime.pixelmotd.motd;

public enum MotdType {
    MOTD_BANNED_USER(5),
    TEMP_BANNED_USER(6),
    OUTDATED_CLIENT(3),
    OUTDATED_SERVER(4),
    WHITELIST(1),
    BLACKLIST(2),
    INVALID(-1),
    NORMAL(0);


    private final int id;

    MotdType(int id) {
        this.id  = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return original().toLowerCase();
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

    public static MotdType parseMotd(String integer) throws NumberFormatException {
        int id = Integer.parseInt(integer);
        for (MotdType type : MotdType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        return INVALID;
    }
}
