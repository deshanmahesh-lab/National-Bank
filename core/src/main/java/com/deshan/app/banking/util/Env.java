package com.deshan.app.banking.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Env {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = Env.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("CRITICAL ERROR: application.properties file not found in classpath.");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties file.", e);
        }
    }

    private Env() {
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}