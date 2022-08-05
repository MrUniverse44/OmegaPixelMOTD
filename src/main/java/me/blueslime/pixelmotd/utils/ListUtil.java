package me.blueslime.pixelmotd.utils;

import java.util.List;

public class ListUtil {

    public static String ListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        int line = 0;
        int maxLine = list.size();
        for (String lines : list) {
            line++;
            if (line != maxLine) {
                builder.append(lines).append("\n");
            } else {
                builder.append(lines);
            }
        }
        return builder.toString();
    }

    public static boolean checkType(String type, List<String> paramList, String object) {
        if (type.equalsIgnoreCase("EQUALS")) {
            for (String name : paramList) {
                if (object.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        for (String name : paramList) {
            if (object.contains(name)) {
                return true;
            }
        }
        return false;
    }

}
