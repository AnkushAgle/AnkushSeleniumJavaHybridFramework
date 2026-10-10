
package com.aa.utility;

import java.nio.file.Path;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvironmentUtils {

    private static final Dotenv DOTENV = Dotenv.configure()
            .directory(Path.of(System.getProperty("user.dir")).toAbsolutePath().toString())
            .filename(".env")
            .ignoreIfMissing()
            .load();

    private EnvironmentUtils() {
    }

    public static String getValue(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Environment variable name must not be blank");
        }

        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = DOTENV.get(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing environment variable: " + key);
        }

        return value;
    }
}
