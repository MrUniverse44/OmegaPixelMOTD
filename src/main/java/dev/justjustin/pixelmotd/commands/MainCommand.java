package dev.justjustin.pixelmotd.commands;

import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.utils.ListType;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;
import dev.mruniverse.slimelib.control.Control;

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

            executeList(commandManager, messages, sender, ListType.WHITELIST, removeArguments(arguments));
            return;
        }

        if (arguments[0].equalsIgnoreCase(argumentsMap.get(2))) {

            String permission = commandManager.getString(path + "permissions.blacklist", "pixelmotd.command.blacklist");

            if (!sender.hasPermission(permission)) {

                String message = messages.getString("messages.error.permission", "");

                sender.sendColoredMessage(message.replace("<permission>", permission));
                return;

            }

            executeList(commandManager, messages, sender, ListType.BLACKLIST, removeArguments(arguments));
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
        }

        if (arguments[0].equalsIgnoreCase(argumentsMap.get(3))) {
            //TODO: Updater
        }
    }

    private void executeList(Control commandManager, Control messages, Sender sender, ListType listType, String[] arguments) {
        if (arguments.length == 0) {
            List<String> message = commandManager.getStringList(path + "admin." + listType.toString());

            //TODO: whitelist and blacklist commands.

            return;
        }
    }

    private String[] removeArguments(String[] args) {
        String[] newArgs = new String[args.length - 2];
        System.arraycopy(args, 2, newArgs, 0, newArgs.length);
        return newArgs;
    }

    @Override
    public List<String> onTabComplete(Sender sender, String commandLabel, String[] args) {
        return SlimeCommand.super.onTabComplete(sender, commandLabel, args);
    }
}
