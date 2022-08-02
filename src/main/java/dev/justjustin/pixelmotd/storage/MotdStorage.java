package dev.justjustin.pixelmotd.storage;

import dev.justjustin.pixelmotd.MotdType;
import dev.mruniverse.slimelib.file.storage.FileStorage;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MotdStorage {

    private final Map<MotdType, List<String>> motdNameListMap = new EnumMap<>(MotdType.class);

    private final Random random = new Random();

    private FileStorage storage;

    public MotdStorage(FileStorage storage) {
        this.storage = storage;

        init();
    }

    public void update(FileStorage storage) {
        this.storage = storage;

        motdNameListMap.clear();
        init();
    }

    /**
     * Initialize Motd Name List
     */
    private void init() {
        for (MotdType motdType : MotdType.values()) {

            List<String> motdList = storage.getConfigurationHandler(motdType.getFile()).getContent(motdType.toString(), false);

            motdNameListMap.put(
                    motdType,
                    motdList
            );

        }
    }

    /**
     * This method returns a specific list of motds of a MotdType
     * @return List of Motds
     */
    private List<String> getNewMotdList(MotdType motdType) {
        return motdNameListMap.computeIfAbsent(motdType, V -> storage.getConfigurationHandler(motdType.getFile()).getContent(motdType.toString(), false));
    }

    /**
     * This method return the list of motds of the MotdType
     * @return List of Motds
     */
    public List<String> getMotdList(MotdType motdType) {
        return getNewMotdList(motdType);
    }

    /**
     * This method return a random motd from a list of the same type of motds.
     * @return String name
     */
    public String getRandomMotd(MotdType motdType) {
        List<String> motdList = getMotdList(motdType);

        return motdList.get(random.nextInt(motdList.size()));
    }
}
