package me.blueslime.omegapixelmotd.modules.metrics.platforms.sponge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.TaskExecutorService;
import org.spongepowered.api.service.ServiceRegistration;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

/**
 * bStats collects some data for plugin authors.
 * <p>
 * Check out https://bStats.org/ to learn more about bStats!
 * <p>
 * DO NOT modify any of this class. Access it from your own plugin ONLY.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Metrics2 implements Metrics {
    /**
     * Internal class for storing information about old bStats instances.
     */
    private static class OutdatedInstance implements Metrics {
        private Object instance;
        private Method method;
        private PluginContainer plugin;

        private OutdatedInstance(Object instance, Method method, PluginContainer plugin) {
            this.instance = instance;
            this.method = method;
            this.plugin = plugin;
        }

        @Override
        public void cancel() {
            // Do nothing, handled once elsewhere
        }

        @Override
        public List<Metrics> getKnownMetricsInstances() {
            return new ArrayList<>();
        }

        @Override
        public JsonObject getPluginData() {
            try {
                return (JsonObject) method.invoke(instance);
            } catch (ClassCastException | IllegalAccessException | InvocationTargetException ignored) { }
            return null;
        }

        @Override
        public PluginContainer getPluginContainer() {
            return plugin;
        }

        @Override
        public int getRevision() {
            return 0;
        }

        @Override
        public void linkMetrics(Metrics metrics) {
            // Do nothing
        }
    }

    /**
     * A factory to create new Metrics classes.
     */
    public static class Factory {

        private final PluginContainer plugin;
        private final Logger logger;
        private final Path configDir;

        // The constructor is not meant to be called by the user.
        // The instance is created using Dependency Injection (https://docs.spongepowered.org/master/en/plugin/injection.html)
        @Inject
        private Factory(PluginContainer plugin, Logger logger, @ConfigDir(sharedRoot = true) Path configDir) {
            this.plugin = plugin;
            this.logger = logger;
            this.configDir = configDir;
        }

        /**
         * Creates a new Metrics2 class.
         *
         * @return A Metrics2 instance that can be used to register custom charts.
         * <p>The return value can be ignored, when you do not want to register custom charts.
         */
        public Metrics2 make(int pluginId) {
            return new Metrics2(plugin, logger, configDir, pluginId);
        }
    }

    static {
        // Do not touch. Needs to always be in this class.
        final String defaultName = "org:bstats:sponge:Metrics".replace(":", ".");
        if (!Metrics.class.getName().equals(defaultName)) {
            throw new IllegalStateException("bStats Metrics interface has been relocated or renamed and will not be run!");
        }
        if (!Metrics2.class.getName().equals(defaultName + "2")) {
            throw new IllegalStateException("bStats Metrics2 class has been relocated or renamed and will not be run!");
        }
    }

    // This ThreadFactory enforces the naming convention for our Threads
    private final ThreadFactory threadFactory = task -> new Thread(task, "bStats-Metrics");

    // Executor service for requests
    // We use an executor service to be independent of server TPS
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, threadFactory);

    // The version of bStats info being sent
    public static final int B_STATS_VERSION = 1;

    // The version of this bStats class
    public static final int B_STATS_CLASS_REVISION = 2;

    // The url to which the data is sent
    private static final String URL = "https://bStats.org/submitData/sponge";

    // The logger
    private Logger logger;

    // The plugin
    private final PluginContainer plugin;

    // The plugin id
    private final int pluginId;

    // The uuid of the server
    private String serverUUID;

    // Should failed requests be logged?
    private boolean logFailedRequests = false;

    // Should the sent data be logged?
    private static boolean logSentData;

    // Should the response text be logged?
    private static boolean logResponseStatusText;

    // A list with all known metrics class objects including this one
    private final List<Metrics> knownMetricsInstances = new CopyOnWriteArrayList<>();

    // A list with all custom charts
    private final List<CustomChart> charts = new ArrayList<>();

    // The config path
    private Path configDir;

    // The list of instances from the bStats 1 instance's that started first
    private List<Object> oldInstances = new ArrayList<>();

    // The constructor is not meant to be called by the user, but by using the Factory
    private Metrics2(PluginContainer plugin, Logger logger, Path configDir, int pluginId) {
        this.plugin = plugin;
        this.logger = logger;
        this.configDir = configDir;
        this.pluginId = pluginId;

        Sponge.eventManager().registerListeners(plugin, this);
    }

    @Listener
    public void startup(StartingEngineEvent<Server> event) {
        try {
            loadConfig();
        } catch (IOException e) {
            // Failed to load configuration
            logger.warn("Failed to load bStats config!", e);
            return;
        }

        Optional<Metrics> optional = Sponge.serviceProvider().provide(Metrics.class);

        if (optional.isPresent()) {
            optional.get().linkMetrics(this);
        } else {
            Optional<ServiceRegistration<Metrics>> service = Sponge.serviceProvider().registration(Metrics.class);
            service.ifPresent(metricsServiceRegistration -> metricsServiceRegistration.service().linkMetrics(this));
            startSubmitting();
        }

    }

    @Override
    public void cancel() {
        scheduler.shutdown();
    }

    @Override
    public List<Metrics> getKnownMetricsInstances() {
        return knownMetricsInstances;
    }

    @Override
    public PluginContainer getPluginContainer() {
        return plugin;
    }

    @Override
    public int getRevision() {
        return B_STATS_CLASS_REVISION;
    }

    /**
     * Links a bStats 1 metrics class with this instance.
     *
     * @param metrics An object of the metrics class to link.
     */
    private void linkOldMetrics(Object metrics) {
        try {
            Field field = metrics.getClass().getDeclaredField("plugin");
            field.setAccessible(true);
            PluginContainer plugin = (PluginContainer) field.get(metrics);
            Method method = metrics.getClass().getMethod("getPluginData");
            linkMetrics(new OutdatedInstance(metrics, method, plugin));
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            // Move on, this bStats is broken
        }
    }

    /**
     * Links an other metrics class with this class.
     * This method is called using Reflection.
     *
     * @param metrics An object of the metrics class to link.
     */
    @Override
    public void linkMetrics(Metrics metrics) {
        knownMetricsInstances.add(metrics);
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    public void addCustomChart(CustomChart chart) {
        Validate.notNull(chart, "Chart cannot be null");
        charts.add(chart);
    }

    @Override
    public JsonObject getPluginData() {
        JsonObject data = new JsonObject();

        String pluginName = "PixelMOTD";
        String pluginVersion = "10.0.0";

        data.addProperty("pluginName", pluginName);
        data.addProperty("id", pluginId);
        data.addProperty("pluginVersion", pluginVersion);
        data.addProperty("metricsRevision", B_STATS_CLASS_REVISION);

        JsonArray customCharts = new JsonArray();
        for (CustomChart customChart : charts) {
            // Add the data of the custom charts
            JsonObject chart = customChart.getRequestJsonObject(logger, logFailedRequests);
            if (chart == null) { // If the chart is null, we skip it
                continue;
            }
            customCharts.add(chart);
        }
        data.add("customCharts", customCharts);

        return data;
    }

    private void startSubmitting() {
        // bStats 1 cleanup. Runs once.
        try {
            File configPath = configDir.resolve("bStats").toFile();
            configPath.mkdirs();
            String className = readFile(new File(configPath, "temp.txt"));
            if (className != null) {
                try {
                    // Let's check if a class with the given name exists.
                    Class<?> clazz = Class.forName(className);

                    // Time to eat it up!
                    Field instancesField = clazz.getDeclaredField("knownMetricsInstances");
                    instancesField.setAccessible(true);
                    oldInstances = (List<Object>) instancesField.get(null);
                    for (Object instance : oldInstances) {
                        linkOldMetrics(instance); // Om nom nom
                    }
                    oldInstances.clear(); // Look at me. I'm the bStats now.

                    // Cancel its timer task
                    // bStats for Sponge version 1 did not expose its timer task - gotta go find it!
                    Map<Thread, StackTraceElement[]> threadSet = Thread.getAllStackTraces();
                    for (Map.Entry<Thread, StackTraceElement[]> entry : threadSet.entrySet()) {
                        try {
                            if (entry.getKey().getName().startsWith("Timer")) {
                                Field timerThreadField = entry.getKey().getClass().getDeclaredField("queue");
                                timerThreadField.setAccessible(true);
                                Object taskQueue = timerThreadField.get(entry.getKey());

                                Field taskQueueField = taskQueue.getClass().getDeclaredField("queue");
                                taskQueueField.setAccessible(true);
                                Object[] tasks = (Object[]) taskQueueField.get(taskQueue);
                                for (Object task : tasks) {
                                    if (task == null) {
                                        continue;
                                    }
                                    if (task.getClass().getName().startsWith(clazz.getName())) {
                                        ((TimerTask) task).cancel();
                                    }
                                }
                            }
                        } catch (Exception ignored) { }
                    }
                } catch (ReflectiveOperationException ignored) { }
            }
        } catch (IOException ignored) { }

        final Runnable submitTask = () -> {
            // Catch any stragglers from inexplicable post-server-load plugin loading of outdated bStats
            for (Object instance : oldInstances) {
                linkOldMetrics(instance); // Om nom nom
            }
            oldInstances.clear(); // Look at me. I'm the bStats now.
            // The data collection (e.g. for custom graphs) is done sync
            // Don't be afraid! The connection to the bStats server is still async, only the stats collection is sync ;)
            Scheduler scheduler = Sponge.asyncScheduler();

            TaskExecutorService service = scheduler.executor(getPluginContainer());;
            service.execute(this::submitData);
        };

        // Many servers tend to restart at a fixed time at xx:00 which causes an uneven distribution of requests on the
        // bStats backend. To circumvent this problem, we introduce some randomness into the initial and second delay.
        // WARNING: You must not modify and part of this Metrics class, including the submit delay or frequency!
        // WARNING: Modifying this code will get your plugin banned on bStats. Just don't do it!
        long initialDelay = (long) (1000 * 60 * (3 + Math.random() * 3));
        long secondDelay = (long) (1000 * 60 * (Math.random() * 30));
        scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30, TimeUnit.MILLISECONDS);

        // Let's log if things are enabled or not, once at startup:
        List<String> enabled = new ArrayList<>();
        List<String> disabled = new ArrayList<>();
        for (Metrics metrics : knownMetricsInstances) {
            if (Sponge.metricsConfigManager().collectionState(metrics.getPluginContainer()).asBoolean()) {
                enabled.add("PixelMOTD");
            } else {
                disabled.add("PixelMOTD");
            }
        }
        StringBuilder builder = new StringBuilder().append(System.lineSeparator());
        builder.append("bStats metrics is present in ").append((enabled.size() + disabled.size())).append(" plugins on this server.");
        builder.append(System.lineSeparator());
        if (enabled.isEmpty()) {
            builder.append("Presently, none of them are allowed to send data.").append(System.lineSeparator());
        } else {
            builder.append("Presently, the following ").append(enabled.size()).append(" plugins are allowed to send data:").append(System.lineSeparator());
            builder.append(enabled).append(System.lineSeparator());
        }
        if (disabled.isEmpty()) {
            builder.append("None of them have data sending disabled.");
            builder.append(System.lineSeparator());
        } else {
            builder.append("Presently, the following ").append(disabled.size()).append(" plugins are not allowed to send data:").append(System.lineSeparator());
            builder.append(disabled).append(System.lineSeparator());
        }
        builder.append("To change the enabled/disabled state of any bStats use in a plugin, visit the Sponge config!");
        logger.info(builder.toString());
    }

    /**
     * Gets the server specific data.
     *
     * @return The server specific data.
     */
    private JsonObject getServerData() {
        // Minecraft specific data
        int playerAmount = Math.min(Sponge.server().onlinePlayers().size(), 200);
        int onlineMode = Sponge.server().isOnlineModeEnabled() ? 1 : 0;
        String minecraftVersion = Sponge.game().platform().minecraftVersion().name();
        String spongeImplementation = Sponge.platform().container(Platform.Component.IMPLEMENTATION).metadata().id();

        // OS/Java specific data
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();

        JsonObject data = new JsonObject();

        data.addProperty("serverUUID", serverUUID);

        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("minecraftVersion", minecraftVersion);
        data.addProperty("spongeImplementation", spongeImplementation);

        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);

        return data;
    }

    /**
     * Collects the data and sends it afterwards.
     */
    private void submitData() {
        final JsonObject data = getServerData();

        JsonArray pluginData = new JsonArray();
        // Search for all other bStats Metrics classes to get their plugin data
        for (Metrics metrics : knownMetricsInstances) {
            if (!Sponge.metricsConfigManager().collectionState(metrics.getPluginContainer()).asBoolean()) {
                continue;
            }
            JsonObject plugin = metrics.getPluginData();
            if (plugin != null) {
                pluginData.add(plugin);
            }
        }

        if (pluginData.size() == 0) {
            return; // All plugins disabled, so we don't send anything
        }

        data.add("plugins", pluginData);

        // Create a new thread for the connection to the bStats server
        new Thread(() -> {
            try {
                // Send the data
                sendData(logger, data);
            } catch (Exception e) {
                // Something went wrong! :(
                if (logFailedRequests) {
                    logger.warn("Could not submit plugin stats!", e);
                }
            }
        }).start();
    }

    /**
     * Loads the bStats configuration.
     *
     * @throws IOException If something did not work :(
     */
    private void loadConfig() throws IOException {
        File configPath = configDir.resolve("bStats").toFile();
        configPath.mkdirs();
        File configFile = new File(configPath, "config.conf");
        HoconConfigurationLoader configurationLoader = HoconConfigurationLoader.builder().file(configFile).build();
        CommentedConfigurationNode node;
        if (!configFile.exists()) {
            configFile.createNewFile();
            node = configurationLoader.load();

            // Add default values
            node.node("enabled").set(false);
            // Every server gets it's unique random id.
            node.node("serverUuid").set(UUID.randomUUID().toString());
            // Should failed request be logged?
            node.node("logFailedRequests").set(false);
            // Should the sent data be logged?
            node.node("logSentData").set(false);
            // Should the response text be logged?
            node.node("logResponseStatusText").set(false);

            node.node("enabled").comment(
                    "Enabling bStats in this file is deprecated. At least one of your plugins now uses the\n" +
                            "Sponge config to control bStats. Leave this value as you want it to be for outdated plugins,\n" +
                            "but look there for further control");
            // Add information about bStats
            node.node("serverUuid").comment(
                    "bStats collects some data for plugin authors like how many servers are using their plugins.\n" +
                            "To control whether this is enabled or disabled, see the Sponge configuration file.\n" +
                            "Check out https://bStats.org/ to learn more :)"
            );
            node.node("configVersion").set(2);

            configurationLoader.save(node);
        } else {
            node = configurationLoader.load();

            if (!node.node("configVersion").virtual()) {

                node.node("configVersion").set(2);

                node.node("enabled").comment(
                        "Enabling bStats in this file is deprecated. At least one of your plugins now uses the\n" +
                                "Sponge config to control bStats. Leave this value as you want it to be for outdated plugins,\n" +
                                "but look there for further control");

                node.node("serverUuid").comment(
                        "bStats collects some data for plugin authors like how many servers are using their plugins.\n" +
                                "To control whether this is enabled or disabled, see the Sponge configuration file.\n" +
                                "Check out https://bStats.org/ to learn more :)"
                );

                configurationLoader.save(node);
            }
        }

        // Load configuration
        serverUUID = node.node("serverUuid").getString();
        logFailedRequests = node.node("logFailedRequests").getBoolean(false);
        logSentData = node.node("logSentData").getBoolean(false);
        logResponseStatusText = node.node("logResponseStatusText").getBoolean(false);
    }

    /**
     * Reads the first line of the file.
     *
     * @param file The file to read. Cannot be null.
     * @return The first line of the file or {@code null} if the file does not exist or is empty.
     * @throws IOException If something did not work :(
     */
    private String readFile(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return bufferedReader.readLine();
        }
    }

    /**
     * Sends the data to the bStats server.
     *
     * @param logger The used logger.
     * @param data The data to send.
     * @throws Exception If the request failed.
     */
    private static void sendData(Logger logger, JsonObject data) throws Exception {
        Validate.notNull(data, "Data cannot be null");
        if (logSentData) {
            logger.info("Sending data to bStats: {}", data);
        }
        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();

        // Compress the data to save bandwidth
        byte[] compressedData = compress(data.toString());

        // Add headers
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
        connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);

        // Send data
        connection.setDoOutput(true);
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(compressedData);
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        }
        if (logResponseStatusText) {
            logger.info("Sent data to bStats and received response: {}", builder);
        }
    }

    /**
     * Gzips the given String.
     *
     * @param str The string to gzip.
     * @return The gzipped String.
     * @throws IOException If the compression failed.
     */
    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        return outputStream.toByteArray();
    }

    /**
     * Represents a custom chart.
     */
    public static abstract class CustomChart {

        // The id of the chart
        private final String chartId;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         */
        CustomChart(String chartId) {
            if (chartId == null || chartId.isEmpty()) {
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            }
            this.chartId = chartId;
        }

        private JsonObject getRequestJsonObject(Logger logger, boolean logFailedRequests) {
            JsonObject chart = new JsonObject();
            chart.addProperty("chartId", chartId);
            try {
                JsonObject data = getChartData();
                if (data == null) {
                    // If the data is null we don't send the chart.
                    return null;
                }
                chart.add("data", data);
            } catch (Throwable t) {
                if (logFailedRequests) {
                    logger.warn("Failed to get data for custom chart with id {}", chartId, t);
                }
                return null;
            }
            return chart;
        }

        protected abstract JsonObject getChartData() throws Exception;

    }

    /**
     * Represents a custom simple pie.
     */
    public static class SimplePie extends CustomChart {

        private final Callable<String> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            String value = callable.call();
            if (value == null || value.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            data.addProperty("value", value);
            return data;
        }
    }

    /**
     * Represents a custom advanced pie.
     */
    public static class AdvancedPie extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
                    continue; // Skip this invalid
                }
                allSkipped = false;
                values.addProperty(entry.getKey(), entry.getValue());
            }
            if (allSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }
    }

    /**
     * Represents a custom drilldown pie.
     */
    public static class DrilldownPie extends CustomChart {

        private final Callable<Map<String, Map<String, Integer>>> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        public JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Map<String, Integer>> map = callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean reallyAllSkipped = true;
            for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                JsonObject value = new JsonObject();
                boolean allSkipped = true;
                for (Map.Entry<String, Integer> valueEntry : map.get(entryValues.getKey()).entrySet()) {
                    value.addProperty(valueEntry.getKey(), valueEntry.getValue());
                    allSkipped = false;
                }
                if (!allSkipped) {
                    reallyAllSkipped = false;
                    values.add(entryValues.getKey(), value);
                }
            }
            if (reallyAllSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }
    }

    /**
     * Represents a custom single line chart.
     */
    public static class SingleLineChart extends CustomChart {

        private final Callable<Integer> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            int value = callable.call();
            if (value == 0) {
                // Null = skip the chart
                return null;
            }
            data.addProperty("value", value);
            return data;
        }

    }

    /**
     * Represents a custom multi line chart.
     */
    public static class MultiLineChart extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
                    continue; // Skip this invalid
                }
                allSkipped = false;
                values.addProperty(entry.getKey(), entry.getValue());
            }
            if (allSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }

    }

    /**
     * Represents a custom simple bar chart.
     */
    public static class SimpleBarChart extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                JsonArray categoryValues = new JsonArray();
                categoryValues.add(new JsonPrimitive(entry.getValue()));
                values.add(entry.getKey(), categoryValues);
            }
            data.add("values", values);
            return data;
        }

    }

    /**
     * Represents a custom advanced bar chart.
     */
    public static class AdvancedBarChart extends CustomChart {

        private final Callable<Map<String, int[]>> callable;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, int[]> map = callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean allSkipped = true;
            for (Map.Entry<String, int[]> entry : map.entrySet()) {
                if (entry.getValue().length == 0) {
                    continue; // Skip this invalid
                }
                allSkipped = false;
                JsonArray categoryValues = new JsonArray();
                for (int categoryValue : entry.getValue()) {
                    categoryValues.add(new JsonPrimitive(categoryValue));
                }
                values.add(entry.getKey(), categoryValues);
            }
            if (allSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }

    }

}
