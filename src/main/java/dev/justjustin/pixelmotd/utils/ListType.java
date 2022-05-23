package dev.justjustin.pixelmotd.utils;

import dev.justjustin.pixelmotd.SlimeFile;

public enum ListType {
    WHITELIST(1),
    BLACKLIST(2);
    private final int id;

    ListType(int id) {
        this.id = id;
    }

    public SlimeFile getFile() {
        return SlimeFile.MODES;
    }

    public Integer getArgument(int argument) {
        return Integer.parseInt(id + "" + argument);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
