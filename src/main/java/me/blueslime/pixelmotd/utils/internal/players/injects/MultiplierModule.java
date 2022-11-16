package me.blueslime.pixelmotd.utils.internal.players.injects;

import me.blueslime.pixelmotd.utils.internal.players.PlayerModule;

public class MultiplierModule extends PlayerModule {

    public static final MultiplierModule INSTANCE = new MultiplierModule();

    @Override
    public int execute(Object... objects) {
        return 0;
    }
}
