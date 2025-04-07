package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import oshi.SystemInfo;

public class CpuTempController {

    public Button closeButton;
    @FXML
    private Label tempLabel;

    private final SystemInfo systemInfo = new SystemInfo();

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTemperature()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTemperature() {
        double temp = systemInfo.getHardware().getSensors().getCpuTemperature();
        String warning = "";

        if (temp >= 80) {
            warning = " 🔥 HIGH!";
            tempLabel.setStyle("-fx-text-fill: red;");
        } else if (temp >= 60) {
            warning = " ⚠️ Warm";
            tempLabel.setStyle("-fx-text-fill: orange;");
        } else {
            warning = " ✅ Cool";
            tempLabel.setStyle("-fx-text-fill: green;");
        }

        tempLabel.setText(String.format("CPU Temp: %.1f°C %s", temp, warning));
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
