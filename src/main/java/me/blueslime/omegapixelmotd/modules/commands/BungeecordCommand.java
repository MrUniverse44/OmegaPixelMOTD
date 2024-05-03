package me.blueslime.omegapixelmotd.modules.commands;

import me.blueslime.omegapixelmotd.modules.commands.interfaces.BungeecordCommandInterface;
import me.blueslime.omegapixelmotd.modules.initialization.BungeecordPixelMOTD;
import me.blueslime.wardenplugin.WardenPlugin;
import net.md_5.bungee.api.CommandSender;

public class BungeecordCommand extends BungeecordCommandInterface {

    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param plugin instanced
     * @param name   the name of this command
     */
    public BungeecordCommand(WardenPlugin<BungeecordPixelMOTD> plugin, String name) {
        super(plugin, name);
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

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}
