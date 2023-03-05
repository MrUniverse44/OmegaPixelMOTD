package me.blueslime.pixelmotd.utils.internal.players.injects;

import me.blueslime.pixelmotd.utils.internal.players.PlayerModule;

public class AdderModule extends PlayerModule {

    public static final AdderModule INSTANCE = new AdderModule();

    @Override
    public int execute(int online, String values) {
        if (values.contains(";")) {
            return online + generateRandomParameter(values);
        }

        return online + Integer.parseInt(values);
    }
}
