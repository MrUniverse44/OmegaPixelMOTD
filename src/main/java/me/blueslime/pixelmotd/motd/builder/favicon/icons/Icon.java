package me.blueslime.pixelmotd.motd.builder.favicon.icons;

import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public abstract class Icon<I> {
    private final SlimeLogs logs;
    private final I favicon;

    public Icon(SlimeLogs logs, File icon) {
        this.favicon  = getFavicon(icon);
        this.logs     = logs;
    }

    public I getFavicon() {
        return favicon;
    }

    public SlimeLogs getLogs() {
        return logs;
    }

    public abstract I getFavicon(File file);

    public void finish(File file) {
        if (logs != null && file != null && file.exists()) {
            logs.info("&7Icon loaded: &f" + file.getName());
        }
    }

    public void error(File file) {
        if (logs != null && file != null) {
            logs.error("&7File doesn't exists:&f " + file.getName());
        }
    }

    public void error(File file, Exception exception) {
        if (logs != null && file != null) {
            logs.error("Can't create favicon: " + file.getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);
        }
    }


}
