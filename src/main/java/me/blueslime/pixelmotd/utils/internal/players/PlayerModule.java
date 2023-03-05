package me.blueslime.pixelmotd.utils.internal.players;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class PlayerModule {

    public abstract int execute(int online, String values);

    public static int generateRandomParameter(String values) {
        Random random = ThreadLocalRandom.current();

        ArrayList<Integer> valueList = new ArrayList<>();

        for (String value : values.split(";")) {
            valueList.add(
                    Integer.parseInt(
                            value
                    )
            );
        }

        return valueList.get(
                random.nextInt(
                        valueList.size()
                )
        );
    }

}
