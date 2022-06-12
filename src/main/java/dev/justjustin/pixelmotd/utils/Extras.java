package dev.justjustin.pixelmotd.utils;

import dev.justjustin.pixelmotd.PixelMOTD;

import java.util.regex.Pattern;

public class Extras {

    private final Pattern pattern = Pattern.compile("%player_(\\d)+%");

    private final PixelMOTD<?> plugin;

    private final int max;

    public Extras(PixelMOTD<?> plugin) {
        this.plugin = plugin;
        this.max    = plugin.getPlayerHandler().getMaxPlayers();
    }



}
