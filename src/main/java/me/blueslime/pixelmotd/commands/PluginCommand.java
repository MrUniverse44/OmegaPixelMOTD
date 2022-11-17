package me.blueslime.pixelmotd.commands;

import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import dev.mruniverse.slimelib.source.SlimeSource;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.extras.ListType;
import me.blueslime.pixelmotd.utils.PlayerUtil;
import me.blueslime.pixelmotd.utils.Updater;
import me.blueslime.pixelmotd.utils.UserSide;
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

        String permission;

        if (argument.equals("help")) {
            permission = commandManager.getString("permissions.main", "pixelmotd.command.main");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            List<String> stringList = commandManager.getStringList("no-arguments");

            stringList.replaceAll(line -> line.replace("%plugin version%", plugin.getPluginInformation().getVersion()));

            for (String line : stringList) {
                sender.sendColoredMessage(line);
            }

            return;
        }
        if (argument.equals("reload")) {
            long before = System.currentTimeMillis();

            permission = commandManager.getString("permissions.reload", "pixelmotd.command.reload");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
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
            permission = commandManager.getString("permissions.whitelist", "pixelmotd.command.whitelist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            executeList(commandManager, messages, command, sender, ListType.WHITELIST, removeArgument(arguments));
            return;
        }
        if (argument.equals("blacklist")) {
            permission = commandManager.getString("permissions.blacklist", "pixelmotd.command.blacklist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            executeList(commandManager, messages, command, sender, ListType.BLACKLIST, removeArgument(arguments));
            return;
        }
        if (argument.equals("admin")) {
            if (sender.hasPermission("pixelmotd.admin")) {

                for (String message : commandManager.getStringList("commands.main-command.admin.main")) {
                    sender.sendColoredMessage(
                            message.replace("%used command%", command)
                    );
                }
            }
            return;
        }
        if (argument.equals("updater")) {
            permission = commandManager.getString("permissions.reload", "pixelmotd.command.reload");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
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
            return;
        }
        sender.sendColoredMessage("&eThis command doesn't exist. Use &f/pmotd &eto get all commands");
        if (sender.hasPermission("pixelmotd.admin")) {
            sender.sendColoredMessage("&a(Admin Permission Detected) &6Or use &f/pmotd admin &6to get all admin commands");
        }
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

        ConfigurationHandler file = plugin.getC;

        if (argument.equals("list")) {
            sender.sendColoredMessage("&aUser Name List: (Global Whitelist)");

            sendList(sender, file, "whitelist.global.players.by-name");

            sender.sendColoredMessage("&aUUID List: (Global Whitelist)");

            sendList(sender, file, "whitelist.global.players.by-uuid");

            UserSide place = UserSide.fromPlatform(plugin.getServerType());

            for (String keys : file.getContent(type + "." + place.toStringLowerCase(), false)) {

                if (keys.equalsIgnoreCase("global")) {
                    continue;
                }

                sender.sendColoredMessage("&aUser Name List: (" + place.toSingular() + "-" + keys + " " + type + ")");

                sendList(sender, file, type + "." + place.toStringLowerCase() + "." + keys + ".players.by-name");

                sender.sendColoredMessage("&aUUID List: (" + place.toSingular() + "-" + keys + " " + type + ")");

                sendList(sender, file, type + "." + place.toStringLowerCase() + "." + keys + ".players.by-uuid");

            }
            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(2)))) {
            if (args.length == 1 || args.length >= 4) {
                sender.sendColoredMessage(messages.getString("messages.error.invalid-arguments", ""));
                return;
            }

            String value = args[1];

            String path = type + ".global.players." + PlayerUtil.getDestinyPath(value);

            UserSide whitelistLocation = UserSide.fromPlatform(plugin.getServerType());

            if (args.length == 3) {
                path = type + "." + whitelistLocation.toStringLowerCase() + "." + args[2] + ".players." + PlayerUtil.getDestinyPath(value);
            }

            List<String> valueList = file.getStringList(path);

            if (valueList.contains(value)) {
                sender.sendColoredMessage(
                        messages.getString("messages.error.already-" + type + "ed", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
                );
                return;
            }

            valueList.add(value);

            plugin.getConfigurationHandler(type.getFile()).set(path, valueList);
            plugin.getConfigurationHandler(type.getFile()).save();
            plugin.getConfigurationHandler(type.getFile()).reload();

            sender.sendColoredMessage(
                    messages.getString("messages." + type + ".player.add", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
            );

            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(3)))) {
            if (args.length == 1 || args.length >= 4) {
                sender.sendColoredMessage(messages.getString("messages.error.invalid-arguments", ""));
                return;
            }

            String value = args[1];

            String path = type + ".global.players." + PlayerUtil.getDestinyPath(value);

            UserSide whitelistLocation = UserSide.fromPlatform(plugin.getServerType());

            if (args.length == 3) {
                path = type + "." + whitelistLocation.toStringLowerCase() + "." + args[2] + ".players." + PlayerUtil.getDestinyPath(value);
            }

            List<String> valueList = file.getStringList(path);

            if (!valueList.contains(value)) {
                sender.sendColoredMessage(
                        messages.getString("messages.error.not-" + type + "ed", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
                );
                return;
            }

            valueList.remove(value);

            plugin.getConfigurationHandler(type.getFile()).set(path, valueList);
            plugin.getConfigurationHandler(type.getFile()).save();
            plugin.getConfigurationHandler(type.getFile()).reload();

            sender.sendColoredMessage(
                    messages.getString("messages." + type + ".player.remove", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
            );

            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(4)))) {

            String path = type + ".global.";

            UserSide whitelistLocation = UserSide.fromPlatform(plugin.getServerType());

            if (args.length >= 2) {

                path = type + "." + whitelistLocation.toStringLowerCase() + "." + args[1] + ".";

            }

            plugin.getConfigurationHandler(type.getFile()).set(
                    path + "enabled",
                    true
            );

            if (!sender.isConsoleSender() || !file.getStatus("custom-console-name.enabled")) {

                plugin.getConfigurationHandler(type.getFile()).set(
                        path + "author",
                        sender.getName()
                );

            } else {

                plugin.getConfigurationHandler(type.getFile()).set(
                        path + "author",
                        file.getString("custom-console-name.name", "")
                );

            }

            plugin.getConfigurationHandler(type.getFile()).set(
                    path + "reason",
                    file.getString("default-reasons." + type)
            );

            sender.sendColoredMessage(
                    messages.getString("messages." + type + ".enabled", "")
            );

            plugin.getListenerManager().update();
            plugin.getConfigurationHandler(type.getFile()).save();
            plugin.getConfigurationHandler(type.getFile()).reload();
            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(5)))) {
            String path = type + ".global.";

            UserSide whitelistLocation = UserSide.fromPlatform(plugin.getServerType());

            if (args.length >= 2) {

                path = type + "." + whitelistLocation.toStringLowerCase() + "." + args[1] + ".";

            }

            plugin.getConfigurationHandler(type.getFile()).set(
                    path + "enabled",
                    false
            );

            if (!sender.isConsoleSender() || !file.getStatus("custom-console-name.enabled")) {

                plugin.getConfigurationHandler(type.getFile()).set(
                        path + "author",
                        sender.getName()
                );

            } else {

                plugin.getConfigurationHandler(type.getFile()).set(
                        path + "author",
                        file.getString("custom-console-name.name", "")
                );

            }

            plugin.getConfigurationHandler(type.getFile()).set(
                    path + "reason",
                    file.getString("default-reasons" + type)
            );

            sender.sendColoredMessage(
                    messages.getString("messages." + type + ".disabled", "")
            );

            plugin.getListenerManager().update();
            plugin.getConfigurationHandler(type.getFile()).save();
            plugin.getConfigurationHandler(type.getFile()).reload();
        }
    }

    private void sendConvertedList(SlimeSource<?> sender, String path) {
        sendConvertedList(sender, getConvertedList(sender, path));
    }

    private void sendConvertedList(SlimeSource<?> sender, List<String> list) {
        for (String message : list) {
            if (message.contains("<isAdmin>")) {
                if (sender.hasPermission("blitzskywars.admin")) {
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
