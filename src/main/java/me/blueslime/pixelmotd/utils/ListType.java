package me.blueslime.pixelmotd.utils;

import me.blueslime.pixelmotd.Configuration;

public enum ListType {
    WHITELIST(1),
    BLACKLIST(2);
    private final int id;

    ListType(int id) {
        this.id = id;
    }

    public Configuration getFile() {
        switch (this) {
            default:
            case BLACKLIST:
                return Configuration.BLACKLIST;
            case WHITELIST:
                return Configuration.WHITELIST;
        }
    }

    public Integer getArgument(int argument) {
        return Integer.parseInt(id + "" + argument);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
