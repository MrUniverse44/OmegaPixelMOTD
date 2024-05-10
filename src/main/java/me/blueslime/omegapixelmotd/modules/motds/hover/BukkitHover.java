package me.blueslime.omegapixelmotd.modules.motds.hover;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.modules.list.BukkitModule;

import java.util.Collections;
import java.util.List;

public class BukkitHover extends BukkitModule {

    public BukkitHover(OmegaPixelMOTD plugin) {
        super(plugin.cast());
    }

    public List<EmptyPlayerInfo> generate(Motd motd, TextReplacer replacer) {
       return Collections.emptyList();
    }

    public EmptyPlayerInfo[] convert(List<EmptyPlayerInfo> list) {
        return new EmptyPlayerInfo[0];
    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}
