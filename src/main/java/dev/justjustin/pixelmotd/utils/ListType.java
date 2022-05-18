package dev.justjustin.pixelmotd.utils;

public enum ListType {
    WHITELIST,
    BLACKLIST;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
