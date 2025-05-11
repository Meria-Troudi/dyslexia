package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationController {
    @FXML
    public void goToChangePassword(ActionEvent event) {
        switchScene(event, "/user/ChangePassword.fxml", "Change Password");
    }

    @FXML
    public void goToOverview(ActionEvent event) {
        switchScene(event, "/user/Settings.fxml", "Profile Settings");
    }

    @FXML
    public void goToEditProfile(ActionEvent event) {
        switchScene(event, "/user/ProfileEdit.fxml", "Edit Profile");
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        System.out.println("NavigationController initialized.");
    }
}
