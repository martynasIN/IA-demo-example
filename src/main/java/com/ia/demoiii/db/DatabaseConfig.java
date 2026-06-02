package com.ia.demoiii.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalStateException("application.properties not found");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConfig() {
    }

    public static String getUrl() {
        return PROPERTIES.getProperty("db.url");
    }

    public static String getUser() {
        return PROPERTIES.getProperty("db.user");
    }

    public static String getPassword() {
        return PROPERTIES.getProperty("db.password", "");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUser(), getPassword());
    }
}
