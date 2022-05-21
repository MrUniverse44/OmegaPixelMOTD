package dev.justjustin.pixelmotd.utils;

import dev.justjustin.pixelmotd.SlimeFile;

public enum ListType {
    WHITELIST(1, SlimeFile.WHITELIST),
    BLACKLIST(2, SlimeFile.BLACKLIST);

    private final SlimeFile file;
    private final int id;

    ListType(int id, SlimeFile file) {
        this.file = file;
        this.id = id;
    }

    public SlimeFile getFile() {
        return file;
    }

    public Integer getArgument(int argument) {
        return Integer.parseInt(id + "" + argument);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
