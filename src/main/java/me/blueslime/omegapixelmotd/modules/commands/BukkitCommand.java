package me.blueslime.omegapixelmotd.modules.commands;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.wardenplugin.modules.list.BukkitModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

public class BukkitCommand extends BukkitModule implements CommandExecutor {
    private final String command;
    public BukkitCommand(OmegaPixelMOTD plugin, String command) {
        super(plugin.cast());
        this.command = command;
    }

    @Override
    public void initialize() {
        PluginCommand command = getPlugin().getPlugin().getServer().getPluginCommand(this.command);

        if (command != null) {
            command.setExecutor(this);
            getLogs().info("Registered command: " + this.command);
        } else {
            getLogs().error("Could not find command: " + this.command);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }
}
