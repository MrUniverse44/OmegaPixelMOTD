package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.MotdType;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public abstract class Icon<I> {

    private final MotdType motdType;
    private final SlimeLogs logs;
    private final I favicon;
    public Icon(SlimeLogs logs, MotdType motdType, File icon) {
        this.motdType = motdType;
        this.favicon  = getFavicon(icon);
        this.logs     = logs;
    }

    public MotdType getType() {
        return motdType;
    }

    public I getFavicon() {
        return favicon;
    }

    public SlimeLogs getLogs() {
        return logs;
    }

    public I getFavicon(File file) {
        return null;
    }


}
