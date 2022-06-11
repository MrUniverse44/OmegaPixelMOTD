package dev.justjustin.pixelmotd.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@SuppressWarnings({"deprecation", "unused"})
public class Updater {
    private static final String USER_AGENT = "Updater by Stipess1 and recoded by MrUniverse44";
    // Direct download link
    private String downloadLink;
    // SlimeLogs
    private final SlimeLogs logs;
    // The folder where releases will be storage
    private final File releaseFolder;
    // The folder where builds will be storage
    private final File buildsFolder;
    // The plugin file
    private final File file;
    // ID of a project
    private final int id;
    // return a page
    private int page = 1;
    // Set the update type
    private final UpdateType updateType;
    // Get the outcome result
    private Result result = Result.SUCCESS;
    // If next page is empty set it to true, and get info from previous page.
    private boolean emptyPage;
    // Version returned from spigot
    private String version;
    // Tag returned from GitHub
    private String tag;
    // Version of the plugin
    private final String currentVersion;
    // If true updater is going to log progress to the console.
    private boolean logger = true;
    // Updater thread
    private final Thread thread;

    private static final String DOWNLOAD = "/download";
    private static final String VERSIONS = "/versions";
    private static final String PAGE = "?page=";
    private static final String API_RESOURCE = "https://api.spiget.org/v2/resources/";

    private static final String GITHUB_DOWNLOAD_LINK = "https://github.com/MrUniverse44/XPixelMotd4/releases/download/";

    private static final String GITHUB_API_RESOURCE = "https://api.github.com/repos/MrUniverse44/XPixelMotd4/releases/latest";

    public Updater(SlimeLogs logger, String currentVersion, int id, File file, UpdateType updateType) {
        this.logs = logger;
        this.currentVersion = currentVersion;
        // The folder where update will be downloaded
        File updateFolder = new File(file, "downloads");
        if (!updateFolder.exists()) {
            if (updateFolder.mkdirs()) logger.info("Downloads folder has been created");
        }
        this.releaseFolder = new File(updateFolder, "releases");
        if (!releaseFolder.exists()) {
            if (releaseFolder.mkdirs()) logger.info("Releases folder has been created");
        }
        this.buildsFolder = new File(updateFolder, "builds");
        if (!buildsFolder.exists()) {
            if (buildsFolder.mkdirs()) logger.info("Releases folder has been created");
        }
        this.id = id;
        this.file = file;
        this.updateType = updateType;

        downloadLink = API_RESOURCE + id;

        thread = new Thread(new UpdaterRunnable());
        thread.start();
    }

    @SuppressWarnings("unused")
    public void disableLogs() {
        logger = false;
    }

    public enum UpdateType {
        // Checks only the version
        VERSION_CHECK,
        // Downloads without checking the version
        DOWNLOAD,
        // If updater finds new version automatically it downloads it.
        CHECK_DOWNLOAD

    }

    public enum Result {

        UPDATE_FOUND,

        NO_UPDATE,

        SUCCESS,

        FAILED,

        BAD_ID
    }

    /**
     * Get the result of the update.
     *
     * @return result of the update.
     * @see Result
     */
    public Result getResult() {
        waitThread();
        return result;
    }

    /**
     * Get the latest version from spigot.
     *
     * @return latest version.
     */
    public String getVersion() {
        waitThread();
        return version;
    }

    /**
     * Check if id of resource is valid
     *
     * @param link link of the resource
     * @return true if id of resource is valid
     */
    private boolean checkResource(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            int code = connection.getResponseCode();

            if (code != 200) {
                connection.disconnect();
                result = Result.BAD_ID;
                return false;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Checks if there is any update available in SpigotMC.
     */
    private void checkUpdate(UpdateSite site) {
        switch (site) {
            case SPIGOTMC:
                checkSpigot();
                break;
            case GITHUB:
                checkGithub();
                break;
            case MC_MARKET:
                checkMcMarket();
                break;
        }
    }

    private void checkMcMarket() {
        //TODO
    }

    private void checkGithub() {
        try {
            URL url = new URL(GITHUB_API_RESOURCE);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = new JsonParser().parse(reader);
            JsonObject object = element.getAsJsonObject();
            element = object.get("tag_name");
            tag = element.toString().replaceAll("\"", "").replace("v", "");
            if (logger)
                logs.info("GITHUB | Checking for update...");
            if (shouldUpdate(tag, currentVersion) && updateType == UpdateType.VERSION_CHECK) {
                result = Result.UPDATE_FOUND;
                if (logger) {
                    logs.info("GITHUB | Update found!");
                }
            } else if (updateType == UpdateType.DOWNLOAD) {
                if (logger)
                    logs.info("GITHUB | Trying to download update..");
                download(UpdateSite.GITHUB);
            } else if (updateType == UpdateType.CHECK_DOWNLOAD) {
                if (shouldUpdate(tag, currentVersion)) {
                    if (logger) {
                        logs.info("GITHUB | Update found, downloading now...");
                    }
                    download(UpdateSite.GITHUB);

                } else {
                    if (logger) {
                        logs.info("GITHUB | You are using latest version of the plugin.");
                    }
                    result = Result.NO_UPDATE;
                }
            } else {
                if (logger) {
                    logs.info("GITHUB | You are using latest version of the plugin.");
                }
                result = Result.NO_UPDATE;
            }
        } catch (Exception exception) {
            if (exception instanceof FileNotFoundException) {
                logs.info("No updates are available on github");
                return;
            }
            logs.error("Can't find for updates on github :(");
            logs.error(exception);

        }
    }

    private void checkSpigot() {
        try {
            String page = Integer.toString(this.page);

            URL url = new URL(API_RESOURCE + id + VERSIONS + PAGE + page);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = new JsonParser().parse(reader);
            JsonArray jsonArray = element.getAsJsonArray();

            if (jsonArray.size() == 10 && !emptyPage) {
                connection.disconnect();
                this.page++;
                checkSpigot();
            } else if (jsonArray.size() == 0) {
                emptyPage = true;
                this.page--;
                checkSpigot();
            } else if (jsonArray.size() < 10) {
                element = jsonArray.get(jsonArray.size() - 1);

                JsonObject object = element.getAsJsonObject();
                element = object.get("name");
                version = element.toString().replaceAll("\"", "").replace("v", "");
                if (logger)
                    logs.info("SPIGOTMC | Checking for update...");
                if (shouldUpdate(version, currentVersion) && updateType == UpdateType.VERSION_CHECK) {
                    result = Result.UPDATE_FOUND;
                    if (logger)
                        logs.info("SPIGOTMC | Update found!");
                } else if (updateType == UpdateType.DOWNLOAD) {
                    if (logger)
                        logs.info("SPIGOTMC | Trying to download update..");
                    download(UpdateSite.SPIGOTMC);
                } else if (updateType == UpdateType.CHECK_DOWNLOAD) {
                    if (shouldUpdate(version, currentVersion)) {
                        if (logger)
                            logs.info("SPIGOTMC | Update found, downloading now...");
                        download(UpdateSite.SPIGOTMC);
                    } else {
                        if (logger)
                            logs.info("SPIGOTMC | You are using latest version of the plugin.");
                        result = Result.NO_UPDATE;
                    }
                } else {
                    if (logger)
                        logs.info("SPIGOTMC | You are using latest version of the plugin.");
                    result = Result.NO_UPDATE;
                }
            }
        } catch (Exception exception) {
            logs.error("Can't find for updates on spigotmc :(");
            logs.error(exception);

        }
    }

    /**
     * Checks if plugin should be updated
     *
     * @param newVersion remote version
     * @param oldVersion current version
     */
    private boolean shouldUpdate(String newVersion, String oldVersion) {
        return !newVersion.equalsIgnoreCase(oldVersion);
    }

    /**
     * Downloads the file
     */
    private void download(UpdateSite site) {
        switch (site) {
            case GITHUB:
                downloadGithub();
                break;
            case SPIGOTMC:
                downloadSpigotmc();
                break;
            case MC_MARKET:
                downloadMcMarket();
                break;
        }
    }

    private void downloadGithub() {
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            URL url = new URL(GITHUB_DOWNLOAD_LINK + tag + "/PixelMOTD-" + tag + ".jar");
            in = new BufferedInputStream(url.openStream());

            fout = new FileOutputStream(new File(buildsFolder, file.getName() + "-v" + version + ".jar"));

            final byte[] data = new byte[4096];
            int count;
            while ((count = in.read(data, 0, 4096)) != -1) {
                fout.write(data, 0, count);
            }

            logs.info("Latest build from Github has been downloaded.");

        } catch (Exception ignored) {
            if (logger)
                logs.info("Can't download latest version automatically, download it manually from website.");
            logs.info(" ");
            result = Result.FAILED;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException exception) {
                logs.error("Can't download");
                logs.error(exception);
            }
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (final IOException exception) {
                logs.error("Can't download");
                logs.error(exception);
            }
        }
    }

    private void downloadSpigotmc() {
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            URL url = new URL(downloadLink);
            in = new BufferedInputStream(url.openStream());

            fout = new FileOutputStream(new File(releaseFolder, file.getName() + "-v" + version + ".jar"));

            final byte[] data = new byte[4096];
            int count;
            while ((count = in.read(data, 0, 4096)) != -1) {
                fout.write(data, 0, count);
            }

            logs.info("Latest release from SpigotMC has been downloaded.");

        } catch (Exception ignored) {
            if (logger)
                logs.info("Can't download latest version automatically, download it manually from website.");
            logs.info(" ");
            result = Result.FAILED;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException exception) {
                logs.error("Can't download");
                logs.error(exception);
            }
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (final IOException exception) {
                logs.error("Can't download");
                logs.error(exception);
            }
        }
    }

    private void downloadMcMarket() {
        //TODO
    }

    /**
     * Updater depends on thread's completion, so it is necessary to wait for thread to finish.
     */
    private void waitThread()
    {
        if (thread != null && thread.isAlive())
        {
            try
            {
                thread.join();
            } catch (InterruptedException exception) {
                logs.error("Can't download");
                logs.error(exception);
            }
        }
    }

    public enum UpdateSite {
        SPIGOTMC,
        GITHUB,
        MC_MARKET
    }

    public class UpdaterRunnable implements Runnable
    {

        public void run() {
            if (checkResource(downloadLink))
            {
                downloadLink = downloadLink + DOWNLOAD;
                checkUpdate(UpdateSite.SPIGOTMC);
                checkUpdate(UpdateSite.GITHUB);
            }
        }
    }
}
