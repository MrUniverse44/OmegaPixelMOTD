package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.MotdType;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public abstract class Icon<I> {

    private final MotdType motdType;
    private final SlimeLogs logs;
    private final String name;
    private final I favicon;
    public Icon(SlimeLogs logs, MotdType motdType, File icon) {
        this.motdType = motdType;
        this.favicon  = getFavicon(icon);
        this.logs     = logs;
        this.name     = icon.getName();
    }

    public MotdType getType() {
        return motdType;
    }

    public String getName() {
        return name;
    }

    public I getFavicon() {
        return favicon;
    }

    public SlimeLogs getLogs() {
        return logs;
    }

    public abstract I getFavicon(File file);


}
