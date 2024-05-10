package me.blueslime.omegapixelmotd.modules.motds;

import me.blueslime.omegapixelmotd.modules.motds.math.MotdPlayerCalculator;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.configuration.ConfigurationHandler;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class Motd {
    private final ConfigurationHandler configuration;

    private MotdData.Protocol protocol;

    private final String identifier;

    private String onlineExpression;
    private String maxExpression;

    private String protocolText;

    private String line1;
    private String line2;

    private boolean hover;
    private boolean icon;
    private boolean hex;
    private boolean max;

    private boolean online;

    public Motd(ConfigurationHandler configuration, String identifier) {
        this.configuration = configuration;
        this.identifier = identifier;
        initialize();
    }

    private void initialize() {
        String path = identifier + ".";

        this.protocol = MotdData.Protocol.fromObject(configuration.get(path + "protocol.state", "1"), 0);

        this.protocolText = configuration.getString(path + "protocol.message", "&fPlayers: &a%online%/1000");

        this.line1 = configuration.getString(path + "configuration.line-1", "");
        this.line2 = configuration.getString(path + "configuration.line-2", "");

        this.onlineExpression = configuration.getString(path + "online-players.modifier", "<value>");
        this.maxExpression = configuration.getString(path + "max-players.modifier", "<value>");

        if (protocolText != null && protocolText.contains("<before-the-icon>")) {
            this.protocolText = protocolText.replace("<before-the-icon>", "");

            String[] split = protocolText.split("<default>");

            String icon = split.length >= 2 ? split[0] : split.length == 1 ? split[0] : "";
            String def = split.length >= 2 ? split[1] : "";

            int max = configuration.getInt(path + "protocol.space-length", 30);

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < max; i++) {
                builder.append(" ");
            }

            this.protocolText = icon + builder + def;
        }

        this.hover = has(configuration.get(path + "hover.state", "default"));
        this.icon = has(configuration.get(path + "icon.state", "default"));
        this.hex = configuration.getString(identifier + ".text-loader", "with_hex").equalsIgnoreCase("with_hex");
        this.max = has(configuration.get(path + "max-players.state", "default"));
        this.online = has(configuration.get(path + "online-players.state", "default"));
    }

    public String getLine1(TextReplacer replacer) {
        return replacer.apply(line1);
    }

    public String getLine2(TextReplacer replacer) {
        return replacer.apply(line2);
    }

    public MotdData.Protocol getProtocol() {
        return protocol;
    }

    public String getProtocolMessage(TextReplacer replacer) {
        return replacer.apply(protocolText);
    }

    public boolean hasHover() {
        return hover;
    }

    private boolean has(Object data) {
        if (data instanceof String) {
            String v = (String) data;
            v = v.toLowerCase(Locale.ENGLISH);
            return v.equals("0") || v.equals("enabled") || v.equals("on") || v.equals("true") || v.equals("yes") || v.equals("custom");
        }
        if (data instanceof Integer) {
            int v = (int) data;
            return v == 0;
        }
        return (boolean) data;
    }

    public List<String> getHover(TextReplacer replacer) {
        List<String> lines = configuration.getStringList(identifier + ".hover.lines");
        lines.replaceAll(replacer::apply);
        return lines;
    }

    public int getOnlinePlayers(int original, int max) {
        if (!online) {
            return original;
        }
        String onlineExpression = generateRandomParameter(this.onlineExpression);
        int result = MotdPlayerCalculator.findExpression(
            onlineExpression.replace(
                "<value>",
                String.valueOf(original)
            ).replace(
                "<online>",
                String.valueOf(original)
            ).replace(
                "<max>",
                String.valueOf(max)
            )
        );
        return Math.max(result, 0);
    }

    public int getMaxPlayers(int original, int online) {
        if (!max) {
            return original;
        }
        String maxExpression = generateRandomParameter(this.maxExpression);
        int result = MotdPlayerCalculator.findExpression(
            maxExpression.replace(
                "<value>",
                String.valueOf(original)
            ).replace(
                "<max>",
                String.valueOf(original)
            ).replace(
                "<online>",
                String.valueOf(online)
            )
        );
        return Math.max(result, 0);
    }

    public boolean hasHex() {
        return hex;
    }

    public boolean hasServerIcon() {
        return icon;
    }

    public String getServerIcon() {
        return generateRandomParameter(
                configuration.getString(
                    identifier + ".icon.location",
                    "default-icon.png"
                )
        );
    }

    public ConfigurationHandler getConfiguration() {
        return configuration;
    }

    private String generateRandomParameter(String values) {
        if (!values.contains(";")) {
            return values;
        }

        Random random = ThreadLocalRandom.current();

        ArrayList<String> valueList = new ArrayList<>(Arrays.asList(values.split(";")));

        return valueList.get(
            random.nextInt(
                valueList.size()
            )
        );
    }
}
