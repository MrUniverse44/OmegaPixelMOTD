package me.blueslime.pixelmotd.utils.list;

import dev.mruniverse.slimelib.SlimePlatform;

public enum PluginList {
    SERVERS,
    WORLDS;
    
    public String loweredName() {
        return super.toString().toLowerCase();
    }

    @Override
    public String toString() {
        return capitalize(
                loweredName()
        );
    }

    public String toSingular() {
        return toString().replace("ers", "er")
                .replace("rds", "rd");
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() +
                text.substring(1);
    }

    public static PluginList fromPlatform(SlimePlatform platform) {
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
}
