package me.blueslime.pixelmotd.external.metrics.sponge;

import me.blueslime.pixelmotd.external.MetricsHandler;

public class SpongeMetricsHandler extends MetricsHandler<Object> {
    public SpongeMetricsHandler(Object main) {
        super(main, 15580);
    }

    @Override
    public void initialize() {

    }
}
