package me.blueslime.omegapixelmotd.modules.motds;

import me.blueslime.omegapixelmotd.utils.toos.PluginTools;

import java.util.Locale;

public class MotdData {
    public static enum Protocol {
        ALWAYS_POSITIVE,
        ALWAYS_NEGATIVE(-1),
        DEFAULT;

        Protocol() {
            code = 0;
        }

        Protocol(int code) {
            this.code = code;
        }

        private int code;

        public Protocol replacement(int code) {
            this.code = code;
            return this;
        }

        public int getCode() {
            return code;
        }

        @Deprecated
        public static Protocol fromText(String paramText, int code) {
            return fromString(paramText, code);
        }

        public static Protocol fromOther(Protocol protocol) {
            for (Protocol p : values()) {
                if (p == protocol) {
                    return p;
                }
            }
            return protocol;
        }

        public static Protocol fromObject(Object object, int code) {
            if (object instanceof String) {
                return fromString(
                    (String)object,
                    code
                );
            }
            if (object instanceof Integer) {
                return fromInteger(
                    (int)object,
                    code
                );
            }
            return DEFAULT.replacement(code);
        }

        public static Protocol fromInteger(int parameter, int code) {
            switch (parameter) {
                default:
                case -1:
                case 0:
                    return DEFAULT.replacement(code);
                case 1:
                    return ALWAYS_POSITIVE.replacement(code);
                case 2:
                    return ALWAYS_NEGATIVE;
            }
        }

        public static Protocol fromString(String paramText, int code) {
            paramText = paramText.toLowerCase(Locale.ENGLISH);

            switch (paramText) {
                case "always negative":
                case "always-negative":
                case "always_negative":
                case "negative":
                case "2":
                    return ALWAYS_NEGATIVE;
                default:
                case "always_positive":
                case "always-positive":
                case "always positive":
                case "always":
                case "positive":
                case "1":
                    return ALWAYS_POSITIVE.replacement(code);
                case "default":
                case "0":
                case "-1":
                    return DEFAULT.replacement(code);
            }
        }
    }

    public static enum Type {
        MOTD_BANNED_USER(5),
        TEMP_BANNED_USER(6),
        OUTDATED_CLIENT(3),
        OUTDATED_SERVER(4),
        WHITELIST(1),
        BLACKLIST(2),
        INVALID(-1),
        NORMAL(0);

        private final int id;

        Type(int id) {
            this.id  = id;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public String toString() {
            return original().toLowerCase(Locale.ENGLISH);
        }

        public String original() {
            return super.toString();
        }

        public static Type fromString(String text) {
            text = text.toLowerCase(Locale.ENGLISH);

            if (text.equals("default")) {
                return NORMAL;
            }

            for (Type type : Type.values()) {

                String name = type.toString();

                String secondName = name.replace("_", "-");

                String thirdName = name.replace("_", " ");

                if (text.equals(name) || text.equals(secondName) || text.equals(thirdName)) {
                    return type;
                }
            }
            return INVALID;
        }

        public static Type fromId(int id) {
            for (Type type : Type.values()) {
                if (id == type.getId()) {
                    return type;
                }
            }
            return INVALID;
        }

        public static Type fromUnidentifiedObject(Object object) {
            String text = object.toString();

            if (PluginTools.isNumber(text)) {
                return fromId(Integer.parseInt(text));
            }

            return fromString(text);
        }
    }
}
