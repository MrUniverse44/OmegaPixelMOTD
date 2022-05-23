package dev.justjustin.pixelmotd.utils;

import dev.mruniverse.slimelib.control.Control;

import java.util.List;

public class MotdPlayers {
    public static int executeAdd(Control control, String path, int value) {
        int singleValue = control.getInt(path);
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

    public static int executeRemove(Control control, String path, int value) {
        return value - control.getInt(path);
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

    public static int executeValues(Control control, String path) {
        List<Integer> values = control.getIntList(path);
        return values.get(control.getRandom().nextInt(values.size()));
    }

    public static int executeSingleValue(Control control, String path) {
        return control.getInt(path);
    }

    public static int getModeFromText(Control control, String paramText, int value, String path) {
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
                return executeAdd(control, path + "single-value", value);
            case "remove":
                return executeRemove(control, path + "single-value", value);
            case "values":
                return executeValues(control, path + "values");
            default:
            case "single":
            case "single_value":
                return executeSingleValue(control, path+ "single-value");

        }
    }

}
