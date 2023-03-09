package me.blueslime.pixelmotd.utils.internal.players;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.utils.internal.players.injects.*;

public class PlayerModules {

    public static PlayerModule MULTIPLIER_MODULE = MultiplierModule.INSTANCE;
    public static PlayerModule DEFAULT_MODULE = DefaultModule.INSTANCE;
    public static PlayerModule REMOVE_MODULE = RemoverModule.INSTANCE;
    public static PlayerModule MIDDLE_MODULE = MiddleModule.INSTANCE;
    public static PlayerModule SPLIT_MODULE = SplitModule.INSTANCE;
    public static PlayerModule ADD_MODULE = AdderModule.INSTANCE;

    public static int execute(Object type, int online, int motdOnline, String values) {
        if (type instanceof String) {
            return execute(
                    Integer.parseInt(
                            (String)type
                    ),
                    online,
                    motdOnline,
                    values
            );
        }
        if (type instanceof Integer) {
            return execute(
                    (int)type,
                    online,
                    motdOnline,
                    values
            );
        }
        return execute(
                -1,
                online,
                motdOnline,
                values
        );
    }

    public static int execute(Object type, int online, String values) {
        if (type instanceof String) {
            return execute(
                    Integer.parseInt(
                            (String)type
                    ),
                    online,
                    values
            );
        }
        if (type instanceof Integer) {
            return execute(
                    (int)type,
                    online,
                    values
            );
        }
        return execute(
                -1,
                online,
                values
        );
    }

    public static int execute(int type, int online, int motdOnline, String values) {
        switch (type) {
            default:
            case -1:
            case 0:
                return online;
            case 1:
                return DEFAULT_MODULE.execute(online, values);
            case 2:
                return ADD_MODULE.execute(online, values);
            case 3:
                return REMOVE_MODULE.execute(online, values);
            case 4:
                return MULTIPLIER_MODULE.execute(online, values);
            case 5:
                return SPLIT_MODULE.execute(online, values);
            case 6:
                return MIDDLE_MODULE.execute(online, values);
            case 7:
                return online + MIDDLE_MODULE.execute(online, values);
            case 8:
                int minimum = online - MIDDLE_MODULE.execute(online, values);

                if (minimum < 1) {
                    minimum = 0;
                }

                return minimum;
            case 9:
                return motdOnline;
        }
    }

    public static int execute(int type, int online, String values) {
        switch (type) {
            default:
            case -1:
            case 0:
                return online;
            case 1:
                return DEFAULT_MODULE.execute(online, values);
            case 2:
                return ADD_MODULE.execute(online, values);
            case 3:
                return REMOVE_MODULE.execute(online, values);
            case 4:
                return MULTIPLIER_MODULE.execute(online, values);
            case 5:
                return SPLIT_MODULE.execute(online, values);
            case 6:
                return MIDDLE_MODULE.execute(online, values);
            case 7:
                return online + MIDDLE_MODULE.execute(online, values);
            case 8:
                int minimum = online - MIDDLE_MODULE.execute(online, values);

                if (minimum < 1) {
                    minimum = 0;
                }

                return minimum;
        }
    }

    public static int getMaximumPlayers(PixelMOTD<?> plugin, CachedMotd motd) {
        return getMaximumPlayers(
                plugin,
                motd.getConfiguration()
        );
    }

    public static int getMaximumPlayers(PixelMOTD<?> plugin, CachedMotd motd, int online) {
        return getMaximumPlayers(
                plugin,
                motd.getConfiguration(),
                online
        );
    }

    public static int getOnlinePlayers(PixelMOTD<?> plugin, CachedMotd motd) {
        return getOnlinePlayers(
                plugin,
                motd.getConfiguration()
        );
    }

    public static int getMaximumPlayers(PixelMOTD<?> plugin, ConfigurationHandler configuration) {
        return execute(
                configuration.get("players.max.type", 1),
                plugin.getPlayerHandler().getMaxPlayers(),
                configuration.getString("players.max.values", "1000;1001")
        );
    }

    public static int getMaximumPlayers(PixelMOTD<?> plugin, ConfigurationHandler configuration, int online) {
        return execute(
                configuration.get("players.max.type", 1),
                plugin.getPlayerHandler().getMaxPlayers(),
                online,
                configuration.getString("players.max.values", "1000;1001")
        );
    }

    public static int getOnlinePlayers(PixelMOTD<?> plugin, ConfigurationHandler configuration) {
        return execute(
                configuration.get("players.online.type", 0),
                plugin.getPlayerHandler().getPlayersSize(),
                configuration.getString("players.online.values", "10")
        );
    }

}
