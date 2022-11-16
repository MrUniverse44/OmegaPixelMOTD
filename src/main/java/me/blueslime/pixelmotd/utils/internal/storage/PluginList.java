package me.blueslime.pixelmotd.utils.internal.storage;

import dev.mruniverse.slimelib.source.SlimeSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "unused"})
public class PluginList<K> {
    private List<String> replaces = null;
    private List<K> list;

    public PluginList() {
        this.list = new ArrayList<>();
    }

    public PluginList(List<K> list) {
        this.list = list;
    }

    public boolean add(K key) {
        return list.add(key);
    }

    public boolean remove(K key) {
        return list.remove(key);
    }

    public K remove(int index) {
        return list.remove(index);
    }

    public int size() {
        return list.size();
    }

    public boolean contains(K key) {
        return list.contains(key);
    }

    public boolean addAll(Collection<K> toAdd) {
        return list.addAll(toAdd);
    }

    public boolean addAll(PluginList<K> toAdd) {
        return list.addAll(toAdd.toOriginalList());
    }

    public boolean removeAll(Collection<K> toRemove) {
        return list.removeAll(toRemove);
    }

    public void forEach(Consumer<? super K> action) {
        list.forEach(action);
    }

    public void add(int index, K object) {
        list.add(index, object);
    }

    public K get(int index) {
        return list.get(index);
    }

    public PluginList<K> replace(String oldChar, String newChar) {
        if (replaces == null) {
            replaces = new ArrayList<>();
        }

        replaces.add(oldChar + ";;:;;" + newChar);

        return this;
    }

    @SuppressWarnings("UnusedAssignment")
    public PluginList<K> finish() {
        if (replaces != null) {
            if (list.size() > 1) {
                K index0 = list.get(0);

                if (!index0.getClass().equals(String.class)) {
                    return this;
                }

                List<String> replacement = new ArrayList<>(
                        (Collection<String>) list
                );

                for (String replace : replaces) {
                    String[] split = replace.split(";;:;;");

                    replacement.forEach(
                            line -> line = line.replace(split[0], split[1])
                    );
                }

                list = new ArrayList<>(
                        (Collection<K>)replacement
                );
            }
        }
        return this;
    }

    public void sendColoredList(SlimeSource<?> source) {
        if (list.size() < 1) {
            return;
        }

        K index0 = list.get(0);

        if (!index0.getClass().equals(String.class)) {
            for (K object : list) {
                source.sendColoredMessage(
                        object.toString()
                );
            }
        } else {
            List<String> substituteList = new ArrayList<>(
                    (Collection<String>) list
            );

            for (String object : substituteList) {
                source.sendColoredMessage(object);
            }
        }
    }

    public void sendUncoloredMessage(SlimeSource<?> source) {
        if (list.size() < 1) {
            return;
        }

        K index0 = list.get(0);

        if (!index0.getClass().equals(String.class)) {
            for (K object : list) {
                source.sendMessage(
                        object.toString()
                );
            }
        } else {
            List<String> substituteList = new ArrayList<>(
                    (Collection<String>) list
            );

            for (String object : substituteList) {
                source.sendMessage(object);
            }
        }
    }

    public void clear() {
        list.clear();
    }

    public List<K> toOriginalList() {
        if (list == null) {
            list = new ArrayList<>();
            return list;
        }
        return list;
    }
}
