package me.blueslime.pixelmotd.commands;

public enum CommandArgument {
    WHITELIST_REMOVE(15, "whitelist.toggle-off", "off"),
    WHITELIST_MAIN(1, "whitelist.main", "whitelist"),
    WHITELIST_LIST(11, "whitelist.list", "list"),
    WHITELIST_ADD(12, "whitelist.add", "add"),
    WHITELIST_OFF(13, "whitelist.remove", "remove"),
    WHITELIST_ON(14, "whitelist.toggle-on", "on"),
    BLACKLIST_REMOVE(25, "blacklist.toggle-off", "off"),
    BLACKLIST_MAIN(2, "blacklist.main", "whitelist"),
    BLACKLIST_LIST(11, "whitelist.list", "list"),
    BLACKLIST_ADD(22, "blacklist.add", "add"),
    BLACKLIST_OFF(23, "blacklist.remove", "remove"),
    BLACKLIST_ON(24, "blacklist.toggle-on", "on"),
    UPDATER(3, "updater"),
    RELOAD(1, "reload");

    private final String argument;
    private final String def;
    private final int id;

    CommandArgument(int id, String def) {
        this.argument = def;
        this.def = def;
        this.id  = id;
    }

    CommandArgument(int id, String argument, String def) {
        this.argument = argument;
        this.def = def;
        this.id  = id;
    }

    public int id() {
        return id;
    }

    public String argument() {
        return argument;
    }

    public String def() {
        return def;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
