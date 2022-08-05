package me.blueslime.pixelmotd.listener.bungeecord.events.type.login;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.bungeecord.events.abstracts.AbstractLoginListener;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class NormalLoginListener extends AbstractLoginListener implements Listener {
    public NormalLoginListener(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        execute(event);
    }
}
