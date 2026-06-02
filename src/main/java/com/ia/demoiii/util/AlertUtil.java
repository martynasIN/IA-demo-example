package com.ia.demoiii.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class AlertUtil {
    public enum Status {
        INFO,
        ERROR,
        WARNING,
        CONFIRMATION
    }

    private AlertUtil() {
    }

    public static void show(Status status, String title, String message) {
        Alert alert = new Alert(toAlertType(status));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(response -> response == ButtonType.YES).isPresent();
    }

    private static Alert.AlertType toAlertType(Status status) {
        return switch (status) {
            case INFO -> Alert.AlertType.INFORMATION;
            case ERROR -> Alert.AlertType.ERROR;
            case WARNING -> Alert.AlertType.WARNING;
            case CONFIRMATION -> Alert.AlertType.CONFIRMATION;
        };
    }
}
