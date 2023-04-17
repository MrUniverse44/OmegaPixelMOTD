package me.blueslime.pixelmotd.commands;

import dev.mruniverse.slimelib.source.SlimeSource;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.exception.NotFoundLanguageException;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.PlayerUtil;
import me.blueslime.pixelmotd.utils.list.PluginList;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(
        description = "Main Command of the PixelMOTD",
        usage = "/<command> (whitelist, blacklist, reload)"
)
public class PluginCommand<T> implements SlimeCommand {

    private final Map<Integer, String> argumentsMap = new HashMap<>();

    private final String path = "commands.main-command.";

    private final PixelMOTD<T> plugin;

    public PluginCommand(PixelMOTD<T> plugin) {
        this.plugin = plugin;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        argumentsMap.clear();

        for (CommandArgument argument : CommandArgument.values()) {
            argumentsMap.put(
                    argument.id(),
                    argument(
                            argument.argument(),
                            argument.def()
                    )
            );
        }
    }

    public String argument(String argument, String def) {
        return plugin.getConfigurationHandler(Configuration.COMMANDS).getString(path + "arguments." + argument, def);
    }

    @Override
    public String getCommand() {
        return plugin.getConfigurationHandler(Configuration.COMMANDS).getString(path + "cmd", "pmotd");
    }

    @Override
    public List<String> getAliases() {
        return plugin.getConfigurationHandler(Configuration.COMMANDS).getStringList(path + "aliases");
    }

    @Override
    public void execute(SlimeSource sender, String command, String[] arguments) {
        ConfigurationHandler commandManager = plugin.getCommandSettings();

        ConfigurationHandler messages;

        try {
            messages = plugin.getMessages();
        } catch (NotFoundLanguageException e) {
            plugin.getLogs().error("Can't initialize message file", e);
            return;
        }

        String argument;

        if (arguments.length == 0) {
            argument = "help";
        } else {
            argument = arguments[0].toLowerCase();
        }

        String permission;

        if (checkArgument(argument, CommandArgument.HELP)) {
            permission = commandManager.getString(path + "permissions.main", "pixelmotd.command.main");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            List<String> stringList = commandManager.getStringList(path + "no-arguments");

            stringList.replaceAll(line -> line.replace("%plugin version%", plugin.getPluginInformation().getVersion()));

            for (String line : stringList) {
                sender.sendColoredMessage(line);
            }

            return;
        }
        if (checkArgument(argument, CommandArgument.RELOAD)) {
            long before = System.currentTimeMillis();

            permission = commandManager.getString(path + "permissions.reload", "pixelmotd.command.reload");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;

            }

            plugin.reload();
            update();

            long after = System.currentTimeMillis();

            sender.sendColoredMessage(
                    messages.getString("messages.reload", "").replace("<ms>", (after - before) + "")
            );

            return;
        }
        if (checkArgument(argument, CommandArgument.WHITELIST_MAIN)) {
            permission = commandManager.getString(path + "permissions.whitelist", "pixelmotd.command.whitelist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            executeList(commandManager, messages, command, sender, ListType.WHITELIST, removeArgument(arguments));
            return;
        }
        if (checkArgument(argument, CommandArgument.BLACKLIST_MAIN)) {
            permission = commandManager.getString(path + "permissions.blacklist", "pixelmotd.command.blacklist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            executeList(commandManager, messages, command, sender, ListType.BLACKLIST, removeArgument(arguments));
            return;
        }
        if (argument.equalsIgnoreCase(
                CommandArgument.ADMIN.argument()
        )) {
            if (sender.hasPermission("pixelmotd.admin")) {

                for (String message : commandManager.getStringList("commands.main-command.admin.main")) {
                    sender.sendColoredMessage(
                            message.replace("%used command%", command)
                    );
                }
            }
            return;
        }
        if (checkArgument(argument, CommandArgument.UPDATER)) {
            permission = commandManager.getString(path + "permissions.reload", "pixelmotd.command.reload");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;
            }

            final ConfigurationHandler settings = plugin.getConfigurationHandler(Configuration.SETTINGS);

            if (settings.getStatus("settings.update-check", true)) {
                sender.sendColoredMessage("&9Updater Command has been disabled temporally.");
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

    private boolean checkArgument(String arg, CommandArgument argument) {
        return arg.equalsIgnoreCase(
                argumentsMap.get(argument.id())
        );
    }

    private void executeList(ConfigurationHandler commandManager, ConfigurationHandler messages, String command, SlimeSource<?> sender, ListType type, String[] args) {
        if (args.length == 0) {
            List<String> message = commandManager.getStringList(path + "admin." + type.toString());

            message.replaceAll(line -> line.replace("%used command%", command));

            for (String text : message) {
                sender.sendColoredMessage(text);
            }

            return;
        }

        ConfigurationHandler file = plugin.getConfigurationHandler(type.getFile());

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(1)))) {
            sender.sendColoredMessage("&aUser Name List: (Global Whitelist)");

            sendList(sender, file, "players.by-name");

            sender.sendColoredMessage("&aUUID List: (Global Whitelist)");

            sendList(sender, file, "players.by-uuid");

            PluginList place = PluginList.fromPlatform(plugin.getServerType());

            for (String keys : file.getContent(place.loweredName(), false)) {

                if (keys.equalsIgnoreCase("global")) {
                    continue;
                }

                sender.sendColoredMessage("&aUser Name List: (" + place.toSingular() + "-" + keys + " " + type + ")");

                sendList(sender, file, place.loweredName() + "." + keys + ".players.by-name");

                sender.sendColoredMessage("&aUUID List: (" + place.toSingular() + "-" + keys + " " + type + ")");

                sendList(sender, file, place.loweredName() + "." + keys + ".players.by-uuid");

            }
            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(2)))) {
            if (args.length == 1 || args.length >= 4) {
                sender.sendColoredMessage(messages.getString("messages.error.invalid-arguments", ""));
                return;
            }

            String value = args[1];

            String path = "players." + PlayerUtil.getDestinyPath(value);

            PluginList pluginList = PluginList.fromPlatform(plugin.getServerType());

            if (args.length == 3) {
                path = pluginList.loweredName() + "." + args[2] + ".players." + PlayerUtil.getDestinyPath(value);
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

            String path = "players." + PlayerUtil.getDestinyPath(value);

            PluginList pluginList = PluginList.fromPlatform(plugin.getServerType());

            if (args.length == 3) {
                path = pluginList.loweredName() + "." + args[2] + ".players." + PlayerUtil.getDestinyPath(value);
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

            String path = "";

            PluginList pluginList = PluginList.fromPlatform(plugin.getServerType());

            if (args.length >= 2) {

                path = pluginList.loweredName() + "." + args[1] + ".";

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

            plugin.reloadListeners();
            plugin.getConfigurationHandler(type.getFile()).save();
            plugin.getConfigurationHandler(type.getFile()).reload();
            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(5)))) {
            String path = "";

            PluginList pluginList = PluginList.fromPlatform(plugin.getServerType());

            if (args.length >= 2) {

                path = pluginList.loweredName() + "." + args[1] + ".";

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

            plugin.reloadListeners();
            plugin.getConfigurationHandler(type.getFile()).save();
            plugin.getConfigurationHandler(type.getFile()).reload();
        }
    }

    private void sendList(SlimeSource<?> sender, ConfigurationHandler file, String path) {
        for (String username : file.getStringList(path)) {
            sender.sendColoredMessage("  &8- &7" + username);
        }
    }

    private String[] removeArgument(String[] args) {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        return newArgs;
    }

    @Override
    public List<String> onTabComplete(SlimeSource sender, String commandLabel, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 0) {
            for (CommandArgument commandArgument : CommandArgument.getMain()) {
                arguments.add(
                        argumentsMap.get(
                                commandArgument.id()
                        )
                );
            }
            arguments.add("admin");
            return arguments;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(CommandArgument.WHITELIST_MAIN.id()))) {
            for (CommandArgument commandArgument : CommandArgument.getWhitelist()) {
                arguments.add(
                        argumentsMap.get(
                                commandArgument.id()
                        )
                );
            }
            return arguments;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(CommandArgument.BLACKLIST_MAIN.id()))) {
            for (CommandArgument commandArgument : CommandArgument.getBlacklist()) {
                arguments.add(
                        argumentsMap.get(
                                commandArgument.id()
                        )
                );
            }
            return arguments;
        }
        return arguments;
    }
}
