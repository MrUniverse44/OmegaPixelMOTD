package me.blueslime.omegapixelmotd;

import me.blueslime.omegapixelmotd.modules.listeners.bukkit.BukkitListeners;
import me.blueslime.omegapixelmotd.modules.listeners.bungeecord.BungeecordListeners;
import me.blueslime.omegapixelmotd.modules.listeners.sponge.SpongeListeners;
import me.blueslime.omegapixelmotd.modules.listeners.velocity.VelocityListeners;
import me.blueslime.wardenplugin.WardenPlugin;
import me.blueslime.wardenplugin.modules.ModuleContainer;
import me.blueslime.wardenplugin.platform.Platforms;

public final class OmegaPixelMOTD extends WardenPlugin<Object> {

    public OmegaPixelMOTD(Object plugin) {
        super(plugin);
    }

    @Override
    public void registration() {
        registerModule(
            ModuleContainer.create(
                Platforms.VELOCITY,
                (creator) -> creator.register(
                    new VelocityListeners(this)
                )
            ),
            ModuleContainer.create(
                Platforms.BUKKIT,
                (creator) -> creator.register(
                    new BukkitListeners(this)
                )
            ),
            ModuleContainer.create(
                Platforms.BUNGEE_CORD,
                (creator) -> creator.register(
                    new BungeecordListeners(this)
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
}
