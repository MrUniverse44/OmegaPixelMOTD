package me.blueslime.pixelmotd.metrics.velocity;

import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import me.blueslime.pixelmotd.metrics.MetricsHandler;

public class VelocityMetricsHandler extends MetricsHandler<Object> {
    public VelocityMetricsHandler(Object main) {
        super(main, 15579);
    }

    @Override
    public void initialize() {
        VelocityMOTD.getInstance().initMetrics(getId());
    }
}
