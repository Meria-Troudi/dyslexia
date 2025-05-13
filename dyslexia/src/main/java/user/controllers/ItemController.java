package user.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import user.services.UserServiceImp;
import user.entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Optional;

public class ItemController {
    @FXML private ImageView ivProfile;
    @FXML private Label lblFullName, lblEmail, lblRole,
            lblBirthDate, lblGender,
            lblCreationDate, lblStatus;
    @FXML private Button btnDetails, btnEdit, btnDelete;

    private User user;
    private AdminDashController parent;

    /**
     * Populates this row and wires button actions.
     */
    public void setUser(User u, AdminDashController parentController) {
        this.user   = u;
        this.parent = parentController;

        // 1) fill labels
        lblFullName  .setText(u.getFirstName() + " " + u.getLastName());
        lblEmail       .setText(u.getEmail());
        String roleName;
        switch (u.getRole()) {
            case ROLE_TEACHER -> roleName = "Teacher";
            case ROLE_STUDENT -> roleName = "Student";
            case ROLE_PSY     -> roleName = "Psychologist";
            case ROLE_ADMIN   -> roleName = "Administrator";
            default           -> roleName = "Unknown";
        }
        lblRole.setText(roleName);        lblBirthDate   .setText(u.getBirthDay().toString());
        lblGender      .setText(u.getGender());
        lblCreationDate.setText(u.getDateCreation().toString());
        lblStatus.setText(u.isEnabled() ? "Enabled" : "Disabled");

        // Null‑safe image handling: only if the view was injected
        if (ivProfile != null) {
            if (u.getProfileImage() == null || u.getProfileImage().isBlank()) {
                ivProfile.setVisible(true);
            } else {
                try {
                    ivProfile.setImage(new Image(u.getProfileImage(), true));
                } catch (Exception e) {
                    ivProfile.setVisible(true);
                }
            }
        }

        // 3) wire per‑row buttons
        btnDetails.setOnAction(e -> showDetails());
        btnDelete .setOnAction(e -> deleteUser());
    }

    private void showDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/showDetails.fxml"));
            AnchorPane root = loader.load();
            // Get the controller of showdetails.fxml
            DetailsController detailsController = loader.getController();
            detailsController.setUser(user);  // Pass the user object to the controller

            // Create a new scene and show it (could be in a new window)
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteUser() {
        // Confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Proceed with the deletion
            new UserServiceImp().removeUSER(user.getIdUser());
            parent.refresh();  // Refresh the user list after deletion
        } else {
            // If the user canceled the deletion, no action is taken.
            System.out.println("User deletion was canceled.");
        }
    }
}
