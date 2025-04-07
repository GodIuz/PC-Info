package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemInfoController{
    @FXML public VBox infoBox;
    @FXML public ProgressIndicator loadingIndicator;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public Button closeButton;

    @FXML
    public void initialize() {
        loadingIndicator.setVisible(true);
        infoBox.setVisible(false);

        executor.submit(() -> {
            try {
                SystemInfo si = new SystemInfo();
                HardwareAbstractionLayer hal = si.getHardware();
                OperatingSystem os = si.getOperatingSystem();
                CentralProcessor cpu = hal.getProcessor();
                GlobalMemory memory = hal.getMemory();
                Sensors sensors = hal.getSensors();

                String osInfo = "Operating System: " + os.toString();
                String cpuInfo = "CPU: " + cpu.getProcessorIdentifier().getName();
                String physicalCores = "Physical Cores: " + cpu.getPhysicalProcessorCount();
                String logicalCores = "Logical Cores: " + cpu.getLogicalProcessorCount();
                String cpuFreq = "Max Frequency: " + (cpu.getMaxFreq() / 1_000_000) + " MHz";
                String memoryTotal = "Total RAM: " + (memory.getTotal() / (1024 * 1024 * 1024)) + " GB";
                String cpuTemp = "CPU Temperature: " + sensors.getCpuTemperature() + " °C";
                String fanCount = "Fan(s): " + sensors.getFanSpeeds().length;

                Platform.runLater(() -> {
                    infoBox.getChildren().addAll(
                            new Label(osInfo),
                            new Label(cpuInfo),
                            new Label(physicalCores),
                            new Label(logicalCores),
                            new Label(cpuFreq),
                            new Label(memoryTotal),
                            new Label(cpuTemp),
                            new Label(fanCount)
                    );
                    loadingIndicator.setVisible(false);
                    infoBox.setVisible(true);
                    infoBox.setPrefWidth(150);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    infoBox.getChildren().add(new Label("❌ Error loading system info."));
                    infoBox.setVisible(true);
                });
            }
        });
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
