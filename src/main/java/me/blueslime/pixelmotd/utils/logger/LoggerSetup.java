package me.blueslime.pixelmotd.utils.logger;

import dev.mruniverse.slimelib.logs.SlimeLogs;

public class LoggerSetup {
    public static void start(SlimeLogs logs) {
        logs.getPrefixes().getDebug().setPrefix("&bPixelMOTD | &f");
        logs.getPrefixes().getInfo().setPrefix("&ePixelMOTD | &f");
        logs.getPrefixes().getWarn().setPrefix("&cPixelMOTD | &f");
        logs.getPrefixes().getIssue().setPrefix("&6PixelMOTD | &f");

        logs.getProperties().getExceptionProperties().CODE_COLOR = "&e";
        logs.getProperties().getExceptionProperties().BASE_COLOR = "&e";

        logs.getSlimeLogger().setContainIdentifier("me.blueslime.pixelmotd");
        logs.getSlimeLogger().setHidePackage("me.blueslime.pixelmotd.");
    }
}
