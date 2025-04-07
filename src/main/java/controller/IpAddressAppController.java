package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class IpAddressAppController {
    @FXML
    public Button closeButton;
    public Label privateIp;
    public Label publicIp;


    private static final String PUBLIC_IP_API_URL = "https://api.ipify.org"; // External service to get public IP

    public void initialize() {
        Stage stage = new Stage();
        stage.setWidth(300);
        stage.setHeight(200);
        stage.setAlwaysOnTop(true);
        publicIp.setText(getPublicIP());
        privateIp.setText(getPrivateIP());
    }
    private String getPrivateIP() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (Exception e) {
            return "Error retrieving private IP";
        }
    }
    private String getPublicIP() {
        try {
            URL url = new URL(PUBLIC_IP_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            return "Error retrieving public IP";
        }
    }
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
