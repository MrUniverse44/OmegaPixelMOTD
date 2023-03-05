package me.blueslime.pixelmotd.utils.internal.players.injects;

import me.blueslime.pixelmotd.utils.internal.players.PlayerModule;

public class MiddleModule extends PlayerModule {

    public static final MiddleModule INSTANCE = new MiddleModule();

    @Override
    public int execute(int online, String values) {
        return online / 2;
    }
}
