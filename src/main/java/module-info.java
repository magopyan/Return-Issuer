module JavaFxApplication {
    requires javafx.controls;
    requires javafx.fxml;
    opens sample to javafx.fxml;
    exports sample;
    exports utils;
    opens utils to javafx.fxml;
    exports controllers;
    opens controllers to javafx.fxml;
    requires selenium.java;
    requires selenium.api;
    requires selenium.chrome.driver;
    requires selenium.support;
}