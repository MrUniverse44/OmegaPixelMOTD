package me.blueslime.pixelmotd.utils.internal.players;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.utils.internal.players.injects.*;

public class PlayerModules {

    public static PlayerModule MULTIPLIER_MODULE = MultiplierModule.INSTANCE;
    public static PlayerModule DEFAULT_MODULE = DefaultModule.INSTANCE;
    public static PlayerModule REMOVE_MODULE = RemoverModule.INSTANCE;
    public static PlayerModule MIDDLE_MODULE = MiddleModule.INSTANCE;
    public static PlayerModule SPLIT_MODULE = SplitModule.INSTANCE;
    public static PlayerModule ADD_MODULE = AdderModule.INSTANCE;

    public static int execute(int type, Object... objects) {
        switch (type) {
            default:
            case -1:
            case 0:
                return (int) objects[0];
            case 1:
                return DEFAULT_MODULE.execute(objects);
            case 2:
                return ADD_MODULE.execute(objects);
            case 3:
                return REMOVE_MODULE.execute(objects);
            case 4:
                return MULTIPLIER_MODULE.execute(objects);
            case 5:
                return SPLIT_MODULE.execute(objects);
            case 6:
                return MIDDLE_MODULE.execute(objects);
            case 7:
                return ((int) objects[0]) + MIDDLE_MODULE.execute(objects);
            case 8:
                int minimum = ((int) objects[0]) - MIDDLE_MODULE.execute(objects);
                if (minimum < 1) {
                    minimum = 0;
                }
                return minimum;
        }
    }

    public static int getMaximumPlayers(int online, ConfigurationHandler configuration) {
        return execute(
                configuration.getInt("players.max.type", 1),
                online,
                configuration.getString("players.max.values", "1000;1001")
        );
    }

    public static int getOnlinePlayers(int online, ConfigurationHandler configuration) {
        return execute(
                configuration.getInt("players.online.type", 0),
                online,
                configuration.getString("players.online.values", "10")
        );
    }

}
