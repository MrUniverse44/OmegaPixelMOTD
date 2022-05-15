package dev.justjustin.pixelmotd.commands;

import dev.justjustin.pixelmotd.SlimeFile;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;

import java.util.List;

@Command(
        description = "Main Command of the PixelMOTD",
        usage = "/<command> (whitelist, blacklist, reload)"
)
public class MainCommand<T> implements SlimeCommand {

    private final SlimePlugin<T> plugin;

    public MainCommand(SlimePlugin<T> plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return plugin.getLoader().getFiles().getControl(SlimeFile.COMMANDS).getString("commands.main-command.cmd", "pmotd");
    }

    @Override
    public List<String> getAliases() {
        return plugin.getLoader().getFiles().getControl(SlimeFile.COMMANDS).getStringList("commands.main-command.aliases");
    }

    @Override
    public void execute(Sender sender, String s, String[] strings) {

    }

    @Override
    public List<String> onTabComplete(Sender sender, String commandLabel, String[] args) {
        return SlimeCommand.super.onTabComplete(sender, commandLabel, args);
    }
}
