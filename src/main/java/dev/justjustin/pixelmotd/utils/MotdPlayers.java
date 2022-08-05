package dev.justjustin.pixelmotd.utils;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;

import java.util.List;

public class MotdPlayers {
    public static int executeAdd(ConfigurationHandler configuration, String path, int value) {
        int singleValue = configuration.getInt(path, 1);
        return value + singleValue;
    }

    public static int executeAddMiddle(int value) {
        int calc;
        if (value >= 2) {
            calc = value / 2;
        } else {
            calc = 0;
        }
        return value + calc;
    }

    public static int executeAddDouble(int value) {
        return value + value;
    }

    public static int executeRemove(ConfigurationHandler configuration, String path, int value) {
        return value - configuration.getInt(path, 1);
    }

    public static int executeRemoveMiddle(int value) {
        int calc;
        if (value >= 2) {
            calc = value / 2;
        } else {
            calc = 0;
        }
        return value - calc;
    }

    public static int executeValues(ConfigurationHandler configuration, String path) {
        List<Integer> values = configuration.getIntList(path);
        if (values.size() >= 1) {
            return values.get(configuration.getRandom().nextInt(values.size()));
        }
        return 1;
    }

    public static int executeSingleValue(ConfigurationHandler configuration, String path) {
        return configuration.getInt(path, 1);
    }

    public static int getModeFromText(ConfigurationHandler configuration, String paramText, int value, String path) {
        paramText = paramText.toLowerCase();
        switch (paramText) {
            case "default":
            case "equal":
                return value;
            case "add_middle":
                return executeAddMiddle(value);
            case "remove_middle":
                return executeRemoveMiddle(value);
            case "add_double":
                return executeAddDouble(value);
            case "add":
                return executeAdd(configuration, path + "single-value", value);
            case "remove":
                return executeRemove(configuration, path + "single-value", value);
            case "values":
                return executeValues(configuration, path + "values");
            default:
            case "single":
            case "single_value":
                return executeSingleValue(configuration, path+ "single-value");

        }
    }

}
