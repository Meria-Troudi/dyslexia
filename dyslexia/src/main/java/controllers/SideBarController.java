package controllers;

import entities.Roles;
import entities.User;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.session.UserSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SideBarController implements Initializable {

    @FXML public ImageView profileImageView;
    public Button addPhotoButton;
    @FXML
    private StackPane profileImageContainer;
    @FXML private Label rolelabel;
    @FXML private Label username;

    @FXML private VBox mainArea;
    @FXML private AnchorPane topBar;
    @FXML private VBox sideBar;

    @FXML private Button btnCourses, btnDashboard, btnEvents,
            btnPsychologists, btnPublications, btnRec,
            btnSettings, btn_notif, btnLogout;

    @FXML private Pane contentArea;
    @FXML private Button toggleButton;

    @FXML private Label dashboardLabel, coursesLabel,
            publicationsLabel, psyLabel,
            eventsLabel, recLabel, settingsLabel;

    private boolean isExpanded = false;
    private static final double EXPANDED_WIDTH = 170;
    private static final double COLLAPSED_WIDTH = 90;
    private static final double EXPANDED_TOGGLE_X = 120;
    private static final double COLLAPSED_TOGGLE_X = 25;
    private static final Duration SIDEBAR_ANIMATION_DURATION = Duration.millis(300);
    private static final Duration LABEL_FADE_DURATION = Duration.millis(150);

    private List<Label> labels;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = UserSession.getCurrentUser().getUserLoggedIn();
        boolean hasImage = user.getProfileImage() != null && !user.getProfileImage().isBlank();
        if (hasImage) {
            try {
                byte[] bytes = Base64.getDecoder().decode(user.getProfileImage());
                profileImageView.setImage(new Image(new ByteArrayInputStream(bytes)));
                profileImageView.setVisible(true);
                addPhotoButton.setVisible(false);
            } catch (IllegalArgumentException e) {
                // corrupt Base64 → treat as “no image”
                profileImageView.setVisible(false);
                addPhotoButton.setVisible(true);
            }
        } else {
            // no image at all
            profileImageView.setVisible(false);
            addPhotoButton.setVisible(true);
        }

        labels = Arrays.asList(
                dashboardLabel, coursesLabel, publicationsLabel,
                psyLabel, eventsLabel, recLabel, settingsLabel
        );
        collapseSidebar();
        loadUserInfo(user);

        if (user != null && user.getRole() == Roles.ROLE_PSY) {
            hideNonPsyMenuItems();
        }
    }

    private void loadUserInfo(User user) {
        if (user == null) {
            username.setText("Unknown");
            rolelabel.setText("Guest");
            return;
        }

        username.setText(user.getFirstName() + " " + user.getLastName());

        switch (user.getRole()) {
            case ROLE_ADMIN:
                rolelabel.setText("ADMIN");
                break;
            case ROLE_TEACHER:
                rolelabel.setText("TEACHER");
                break;
            case ROLE_STUDENT:
                rolelabel.setText("STUDENT");
                break;
            case ROLE_PSY:
                rolelabel.setText("PSYCHOLOGIST");
                break;
            default:
                rolelabel.setText("UNKNOWN");
        }
    }

    private void hideNonPsyMenuItems() {
        List<Node> toHide = Arrays.asList(
                btnCourses, coursesLabel,
                btnDashboard, dashboardLabel,
                btnEvents, eventsLabel,
                btnPublications, publicationsLabel,
                btnRec, recLabel
                // Removed btnPsychologists and psyLabel to keep them visible for psychologists
        );
        toHide.forEach(n -> {
            if (n != null) {
                n.setVisible(false);
                n.setManaged(false); // Ensures layout doesn't reserve space
                profileImageView.setManaged(false);
                profileImageView.setLayoutX(15);     // Horizontal distance from the left
                profileImageView.setLayoutY(10);     // Vertical distance from the top
            }
        });
    }

    private void collapseSidebar() {
        sideBar.setPrefWidth(COLLAPSED_WIDTH);
        labels.forEach(label -> {
            label.setVisible(false);
            label.setOpacity(0);
        });
    }

    @FXML private void toggleSidebar() {
        double targetWidth = isExpanded ? COLLAPSED_WIDTH : EXPANDED_WIDTH;
        double targetX = isExpanded ? COLLAPSED_TOGGLE_X : EXPANDED_TOGGLE_X;

        animateSidebar(targetWidth, targetX);
        animateLabels(isExpanded);
        isExpanded = !isExpanded;
    }

    private void animateSidebar(double width, double toggleX) {
        Timeline timeline = new Timeline(
                new KeyFrame(SIDEBAR_ANIMATION_DURATION,
                        new KeyValue(sideBar.prefWidthProperty(), width, Interpolator.EASE_BOTH),
                        new KeyValue(toggleButton.layoutXProperty(), toggleX, Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
    }

    private void animateLabels(boolean fadeOut) {
        for (Label label : labels) {
            if (!fadeOut) {
                label.setVisible(true);
                label.setOpacity(0);
            }
            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(label.opacityProperty(), fadeOut ? 1.0 : 0.0)),
                    new KeyFrame(LABEL_FADE_DURATION,
                            new KeyValue(label.opacityProperty(), fadeOut ? 0.0 : 1.0))
            );
            if (fadeOut) fade.setOnFinished(e -> label.setVisible(false));
            fade.play();
        }
    }

    // ======================= Navigation Handlers ========================

    @FXML void goToDashboard(ActionEvent event) {
        User user = UserSession.getCurrentUser().getUserLoggedIn();
        if (user == null) {
            showAlert("No user session found.");
            return;
        }

        String fxmlPath;
        switch (user.getRole()) {
            case ROLE_ADMIN:   fxmlPath = "/user/AdminDash.fxml"; break;
            case ROLE_TEACHER: fxmlPath = "/user/StDash.fxml";     break;
            case ROLE_STUDENT: fxmlPath = "/user/StDash.fxml";    break;
            case ROLE_PSY:     fxmlPath = "/user/PsyDash.fxml";   break;
            default:
                showAlert("Unknown role.");
                return;
        }

        loadScene(fxmlPath, event);
    }

    @FXML void goToSettings(ActionEvent event) {
        loadScene("/user/Settings.fxml", event);
    }

    @FXML void goToLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
            UserSession.CURRENT_USER.logout();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to logout.");
        }
    }

    @FXML void goToEditProfile(ActionEvent event) {
        User user = UserSession.getCurrentUser().getUserLoggedIn();
        if (user == null) {
            showAlert("User not logged in.");
            return;
        }

        String imgData = user.getProfileImage();
        boolean hasImage = imgData != null && !imgData.isBlank();
        if (!hasImage) {
            loadScene("/EditProfile.fxml", event);
            showAlert("You haven't uploaded a profile picture yet. Please add one now.");
        } else {
            loadScene("/UserProfile.fxml", event);
        }
    }

    // ===== Other Menu Items: You can implement or leave empty =====

    @FXML void goToCourses(ActionEvent e)        { /* to implement */ }
    @FXML void goToEvents(ActionEvent e)         { /* to implement */ }
    @FXML void goToPsychologists(ActionEvent e)  { /* to implement */ }
    @FXML void goToPublications(ActionEvent e)   { /* to implement */ }
    @FXML void goToRec(ActionEvent e)            { /* to implement */ }

    // ======================= Utilities ========================

    private void loadScene(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Navigation");
            stage.show();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load: " + fxmlPath);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    private void openEditProfile(ActionEvent event) {
        User user = UserSession.getCurrentUser().getUserLoggedIn();
        String fxml = (user.getProfileImage() == null || user.getProfileImage().isBlank())
                ? "/user/ProfileEdit.fxml" : "/user/Settings.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot load " + fxml).showAndWait();
        }
    }

}
