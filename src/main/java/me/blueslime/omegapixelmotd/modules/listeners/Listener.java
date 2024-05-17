package me.blueslime.omegapixelmotd.modules.listeners;

import me.blueslime.wardenplugin.logs.WardenLogs;

public interface Listener {
    void initialize();
    void shutdown();
    void reload();

    <T> T getOriginPlugin();

    WardenLogs getLogs();
}
