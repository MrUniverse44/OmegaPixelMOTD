package me.blueslime.pixelmotd.utils.internal;

import dev.mruniverse.slimelib.logs.SlimeLogs;

public class LoggerSetup {

    public static void initialize(SlimeLogs logs) {
        logs.getSlimeLogger().setHidePackage("me.blueslime.pixelmotd.");
        logs.getSlimeLogger().setContainIdentifier("me.blueslime.pixelmotd");
        logs.getSlimeLogger().setPluginName("PixelMOTD");

        logs.getProperties().getExceptionProperties().BASE_COLOR = "&9";

        logs.getPrefixes().getIssue().setPrefix("&c[PixelMOTD] &7");
        logs.getPrefixes().getWarn().setPrefix("&e[PixelMOTD] &7");
        logs.getPrefixes().getDebug().setPrefix("&9[PixelMOTD] &7");
        logs.getPrefixes().getInfo().setPrefix("&3[PixelMOTD] &7");
    }

    public static void shutdown(SlimeLogs logs, String version) {
        logs.info("This plugin is being unloaded, thanks for using it! v" + version);
    }
}
