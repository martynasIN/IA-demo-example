module com.ia.demoiii {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.ia.demoiii to javafx.fxml;
    opens com.ia.demoiii.models to javafx.base;
    exports com.ia.demoiii;
    exports com.ia.demoiii.controllers;
    opens com.ia.demoiii.controllers to javafx.fxml;
}