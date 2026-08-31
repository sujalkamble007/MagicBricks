package com.magicbricks.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads configuration values from config.properties file.
 * Single-purpose utility following SRP.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from: " + CONFIG_PATH, e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getLoginUrl() {
        return getProperty("login.url");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static int getActionDelay() {
        return Integer.parseInt(getProperty("action.delay"));
    }

    public static boolean isHighlightEnabled() {
        return Boolean.parseBoolean(getProperty("highlight.elements"));
    }

    public static String getTestDataExcelPath() {
        return getProperty("testdata.excel.path");
    }
}
