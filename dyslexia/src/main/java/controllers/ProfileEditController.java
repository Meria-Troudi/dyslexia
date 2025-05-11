package controllers;
import entities.*;
import javafx.animation.FadeTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import services.*;
import services.session.UserSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
public class ProfileEditController extends NavigationController{
    public VBox teacherStepPane;
    public TextField subjectAreaField;
    public TextField availabilityField;
    public VBox studentStepPane;
    public ComboBox<String>  dyslexiaTypeComboBox,severityLevelComboBox;
    public Button saveChangesProfile;
    public TextField phoneField;
    public TextField emailField;
    public TextField lastNameField;
    public TextField firstNameField;
    public VBox psyStepPane;
    public TextField specializationField;
    public TextField therapeuticApproachField;
    public TextField specificNeedsField;
    public Label userIdLabel;
    public DatePicker birthDateField;
    public RadioButton maleRadio;
    public RadioButton femaleRadio;
    public VBox profilPane;
    public Button back1;
    public Button back2;
    @FXML
    private Button testButton;
    private User loggedInUser;
    private UserServiceImp userService = new UserServiceImp();
    private User user;
    @FXML private ImageView profileImageView;
    @FXML private Button changePhotoButton;

    private final FileChooser fileChooser = new FileChooser();
    private String newProfileImageBase64;  // <-- store as String
    private User currentUser;

    public void setUser(User user) {
        this.user = user;
        System.out.println("Welcome " + user.getFirstName());
    }
    @FXML
    public void initialize() {
        // decode+show existing:
        User u = UserSession.getCurrentUser().getUserLoggedIn();
        if (u.getProfileImage() != null) {
            byte[] bytes = Base64.getDecoder().decode(u.getProfileImage());
            profileImageView.setImage(new Image(new ByteArrayInputStream(bytes)));
        }
        // restrict to images
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        // load current user
        loggedInUser = UserSession.getCurrentUser().getUserLoggedIn();
        // if they already have an image (Base-64), decode + show
        if (loggedInUser.getProfileImage() != null) {
            byte[] bytes = Base64.getDecoder()
                    .decode(loggedInUser.getProfileImage());
            profileImageView.setImage(new Image(
                    new ByteArrayInputStream(bytes)
            ));
        }
        loggedInUser = UserSession.getCurrentUser().getUserLoggedIn();
        if (loggedInUser != null) {
            populateProfileFields();
            showRoleSpecificButton();

            // Hide special panes properly
            teacherStepPane.setVisible(false);
            teacherStepPane.setManaged(false);
            studentStepPane.setVisible(false);
            studentStepPane.setManaged(false);
        } else {
            System.err.println("No user is logged in!");
        }
    }

    private void showRoleSpecificButton() {
        testButton.setVisible(true);
        back1.setVisible(true);
        back2.setVisible(true);

    }

    private void populateProfileFields() {
        firstNameField.setText(loggedInUser.getFirstName());
        lastNameField.setText(loggedInUser.getLastName());
        emailField.setText(loggedInUser.getEmail());
        phoneField.setText(String.valueOf(loggedInUser.getPhoneNumber()));
        // Populate birth date and gender
        Date date = loggedInUser.getBirthDay();
        if (date instanceof java.sql.Date) {
            birthDateField.setValue(((java.sql.Date) date).toLocalDate());
        } else if (date != null) {
            birthDateField.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if ("Male".equals(loggedInUser.getGender())) {maleRadio.setSelected(true);
        } else if ("Female".equals(loggedInUser.getGender())) {
            femaleRadio.setSelected(true);
        }
        if (loggedInUser instanceof Teacher teacher) {
            subjectAreaField.setText(teacher.getSubjectArea());
            availabilityField.setText(teacher.getAvailability());
        } else if (loggedInUser instanceof DyslexicStudent student) {
            dyslexiaTypeComboBox.setValue(String.valueOf(student.getDyslexiaType()));
            severityLevelComboBox.setValue(String.valueOf(student.getSeverityLevel()));
            specificNeedsField.setText(student.getSpecificNeeds());
        }  else if (loggedInUser instanceof Psy psy) {
        specializationField.setText(psy.getSchedule());
        therapeuticApproachField.setText(psy.getBiography());
    }
    }

    @FXML
    public void goToSpecialField() {
        switch (loggedInUser.getRole()) {
            case ROLE_TEACHER -> switchPane(teacherStepPane);
            case ROLE_STUDENT -> switchPane(studentStepPane);
            case ROLE_PSY -> switchPane(psyStepPane);
            default -> System.err.println("No special pane found for user role.");        }
    }
    @FXML
    private void handleChangePhoto(ActionEvent ev) {
        Window owner = changePhotoButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(owner);                changePhotoButton.getScene().getWindow();
        if (file == null) return;

        // 1) size check
        if (file.length() > 2 * 1024 * 1024) {
            showError("Image too large (max 2MB)");
            return;
        }

        try {
            // 2) read raw bytes
            byte[] bytes = Files.readAllBytes(file.toPath());

            // 3) preview in ImageView
            profileImageView.setImage(new Image(
                    new ByteArrayInputStream(bytes)
            ));

            // 4) convert to Base-64 string
            newProfileImageBase64 = Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load image: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }
    @FXML
    public void handleUpdate() {
        try {
            loggedInUser.setFirstName(firstNameField.getText());
            loggedInUser.setLastName(lastNameField.getText());
            loggedInUser.setEmail(emailField.getText());
            loggedInUser.setPhoneNumber(Integer.parseInt(phoneField.getText()));
            loggedInUser.setGender(maleRadio.isSelected() ? "Male" : "Female");
            loggedInUser.setBirthDay(java.sql.Date.valueOf(birthDateField.getValue()));

            if (loggedInUser instanceof Teacher teacher) {
                teacher.setSubjectArea(subjectAreaField.getText());
                teacher.setAvailability(availabilityField.getText());
            } else if (loggedInUser instanceof DyslexicStudent student) {
                student.setDyslexiaType(DyslexiaType.valueOf(dyslexiaTypeComboBox.getValue()));
                student.setSeverityLevel(SeverityLevel.valueOf(severityLevelComboBox.getValue()));
                student.setSpecificNeeds(specificNeedsField.getText());
            } else if (loggedInUser instanceof Psy psy) {
                psy.setSchedule(specializationField.getText());
                psy.setBiography(therapeuticApproachField.getText());
            }

            userService.updateUser(loggedInUser);
        } catch (Exception e) {
            System.out.println(e);
        }
        // 1) attach the new image up-front
        if (newProfileImageBase64 != null) {
            loggedInUser.setProfileImage(newProfileImageBase64);
        }

// 2) call updateUser exactly once
        try {
            userService.updateUser(loggedInUser);
            showInfo("Profile updated successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Save failed: " + ex.getMessage());
        }

    }

    private void switchPane(VBox paneToShow) {
        List<VBox> allPanes = List.of(profilPane, teacherStepPane, studentStepPane);
        for (VBox pane : allPanes) {
            boolean show = pane == paneToShow;
            pane.setVisible(show);
            pane.setManaged(show); // Crucial: avoid blank space from hidden panes
            if (show) {
                FadeTransition fade = new FadeTransition(Duration.millis(300), pane);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.play();
            }
        }
    }
    @FXML
    public void backToCommonFields() {
        switchPane(profilPane);
        teacherStepPane.setVisible(false);
        studentStepPane.setVisible(false);
    }
    // Reuse inherited methods from NavigationController
    @FXML
    public void goToOverview(ActionEvent event) {
        super.goToOverview(event);
    }

    @FXML
    public void goToEditProfile(ActionEvent event) {
        super.goToEditProfile(event);
    }

    @FXML
    public void goToChangePassword(ActionEvent event) {
        super.goToChangePassword(event);
    }

}
