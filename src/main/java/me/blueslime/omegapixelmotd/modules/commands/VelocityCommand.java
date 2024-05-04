package me.blueslime.omegapixelmotd.modules.commands;

import com.velocitypowered.api.command.SimpleCommand;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.wardenplugin.modules.list.VelocityModule;

import java.util.List;

public class VelocityCommand extends VelocityModule implements SimpleCommand {
    private final String command;

    public VelocityCommand(OmegaPixelMOTD plugin, String command) {
        super(plugin.cast());
        this.command = command;
    }

    @Override
    public void initialize() {
        getPlugin().getPlugin().getCommandManager().register(
            command,
            this,
            "pixelmotd"
        );
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }

    /**
     * Executes the command for the specified invocation.
     *
     * @param invocation the invocation context
     */
    @Override
    public void execute(Invocation invocation) {

    }

    /**
     * Provides tab complete suggestions for the specified invocation.
     *
     * @param invocation the invocation context
     * @return the tab complete suggestions
     */
    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    /**
     * Tests to check if the source has permission to perform the specified invocation.
     *
     * <p>If the method returns {@code false}, the handling is forwarded onto
     * the players current server.
     *
     * @param invocation the invocation context
     * @return {@code true} if the source has permission
     */
    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
