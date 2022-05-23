package dev.justjustin.pixelmotd.commands;

import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.utils.ListType;
import dev.justjustin.pixelmotd.utils.PlayerUtil;
import dev.justjustin.pixelmotd.utils.Updater;
import dev.justjustin.pixelmotd.utils.WhitelistLocation;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;
import dev.mruniverse.slimelib.control.Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(
        description = "Main Command of the PixelMOTD",
        usage = "/<command> (whitelist, blacklist, reload)"
)
public class MainCommand<T> implements SlimeCommand {

    private final Map<Integer, String> argumentsMap = new HashMap<>();

    private final String path = "commands.main-command.";

    private final SlimePlugin<T> plugin;

    public MainCommand(SlimePlugin<T> plugin) {
        this.plugin = plugin;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        argumentsMap.clear();

        Control commandSettings = plugin.getLoader().getFiles().getControl(SlimeFile.COMMANDS);

        argumentsMap.put(0, commandSettings.getString(path + "arguments.reload", "reload"));
        argumentsMap.put(1, commandSettings.getString(path + "arguments.whitelist.main", "whitelist"));


        argumentsMap.put(11, commandSettings.getString(path + "arguments.whitelist.list", "list"));
        argumentsMap.put(12, commandSettings.getString(path + "arguments.whitelist.add", "add"));
        argumentsMap.put(13, commandSettings.getString(path + "arguments.whitelist.remove", "remove"));
        argumentsMap.put(14, commandSettings.getString(path + "arguments.whitelist.toggle-on", "on"));
        argumentsMap.put(15, commandSettings.getString(path + "arguments.whitelist.toggle-off", "off"));


        argumentsMap.put(2, commandSettings.getString(path + "arguments.blacklist.main", "blacklist"));

        argumentsMap.put(21, commandSettings.getString(path + "arguments.blacklist.list", "list"));
        argumentsMap.put(22, commandSettings.getString(path + "arguments.blacklist.add", "add"));
        argumentsMap.put(23, commandSettings.getString(path + "arguments.blacklist.remove", "remove"));
        argumentsMap.put(24, commandSettings.getString(path + "arguments.blacklist.toggle-on", "on"));
        argumentsMap.put(25, commandSettings.getString(path + "arguments.blacklist.toggle-off", "off"));

        argumentsMap.put(3, commandSettings.getString(path + "arguments.updater", "updater"));
    }

    @Override
    public String getCommand() {
        return plugin.getLoader().getFiles().getControl(SlimeFile.COMMANDS).getString(path + "cmd", "pmotd");
    }

    @Override
    public List<String> getAliases() {
        return plugin.getLoader().getFiles().getControl(SlimeFile.COMMANDS).getStringList(path + "aliases");
    }

    @Override
    public void execute(Sender sender, String command, String[] arguments) {
        Control commandManager = plugin.getLoader().getFiles().getControl(SlimeFile.COMMANDS);
        Control messages  = plugin.getLoader().getFiles().getControl(SlimeFile.MESSAGES);

        if (arguments.length == 0) {

            String permission = commandManager.getString(path + "permissions.main", "pixelmotd.command.main");

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

        if (arguments[0].equalsIgnoreCase(argumentsMap.get(1))) {

            String permission = commandManager.getString(path + "permissions.whitelist", "pixelmotd.command.whitelist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;

            }

            executeList(commandManager, messages, command, sender, ListType.WHITELIST, removeArguments(arguments));
            return;
        }

        if (arguments[0].equalsIgnoreCase(argumentsMap.get(2))) {

            String permission = commandManager.getString(path + "permissions.blacklist", "pixelmotd.command.blacklist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;

            }

            executeList(commandManager, messages, command, sender, ListType.BLACKLIST, removeArguments(arguments));
            return;
        }

        if (arguments[0].equalsIgnoreCase(argumentsMap.get(0))) {

            String permission = commandManager.getString(path + "permissions.reload", "pixelmotd.command.reload");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;

            }
            plugin.reload();
            update();
        }

        if (arguments[0].equalsIgnoreCase(argumentsMap.get(3))) {
            final Control settings = plugin.getLoader().getFiles().getControl(SlimeFile.SETTINGS);

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

    private void executeList(Control commandManager, Control messages, String command, Sender sender, ListType type, String[] args) {
        if (args.length == 0) {
            List<String> message = commandManager.getStringList(path + "admin." + type.toString());

            message.replaceAll(line -> line.replace("%used command%", command));

            for (String text : message) {
                sender.sendColoredMessage(text);
            }

            return;
        }

        Control file = plugin.getLoader().getFiles().getControl(type.getFile());

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(1)))) {
            sender.sendColoredMessage("&aUser Name List: (Global Whitelist)");

            sendList(sender, file, "global-whitelist-players.players.by-name");

            sender.sendColoredMessage("&aUUID List: (Global Whitelist)");

            sendList(sender, file, "global-whitelist-players.players.by-uuid");

            WhitelistLocation place = WhitelistLocation.fromPlatform(plugin.getServerType());

            for (String keys : file.getContent(place.toString(), false)) {

                sender.sendColoredMessage("&aUser Name List: (" + place.toSingular() + "-" + keys + " Whitelist)");

                sendList(sender, file, place + "." + keys + ".players.by-name");

                sender.sendColoredMessage("&aUUID List: (" + place.toSingular() + "-" + keys + " Whitelist)");

                sendList(sender, file, place + "." + keys + ".players.by-uuid");

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

            WhitelistLocation whitelistLocation = WhitelistLocation.fromPlatform(plugin.getServerType());

            if (args.length == 3) {
                path = type + "." + whitelistLocation.toStringLowerCase() + "." + value + ".players." + PlayerUtil.getDestinyPath(value);
            }

            List<String> valueList = file.getStringList(path);

            if (valueList.contains(value)) {
                sender.sendColoredMessage(
                        messages.getString("messages.error.already-" + type + "ed", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
                );
                return;
            }

            valueList.add(value);

            plugin.getLoader().getFiles().getControl(type.getFile()).set(path, valueList);
            plugin.getLoader().getFiles().getControl(type.getFile()).save();
            plugin.getLoader().getFiles().getControl(type.getFile()).reload();

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

            WhitelistLocation whitelistLocation = WhitelistLocation.fromPlatform(plugin.getServerType());

            if (args.length == 3) {
                path = type + "." + whitelistLocation.toStringLowerCase() + "." + value + ".players." + PlayerUtil.getDestinyPath(value);
            }

            List<String> valueList = file.getStringList(path);

            if (!valueList.contains(value)) {
                sender.sendColoredMessage(
                        messages.getString("messages.error.not-" + type + "ed", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
                );
                return;
            }

            valueList.remove(value);

            plugin.getLoader().getFiles().getControl(type.getFile()).set(path, valueList);
            plugin.getLoader().getFiles().getControl(type.getFile()).save();
            plugin.getLoader().getFiles().getControl(type.getFile()).reload();

            sender.sendColoredMessage(
                    messages.getString("messages." + type + ".player.remove", "").replace("<type>", PlayerUtil.fromUnknown(value)).replace("<player>", value)
            );

            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(4)))) {

            String path = type + ".global.";

            WhitelistLocation whitelistLocation = WhitelistLocation.fromPlatform(plugin.getServerType());

            if (args.length >= 2) {

                path = type + "." + whitelistLocation.toStringLowerCase() + "." + args[1] + ".";

            }

            plugin.getLoader().getFiles().getControl(type.getFile()).set(
                    path + "enabled",
                    true
            );

            if (!sender.isConsoleSender() || !file.getStatus("custom-console-name.enabled")) {

                plugin.getLoader().getFiles().getControl(type.getFile()).set(
                        path + "author",
                        sender.getName()
                );

            } else {

                plugin.getLoader().getFiles().getControl(type.getFile()).set(
                        path + "author",
                        file.getString("custom-console-name.name", "")
                );

            }

            plugin.getLoader().getFiles().getControl(type.getFile()).set(
                    path + "reason",
                    file.getString("default-reasons" + type)
            );

            plugin.getLoader().getFiles().getControl(type.getFile()).save();
            plugin.getLoader().getFiles().getControl(type.getFile()).reload();
            return;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(type.getArgument(5)))) {
            String path = type + ".global.";

            WhitelistLocation whitelistLocation = WhitelistLocation.fromPlatform(plugin.getServerType());

            if (args.length >= 2) {

                path = type + "." + whitelistLocation.toStringLowerCase() + "." + args[1] + ".";

            }

            plugin.getLoader().getFiles().getControl(type.getFile()).set(
                    path + "enabled",
                    true
            );

            if (!sender.isConsoleSender() || !file.getStatus("custom-console-name.enabled")) {

                plugin.getLoader().getFiles().getControl(type.getFile()).set(
                        path + "author",
                        sender.getName()
                );

            } else {

                plugin.getLoader().getFiles().getControl(type.getFile()).set(
                        path + "author",
                        file.getString("custom-console-name.name", "")
                );

            }

            plugin.getLoader().getFiles().getControl(type.getFile()).set(
                    path + "reason",
                    file.getString("default-reasons" + type)
            );

            plugin.getLoader().getFiles().getControl(type.getFile()).save();
            plugin.getLoader().getFiles().getControl(type.getFile()).reload();
        }
    }

    private void sendList(Sender sender, Control file, String path) {
        for (String username : file.getStringList(path)) {
            sender.sendColoredMessage("  &8- &7" + username);
        }
    }

    private String[] removeArguments(String[] args) {
        String[] newArgs = new String[args.length - 2];
        System.arraycopy(args, 2, newArgs, 0, newArgs.length);
        return newArgs;
    }

    @Override
    public List<String> onTabComplete(Sender sender, String commandLabel, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 0) {
            arguments.add(argumentsMap.get(0));
            arguments.add(argumentsMap.get(1));
            arguments.add(argumentsMap.get(2));
            arguments.add(argumentsMap.get(3));
            return arguments;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(1))) {
            arguments.add(argumentsMap.get(11));
            arguments.add(argumentsMap.get(12));
            arguments.add(argumentsMap.get(13));
            arguments.add(argumentsMap.get(14));
            arguments.add(argumentsMap.get(15));
            return arguments;
        }

        if (args[0].equalsIgnoreCase(argumentsMap.get(2))) {
            arguments.add(argumentsMap.get(21));
            arguments.add(argumentsMap.get(22));
            arguments.add(argumentsMap.get(23));
            arguments.add(argumentsMap.get(24));
            arguments.add(argumentsMap.get(25));
            return arguments;
        }
        return arguments;
    }
}
