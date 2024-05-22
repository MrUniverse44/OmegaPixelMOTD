package me.blueslime.omegapixelmotd;

import me.blueslime.omegapixelmotd.modules.commands.BukkitCommand;
import me.blueslime.omegapixelmotd.modules.commands.BungeecordCommand;
import me.blueslime.omegapixelmotd.modules.commands.VelocityCommand;
import me.blueslime.omegapixelmotd.modules.configurations.Configurations;
import me.blueslime.omegapixelmotd.modules.listeners.bukkit.BukkitListeners;
import me.blueslime.omegapixelmotd.modules.listeners.bungeecord.BungeecordListeners;
import me.blueslime.omegapixelmotd.modules.listeners.sponge.SpongeListeners;
import me.blueslime.omegapixelmotd.modules.listeners.velocity.VelocityListeners;
import me.blueslime.wardenplugin.WardenPlugin;
import me.blueslime.wardenplugin.modules.ModuleContainer;
import me.blueslime.wardenplugin.platform.Platforms;

public final class OmegaPixelMOTD extends WardenPlugin<Object> {

    public OmegaPixelMOTD(Class<?> pluginClazz, Object plugin) {
        super(pluginClazz, plugin);
    }

    @Override
    public void registration() {
        registerModule(
            ModuleContainer.create(
                Platforms.UNIVERSAL,
                (creator) -> {
                    Configurations configurations = new Configurations(this);
                    // Configuration files should be loaded first, the other part of the plugin should be loaded before this.
                    configurations.initialize();
                    // Register a configuration already initialized
                    creator.register(
                        configurations
                    );
                }
            )
        );
        registerModule(
            ModuleContainer.create(
                Platforms.VELOCITY,
                (creator) -> creator.register(
                    new VelocityListeners(this),
                    new VelocityCommand(this, "pmotd")
                )
            ),
            ModuleContainer.create(
                Platforms.BUKKIT,
                (creator) -> creator.register(
                    new BukkitListeners(this),
                    new BukkitCommand(this, "pmotd")
                )
            ),
            ModuleContainer.create(
                Platforms.BUNGEE_CORD,
                (creator) -> creator.register(
                    new BungeecordListeners(this),
                    new BungeecordCommand(this, "pmotd")
                )
            ),
            ModuleContainer.create(
                Platforms.SPONGE,
                (creator) -> creator.register(
                    new SpongeListeners(this)
                )
            )
        );
    }

    @SuppressWarnings("unchecked")
    public <T> WardenPlugin<T> cast() {
        return (WardenPlugin<T>) this;
    }

    @SuppressWarnings("unchecked")
    public <T> T core() {
        return ((WardenPlugin<T>)this).getPlugin();
    }
}
