package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SpeedTestController {
    @FXML
    public Label resultLabel;
    @FXML
    public ProgressIndicator loadingIndicator;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String IP_API_URL = "http://ip-api.com/json/";
    public Button closeButton;


    private String getISPAndGeolocation() {
        try {
            URL url = new URL(IP_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject myResponse = new JSONObject(response.toString());
            String isp = myResponse.getString("isp");
            String country = myResponse.getString("country");
            return "ISP: " + isp + "\nLocation: " + country;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving ISP and geolocation info.";
        }
    }


    private String fetchServerLink(){
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return getISPAndGeolocation();
            }
        };
        task.setOnSucceeded(event -> {
            String locationInfo = task.getValue();
        });
        task.setOnFailed(event -> resultLabel.setText("Failed to get ISP and geolocation info."));
        new Thread(task).start();
        return null;
    }

    private String selectOptimalServer(String locationInfo) {
        if(locationInfo.contains("GR")){
            return "https://www.cosmote.gr";
        }
        else{
            return "https://www.google.com";
        }
    }

    public void runSpeedTest(javafx.event.ActionEvent actionEvent) {
        resultLabel.setText("");
        loadingIndicator.setVisible(true);

        executor.submit(() -> {
            try {
                long start = System.nanoTime();
                String localInfo = fetchServerLink();
                String serverLink = selectOptimalServer(localInfo);
                URL url = new URL(serverLink); // public speed test file
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(10000);
                int totalBytes = conn.getInputStream().readAllBytes().length;
                long end = System.nanoTime();

                double seconds = (end - start) / 1e9;
                double speedMbps = (totalBytes * 8) / (seconds * 1024 * 1024);

                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    resultLabel.setText(String.format("Download Speed: %.2f Mbps", speedMbps));
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    resultLabel.setText("❌ Error: Check your internet connection.");
                });
            }
        });
    }
    @FXML
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
