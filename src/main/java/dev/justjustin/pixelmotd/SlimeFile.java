package dev.justjustin.pixelmotd;

import dev.mruniverse.slimelib.SlimeFiles;
import dev.mruniverse.slimelib.SlimePlatform;

@SuppressWarnings("unused")
public enum SlimeFile implements SlimeFiles {
    SETTINGS("settings.yml"),
    SERVER_MOTDS("server-motds.yml", "motds"),
    JOIN_MOTDS("join-motds.yml", "motds"),
    EVENTS("events.yml"),
    MODES("modes.yml"),
    COMMANDS("commands.yml"),
    WHITELIST("whitelist.yml", "modes"),
    BLACKLIST("blacklist.yml", "modes"),
    OUTDATED_SERVER("outdated-server.yml", "modes"),
    OUTDATED_CLIENT("outdated-client.yml", "modes");

    private final boolean differentFolder;

    private final String file;

    private final String folder;

    private final String resource;

    SlimeFile(String file) {
        this.file = file;
        this.resource = file;
        this.differentFolder = false;
        this.folder = "";
    }

    SlimeFile(String file,String folder) {
        this.file = file;
        this.resource = file;
        this.differentFolder = true;
        this.folder = folder;
    }

    SlimeFile(String file,String folder,String resource) {
        this.file = file;
        this.resource = resource;
        this.differentFolder = true;
        this.folder = folder;
    }

    SlimeFile(String file,String folderOrResource,boolean isResource) {
        this.file = file;
        if(isResource) {
            this.resource = folderOrResource;
            this.folder = "";
            this.differentFolder = false;
        } else {
            this.resource = file;
            this.folder = folderOrResource;
            this.differentFolder = true;
        }
    }

    @Override
    public String getFileName() {
        return this.file;
    }

    @Override
    public String getFolderName() {
        return this.folder;
    }

    @Override
    public String getResourceFileName(SlimePlatform platform) {
        if (platform == SlimePlatform.VELOCITY || platform == SlimePlatform.SPONGE) {
            return "/" + this.resource;
        }
        return this.resource;
    }

    @Override
    public boolean isInDifferentFolder() {
        return this.differentFolder;
    }
}
