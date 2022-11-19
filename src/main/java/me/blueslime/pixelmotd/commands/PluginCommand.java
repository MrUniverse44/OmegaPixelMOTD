package me.blueslime.pixelmotd.commands;

import dev.mruniverse.slimelib.source.SlimeSource;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.extras.ListType;
import me.blueslime.pixelmotd.utils.Updater;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.utils.internal.storage.PluginList;
import me.blueslime.pixelmotd.utils.internal.text.TemporalFormatText;
import me.blueslime.pixelmotd.utils.internal.text.TextPosition;

import java.util.*;

@Command(
        description = "Main Command of the PixelMOTD",
        usage = "/<command> (whitelist, blacklist, reload)"
)
@SuppressWarnings("unused")
public class PluginCommand<T> implements SlimeCommand {
    private final PixelMOTD<T> plugin;

    public PluginCommand(PixelMOTD<T> plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return plugin.getCommandSettings().getString("command", "pmotd");
    }

    @Override
    public List<String> getAliases() {
        return plugin.getCommandSettings().getStringList("aliases");
    }

    @Override
    public void execute(SlimeSource sender, String command, String[] arguments) {
        ConfigurationHandler commandManager = plugin.getCommandSettings();
        ConfigurationHandler messages  = plugin.getMessages();

        String argument;

        if (arguments.length == 0) {
            argument = "help";
        } else {
            argument = arguments[0].toLowerCase();
        }

        if (argument.equals("help")) {
            if (isDeniedByPermission(sender, "help")) {
                sendConvertedList(sender, "no-permission");
                return;
            }

            sendConvertedList(sender, "help");
            return;
        }

        if (argument.equals("reload")) {
            long before = System.currentTimeMillis();

            if (isDeniedByPermission(sender, "reload")) {
                sendConvertedList(sender, "no-permission");
                return;
            }

            plugin.reload();

            long after = System.currentTimeMillis();

            sender.sendColoredMessage(
                    messages.getString("messages.reload", "").replace("<ms>", (after - before) + "")
            );

            return;
        }
        if (argument.equals("whitelist")) {
            if (isDeniedByPermission(sender, "whitelist")) {
                sendConvertedList(sender, "no-permission");
                return;
            }

            executeList(commandManager, messages, command, sender, ListType.WHITELIST, removeArgument(arguments));
            return;
        }
        if (argument.equals("blacklist")) {
            if (isDeniedByPermission(sender, "blacklist")) {
                sendConvertedList(sender, "no-permission");
                return;
            }

            executeList(commandManager, messages, command, sender, ListType.BLACKLIST, removeArgument(arguments));
            return;
        }
        if (argument.equals("updater")) {
            if (isDeniedByPermission(sender, "updater")) {
                sendConvertedList(sender, "no-permission");
                return;
            }

            final ConfigurationHandler settings = plugin.getConfigurationHandler(SlimeFile.SETTINGS);

            if (settings.getStatus("settings.update-check", true)) {
                sender.sendColoredMessage("&9Updater Command has been used, information will be posted in Console");

                Updater.UpdateType type = Updater.UpdateType.VERSION_CHECK;

                if (settings.getStatus("settings.auto-download-updates", true)) {
                    type = Updater.UpdateType.CHECK_DOWNLOAD;
                }

                new Updater(
                        plugin.getLogs(),
                        plugin.getPluginInformation().getVersion(),
                        37177, plugin.getDataFolder(),
                        type
                );

                sender.sendColoredMessage("&bUpdater has been applied read info in Console.");
            } else {
                sender.sendColoredMessage("&cUpdater is not enabled in settings.yml");
            }
        }
    }

    private boolean isDeniedByPermission(SlimeSource<?> source, String id) {
        return !source.hasPermission(
                obtainPermissionPath("permissions." + id, "pixelmotd." + id)
        ) && !source.hasPermission(
                obtainPermissionPath("permissions.bypass-permissions", "pixelmotd.*")
        );
    }

    private void executeList(ConfigurationHandler commandInformation, ConfigurationHandler messages, String command, SlimeSource<?> sender, ListType type, String[] args) {
        if (args.length == 0) {
            List<String> message = commandInformation.getStringList(type.toString());

            message.replaceAll(line -> line.replace("%used command%", command));

            for (String text : message) {
                sender.sendColoredMessage(text);
            }

            return;
        }

        String argument = args[0].toLowerCase(Locale.ENGLISH);

        if (argument.equals("list")) {
            //TODO: Set List Command
            return;
        }

        if (argument.equals("add")) {
            //TODO: Add command Argument

            return;
        }

        if (argument.equals("remove")) {
            //TODO: Add the remove argument
            return;
        }

        if (argument.equals("on")) {
            //TODO: Toggle on the whitelist or blacklist
            return;
        }

        if (argument.equals("off")) {
            //TODO: Toggle of the blacklist or whitelist
        }
    }

    private String obtainPermissionPath(String path, String defaultPermission) {
        return plugin.getCommandSettings().getString(path, defaultPermission);
    }

    private void sendConvertedList(SlimeSource<?> sender, String path) {
        sendConvertedList(sender, getConvertedList(sender, path));
    }

    private void sendConvertedList(SlimeSource<?> sender, List<String> list) {
        for (String message : list) {
            if (message.contains("<isAdmin>")) {
                if (sender.hasPermission("pixelmotd.admin") || sender.hasPermission("pixelmotd.*")) {
                    sender.sendColoredMessage(
                            message.replace("<isAdmin>", "")
                    );
                }
            } else {
                sender.sendColoredMessage(
                        message
                );
            }
        }
    }

    private void sendList(SlimeSource<?> sender, ConfigurationHandler configuration, TemporalFormatText temporalFormatter) {
        sendList(
                sender,
                temporalFormatter.findIn(
                        configuration,
                        true
                )
        );
    }

    private void sendList(SlimeSource<?> sender, PluginList<String> pluginList) {
        for (String value : pluginList.toOriginalList()) {
            sender.sendColoredMessage(value);
        }
    }

    private void sendSeveralMessages(SlimeSource<?> source, String... messages) {
        for (String message : messages) {
            source.sendColoredMessage(
                    message
            );
        }
    }

    private String[] removeArgument(String[] args) {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        return newArgs;
    }

    @Override
    public List<String> onTabComplete(SlimeSource sender, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

    @SuppressWarnings("UnusedAssignment")
    private List<String> getConvertedList(SlimeSource<?> source, String path) {
        List<String> clone = new ArrayList<>(
                plugin.getCommandSettings().getStringList(path)
        );
        clone.replaceAll(
                line -> line = TextPosition.getCentered(
                        line.replace("%sender%", source.getName())
                                .replace("%plugin version%", plugin.getPluginInformation().getVersion())
                                .replace("%plugin ver%", plugin.getPluginInformation().getVersion())
                )
        );
        return clone;
    }
}
