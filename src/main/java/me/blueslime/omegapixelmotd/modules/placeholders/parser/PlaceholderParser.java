package me.blueslime.omegapixelmotd.modules.placeholders.parser;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderParser {
    public static String parse(String text) {
        return PlaceholderAPI.setPlaceholders(null, text);
    }
}
