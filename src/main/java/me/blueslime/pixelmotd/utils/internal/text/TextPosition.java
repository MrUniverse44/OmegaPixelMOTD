package me.blueslime.pixelmotd.utils.internal.text;

import dev.mruniverse.slimelib.utils.CenterText;

public class TextPosition {
    public static boolean isCentered(String message) {
        return message.contains("<center>");
    }

    public static String getCentered(String message) {
        if (!isCentered(message)) {
            return message;
        }
        return CenterText.sendToCenter(
                message.replace("<center>", "")
                        .replace("&", "§")
        );
    }
}
