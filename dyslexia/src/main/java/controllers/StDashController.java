package controllers;

import entities.DyslexicStudent;
import entities.Teacher;
import entities.User;
import entities.Roles;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import services.UserServiceImp;
import services.session.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class StDashController implements Initializable {

    // --- FXML injected ---
    @FXML private Label nameuser;
    @FXML private ImageView bannerImage;      // if you need the banner later

    @FXML private Pane studentInfoPane;
    @FXML private Pane teacherInfoPane;

    // Student fields
    @FXML private Label DyslexiaTypecFields;
    @FXML private Label SeverityLevelFields;
    @FXML private Label specificNeedsFields;

    // Teacher fields
    @FXML private Label subjectAreaLabel;
    @FXML private Label availabilityLabel;

    @FXML private Label courseLabel;          // example dynamic course card
    @FXML private Hyperlink viewcour;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserSession session = UserSession.getCurrentUser();
        if (session == null || session.getUserLoggedIn() == null) {
            showAlert("Error", "User session not found.");
            return;
        }
        User user = session.getUserLoggedIn();

        // 1) Common fields
        nameuser.setText(user.getFirstName() + " " + user.getLastName());

        // hide both panes by default
        studentInfoPane.setVisible(false);
        studentInfoPane.setManaged(false);
        teacherInfoPane.setVisible(false);
        teacherInfoPane.setManaged(false);

        // 2) Branch on actual type
        if (user instanceof Teacher t) {
            teacherInfoPane.setVisible(true);
            teacherInfoPane.setManaged(true);
            subjectAreaLabel.setText(t.getSubjectArea());
            availabilityLabel.setText(t.getAvailability());

        } else if (user instanceof DyslexicStudent ds) {
            studentInfoPane.setVisible(true);
            studentInfoPane.setManaged(true);
            DyslexiaTypecFields.setText(
                    ds.getDyslexiaType() != null
                            ? ds.getDyslexiaType().toString()
                            : "N/A"
            );
            SeverityLevelFields.setText(
                    ds.getSeverityLevel() != null
                            ? ds.getSeverityLevel().toString()
                            : "N/A"
            );
            specificNeedsFields.setText(
                    ds.getSpecificNeeds() != null
                            ? ds.getSpecificNeeds()
                            : "N/A"
            );

        } else {
            // fallback for admin, psy, or unexpected
            showAlert("Role Error", "Unsupported dashboard for role: " + user.getRole());
        }
        loadBannerImage();
    }

    // Optional: if you ever want to load a banner image
    private void loadBannerImage() {
         try {
           var img = new Image(getClass().getResourceAsStream("/images/3d-rendering-student-character.png"));
           bannerImage.setImage(img);
         } catch (Exception e) {
           System.err.println("Failed to load banner image: " + e.getMessage());
         }
    }

    private void showAlert(String title, String msg) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
