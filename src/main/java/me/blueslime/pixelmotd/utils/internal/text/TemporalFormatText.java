package me.blueslime.pixelmotd.utils.internal.text;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import me.blueslime.pixelmotd.utils.internal.storage.PluginList;

public class TemporalFormatText {
    private final static String DEFAULT_FORMAT = "&8- &7%value%";
    private final String customFormat;
    private final String formatPath;
    private final String finderPath;


    public TemporalFormatText(String formatPath, String finderPath) {
        this(DEFAULT_FORMAT, formatPath, finderPath);
    }

    public TemporalFormatText(String defaultFormat, String formatPath, String finderPath) {
        this.customFormat = defaultFormat;
        this.formatPath = formatPath;
        this.finderPath = finderPath;
    }

    public String getCustomFormat() {
        return customFormat;
    }

    public String getFinderPath() {
        return finderPath;
    }

    public String getFormatPath() {
        return formatPath;
    }

    public PluginList<String> findIn(ConfigurationHandler configuration, boolean colorize) {
        PluginList<String> values = new PluginList<>();

        String format;

        if (colorize) {
            format = configuration.getString(
                    TextDecoration.LEGACY,
                    getFormatPath(),
                    customFormat
            );
        } else {
            format = configuration.getString(
                    getFormatPath(),
                    customFormat
            );
        }

        for (String value : configuration.getStringList(getFinderPath())) {
            values.add(
                    format.replace(
                            "%value%",
                            value
                    )
            );
        }
        return values.finish();
    }

    public static TemporalFormatText fromArguments(String format, String finder) {
        return new TemporalFormatText(
                format,
                finder
        );
    }

    public static TemporalFormatText fromArguments(String defaultFormat, String format, String finder) {
        return new TemporalFormatText(
                defaultFormat,
                format,
                finder
        );
    }

}
