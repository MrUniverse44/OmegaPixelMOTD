package dev.justjustin.pixelmotd.servers;

public class Server {
    private final String name;
    private final int online;

    public Server(String name, int online) {
        this.name = name;
        this.online = online;
    }

    public int getOnline() {
        return online;
    }

    public String getName() {
        return name;
    }
}
