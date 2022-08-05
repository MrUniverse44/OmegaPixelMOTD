package me.blueslime.pixelmotd.listener;

import me.blueslime.pixelmotd.MotdType;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public abstract class Icon<I> {

    private final MotdType motdType;
    private final SlimeLogs logs;
    private final I favicon;

    public Icon(SlimeLogs logs, MotdType motdType, File icon) {
        this.motdType = motdType;
        this.logs     = logs;
        this.favicon  = getFavicon(icon);
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

    public abstract I getFavicon(File file);


}
