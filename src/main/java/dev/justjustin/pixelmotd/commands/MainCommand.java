package dev.justjustin.pixelmotd.commands;

import dev.justjustin.pixelmotd.SlimeFile;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;
import dev.mruniverse.slimelib.control.Control;

import java.util.List;

@Command(
        description = "Main Command of the PixelMOTD",
        usage = "/<command> (whitelist, blacklist, reload)"
)
public class MainCommand<T> implements SlimeCommand {

    private final String path = "commands.main-command.";

    private final SlimePlugin<T> plugin;

    public MainCommand(SlimePlugin<T> plugin) {
        this.plugin = plugin;
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

    }

    @Override
    public List<String> onTabComplete(Sender sender, String commandLabel, String[] args) {
        return SlimeCommand.super.onTabComplete(sender, commandLabel, args);
    }
}
