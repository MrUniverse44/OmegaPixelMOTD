package me.blueslime.pixelmotd.utils.list;

import me.blueslime.slimelib.SlimePlatform;

import java.util.Locale;

public enum PluginList {
    SERVERS,
    WORLDS;
    
    public String loweredName() {
        return toString(true);
    }

    public String toString(boolean lowerCase) {
        if (lowerCase) {
            return super.toString().toLowerCase(Locale.ENGLISH);
        }
        return toString();
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
            case BUKKIT:
            case SPONGE:
                return WORLDS;
        }
    }
}
