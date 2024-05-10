package me.blueslime.omegapixelmotd.modules.motds.hover;

import java.util.UUID;

@SuppressWarnings("unused")
public class EmptyPlayerInfo {
    private String name;
    private UUID uniqueId;

    public EmptyPlayerInfo(String name, String id) {
        setName(name);
        setId(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        try {
            uniqueId = UUID.fromString(id);
        } catch (IllegalArgumentException ignored) { }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return uniqueId.toString().replace( "-", "" );
    }
}
