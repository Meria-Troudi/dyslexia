package controllers;

import entities.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class DetailsController {
    @FXML
    private ImageView statusIcon;  // Icon for the Account Enabled status
    public Label lblCSchedule;
    public Label lblPsychologistType;
    @FXML private ImageView ivProfile;
    @FXML private Label lblFullName, lblEmail, lblRole, lblBirthDate, lblGender, lblCreationDate, lblStatus, lblRoleSpecific;
    @FXML private Button btnClose, btnEdit;
    @FXML private VBox studentInfoBox, teacherInfoBox, psyInfoBox;
    @FXML private Label lblDyslexiaType, lblDyslexiaLevel, lblSpecificNeeds;
    @FXML private Label lblSubjectArea, lblAvailability;
    private User user;

    public void setUser(User user) {
        this.user = user;
        populateUserDetails();
    }
    private void populateUserDetails() {
        // Common fields
        lblFullName.setText(user.getFirstName() + " " + user.getLastName());
        lblEmail.setText(user.getEmail());
        lblRole.setText(user.getRole().name());
        lblBirthDate.setText(user.getBirthDay().toString());
        lblGender.setText(user.getGender());
        lblCreationDate.setText(user.getDateCreation().toString());

        // Handle account status (Enabled/Disabled)
        if (user.isEnabled()) {
            new Image(getClass().getResource("/images/enable.png").toExternalForm());
            lblStatus.setText("Enabled");
        } else {
            new Image(getClass().getResource("/images/disable.png").toExternalForm());
            lblStatus.setText("Disabled");
        }

        // Role-specific info
        populateRoleSpecificInfo(user);
        loadProfileImage(user);
    }

    private void populateRoleSpecificInfo(User user) {
        if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            teacherInfoBox.setVisible(true);
            teacherInfoBox.setManaged(true);
            lblSubjectArea.setText(teacher.getSubjectArea());
            lblAvailability.setText(teacher.getAvailability());
        } else if (user instanceof DyslexicStudent) {
            DyslexicStudent student = (DyslexicStudent) user;
            studentInfoBox.setVisible(true);
            studentInfoBox.setManaged(true);
            lblDyslexiaType.setText(String.valueOf(student.getDyslexiaType()));
            lblDyslexiaLevel.setText(String.valueOf(student.getSeverityLevel()));
            lblSpecificNeeds.setText(student.getSpecificNeeds());
        } else if (user instanceof Psy) {
            Psy psy = (Psy) user;
            psyInfoBox.setVisible(true);
            psyInfoBox.setManaged(true);
            lblPsychologistType.setText(psy.getBiography());
            String schedule = ((Psy) user).getSchedule() != null ? ((Psy) user).getSchedule().toString() : "No schedule available";
            lblCSchedule.setText(schedule);
        }
        if (lblRoleSpecific != null) {
            lblRoleSpecific.setText("Role Specific Information");
        }
    }

    private void loadProfileImage(User user) {
        if (user != null && user.getProfileImage() != null && !user.getProfileImage().isBlank()) {
            try {
                String base64Data = user.getProfileImage();
                if (base64Data.contains(",")) {
                    base64Data = base64Data.split(",")[1];  // Strip header
                }
                byte[] bytes = Base64.getDecoder().decode(base64Data);
                Image img = new Image(new ByteArrayInputStream(bytes));
                if (img.isError()) {
                    System.out.println("Error loading image: " + img.getException());
                }
                ivProfile.setImage(img);
            } catch (Exception e) {
                e.printStackTrace();
                ivProfile.setImage(new Image(getClass().getResource("/images/User.png").toExternalForm()));
            }
        } else {
            ivProfile.setImage(new Image(getClass().getResource("/images/User.png").toExternalForm()));
        }
    }

    // Close the details window
    @FXML
    private void closeDetails() {
        Stage stage = (Stage) lblFullName.getScene().getWindow();
        stage.close();
    }

    // Edit user (can be expanded based on your needs)
    @FXML
    private void editUser() {
        System.out.println("Editing user: " + user.getIdUser());
    }
}
