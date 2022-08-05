package me.blueslime.pixelmotd;

public enum MotdType {

    NORMAL(SlimeFile.SERVER_MOTDS, "motds", false),
    NORMAL_HEX(SlimeFile.SERVER_MOTDS, "motds-hex", true),
    WHITELIST(SlimeFile.WHITELIST, "whitelist", false),
    WHITELIST_HEX(SlimeFile.WHITELIST, "whitelist-hex", true),
    BLACKLIST(SlimeFile.BLACKLIST, "blacklist", false),
    BLACKLIST_HEX(SlimeFile.BLACKLIST, "blacklist-hex", true),
    OUTDATED_SERVER(SlimeFile.OUTDATED_SERVER, "outdated-server", false),
    OUTDATED_SERVER_HEX(SlimeFile.OUTDATED_SERVER, "outdated-server-hex", true),
    OUTDATED_CLIENT(SlimeFile.OUTDATED_CLIENT, "outdated-client", false),
    OUTDATED_CLIENT_HEX(SlimeFile.OUTDATED_CLIENT, "outdated-client-hex", true);

    private final String path;

    private final SlimeFile file;

    private final boolean hexMode;

    MotdType(SlimeFile file, String path, boolean hexMode) {
        this.path = path;
        this.file = file;
        this.hexMode = hexMode;
    }

    public static MotdType fromText(String text) {
        switch (text.toLowerCase()) {
            case "outdated-client-hex":
                return OUTDATED_CLIENT_HEX;
            case "outdated-client":
                return OUTDATED_CLIENT;
            case "outdated-server-hex":
                return OUTDATED_SERVER_HEX;
            case "outdated-server":
                return OUTDATED_SERVER;
            case "blacklist-hex":
                return BLACKLIST_HEX;
            case "blacklist":
                return BLACKLIST;
            case "whitelist-hex":
                return WHITELIST_HEX;
            case "whitelist":
                return WHITELIST;
            case "motds-hex":
                return NORMAL_HEX;
            default:
            case "motds":
                return NORMAL;
        }
    }

    public SlimeFile getFile() {
        return file;
    }

    public boolean isHexMotd() {
        return hexMode;
    }

    @Override
    public String toString() {
        return path;
    }
}
