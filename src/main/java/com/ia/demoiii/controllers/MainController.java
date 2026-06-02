package com.ia.demoiii.controllers;

import com.ia.demoiii.StudentApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentArea;

    private Parent homeView;
    private Parent studentsView;

    @FXML
    public void initialize() throws IOException {
        showHome();
    }

    @FXML
    public void showHome() throws IOException {
        if (homeView == null) {
            homeView = FXMLLoader.load(StudentApplication.class.getResource("home.fxml"));
        }
        showView(homeView);
    }

    @FXML
    public void showStudents() throws IOException {
        studentsView = FXMLLoader.load(StudentApplication.class.getResource("student.fxml"));
        showView(studentsView);
    }

    private void showView(Parent view) {
        if (view instanceof Region region) {
            region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        contentArea.getChildren().setAll(view);
    }
}
