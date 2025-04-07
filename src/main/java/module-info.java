module com.droidgeniuslabs.pcinfo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.github.oshi;
    requires java.desktop;
    requires org.json;

    opens com.droidgeniuslabs.pcinfo to javafx.fxml;
    exports com.droidgeniuslabs.pcinfo;
    exports controller;
    opens controller to javafx.fxml;
}