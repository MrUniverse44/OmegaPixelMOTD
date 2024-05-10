package me.blueslime.omegapixelmotd.utils.toos;

public class PluginTools {
    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
