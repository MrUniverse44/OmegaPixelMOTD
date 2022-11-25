package me.blueslime.pixelmotd.motd.builder.hover;

import me.blueslime.pixelmotd.PixelMOTD;

import java.util.List;

public abstract class HoverController<T, I> {
    private final PixelMOTD<T> plugin;

    public HoverController(PixelMOTD<T> plugin) {
        this.plugin = plugin;
    }

    public abstract List<I> fromList(List<String> lines);

}
