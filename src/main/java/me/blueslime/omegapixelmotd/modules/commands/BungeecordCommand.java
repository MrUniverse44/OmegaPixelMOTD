package me.blueslime.omegapixelmotd.modules.commands;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.commands.interfaces.BungeecordCommandInterface;
import net.md_5.bungee.api.CommandSender;

public class BungeecordCommand extends BungeecordCommandInterface {

    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param plugin instanced
     * @param name   the name of this command
     */
    public BungeecordCommand(OmegaPixelMOTD plugin, String name) {
        super(plugin.cast(), name);
    }

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args   arguments used to invoke this command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

    }

    @Override
    public void initialize() {
        getPlugin().getPlugin().getProxy().getPluginManager().registerCommand(getPlugin().getPlugin(), this);
        getLogs().info("Bungeecord command initialized");
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}
