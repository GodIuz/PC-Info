package com.droidgeniuslabs.pcinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    private final Map<String, Stage> openStages = new HashMap<>(); // Track windows by title

    // Generic method to open/restore windows
    private void openWindow(String fxmlPath, String title) {
        Stage existingStage = openStages.get(title);

        if (existingStage == null || !existingStage.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();

                Stage newStage = new Stage();
                newStage.setTitle(title);
                newStage.setScene(new Scene(root));
                newStage.setAlwaysOnTop(true);
                newStage.initStyle(StageStyle.UNDECORATED);

                // Cleanup on close
                newStage.setOnCloseRequest(e -> openStages.remove(title));

                openStages.put(title, newStage);
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            existingStage.toFront(); // Bring existing window to front
            if (existingStage.isIconified()) {
                existingStage.setIconified(false); // Restore if minimized
            }
        }
    }

    // Consolidated window handlers
    public void openSpeedTest() {
        openWindow("speedtest_view.fxml", "Internet Speed Test");
    }

    public void openSystemInfo() {
        openWindow("system_info.fxml", "System Info");
    }

    public void openGetIps(ActionEvent actionEvent) {
        openWindow("get_ips.fxml", "Your IPs");
    }

    public void openCpuTemp() {
        openWindow("cpu_temp.fxml", "CPU Temp Monitor");
    }
}