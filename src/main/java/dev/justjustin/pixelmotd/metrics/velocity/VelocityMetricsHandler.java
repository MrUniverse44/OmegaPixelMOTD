package dev.justjustin.pixelmotd.metrics.velocity;

import dev.justjustin.pixelmotd.initialization.velocity.VelocityMOTD;
import dev.justjustin.pixelmotd.metrics.MetricsHandler;

public class VelocityMetricsHandler extends MetricsHandler<Object> {
    public VelocityMetricsHandler(Object main) {
        super(main, 15579);
    }

    @Override
    public void initialize() {
        VelocityMOTD.getInstance().initMetrics(getId());
    }
}
