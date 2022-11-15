package me.blueslime.pixelmotd.external.metrics.velocity;

import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import me.blueslime.pixelmotd.external.MetricsHandler;

public class VelocityMetricsHandler extends MetricsHandler<Object> {
    public VelocityMetricsHandler(Object main) {
        super(main, 15579);
    }

    @Override
    public void initialize() {
        VelocityMOTD.getInstance().initMetrics(getId());
    }
}
