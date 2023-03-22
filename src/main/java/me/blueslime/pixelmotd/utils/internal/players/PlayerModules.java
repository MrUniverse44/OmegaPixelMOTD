package me.blueslime.pixelmotd.utils.internal.players;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.utils.internal.players.injects.*;

public class PlayerModules {
    private static final ClassCastException CLASS_CAST_EXCEPTION = new ClassCastException("Did you tried to load a String list or a object in the player number");
    public static PlayerModule MULTIPLIER_MODULE = MultiplierModule.INSTANCE;
    public static PlayerModule DEFAULT_MODULE = DefaultModule.INSTANCE;
    public static PlayerModule REMOVE_MODULE = RemoverModule.INSTANCE;
    public static PlayerModule MIDDLE_MODULE = MiddleModule.INSTANCE;
    public static PlayerModule SPLIT_MODULE = SplitModule.INSTANCE;
    public static PlayerModule ADD_MODULE = AdderModule.INSTANCE;

    public static int execute(Object type, PlayerHandler players, int online, Object values) {
        String value;

        if (values instanceof String) {
            value = (String)values;
        } else {
            value = values + "";
        }

        String t = type + "";

        return execute(
                Integer.parseInt(t),
                players,
                online,
                value
        );
    }

    public static int execute(int type, PlayerHandler players, int online, String values) {
        switch (type) {
            default:
            case -1:
            case 0:
                return players.getMaxPlayers();
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
                return online;
        }
    }

    public static int execute(Object type, int online, int motdOnline, Object values) {

        String value;

        if (values instanceof String) {
            value = (String)values;
        } else {
            value = values + "";
        }

        if (type instanceof String) {
            return execute(
                    Integer.parseInt(
                            (String)type
                    ),
                    online,
                    motdOnline,
                    value
            );
        }
        if (type instanceof Integer) {
            return execute(
                    (int)type,
                    online,
                    motdOnline,
                    value
            );
        }
        return execute(
                -1,
                online,
                motdOnline,
                value
        );
    }

    public static int execute(Object type, int online, Object values) {
        String value;

        if (values instanceof String) {
            value = (String)values;
        } else {
            value = String.valueOf((int)values);
        }
        if (type instanceof String) {
            return executeDirect(
                    Integer.parseInt(
                            (String)type
                    ),
                    online,
                    value
            );
        }
        if (type instanceof Integer) {
            return executeDirect(
                    (int)type,
                    online,
                    value
            );
        }
        throw CLASS_CAST_EXCEPTION;
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

    public static int executeDirect(int type, int online, String values) {
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
                configuration.getString("players.max.value", "1000;1001")
        );
    }

    public static int getMaximumPlayers(PixelMOTD<?> plugin, ConfigurationHandler configuration, int online) {
        plugin.getLogs().info("max type is: " + configuration.get("players.max.type", 1));
        plugin.getLogs().info("max values: " + configuration.get("players.max.value", "1000;1001"));
        return execute(
                configuration.get("players.max.type", 1),
                plugin.getPlayerHandler(),
                online,
                configuration.get("players.max.value", "1000;1001")
        );
    }

    public static int getOnlinePlayers(PixelMOTD<?> plugin, ConfigurationHandler configuration) {
        return execute(
                configuration.get("players.online.type", 0),
                plugin.getPlayerHandler().getPlayersSize(),
                configuration.get("players.online.value", "10")
        );
    }

}
