package dev.justjustin.pixelmotd.utils;

import dev.mruniverse.slimelib.SlimePlatform;

public enum WhitelistLocation {
    SERVERS,
    WORLDS;

    public static WhitelistLocation fromPlatform(SlimePlatform platform) {
        switch (platform) {
            default:
            case VELOCITY:
            case BUNGEECORD:
                return SERVERS;
            case SPIGOT:
            case SPONGE:
                return WORLDS;
        }
    }
    
    public String toStringLowerCase() {
        return super.toString().toLowerCase();
    }

    @Override
    public String toString() {
        return capitalize(super.toString().toLowerCase());
    }

    public String toSingular() {
        return toString().replace("ers", "er").replace("rds", "rd");
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
