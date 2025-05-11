package controllers;

import entities.Psy;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.session.UserSession;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.ResourceBundle;

public class PsyProfileController implements Initializable {

    @FXML private ImageView ivProfile;
    @FXML private Label lblName, lblEmail;
    @FXML private Text txtBio;
    @FXML private Button btnEditProfile, btnConsult, btnLogout;

    // New labels for full profile info
    @FXML private Label txtName, txtLast, txtEmail, txtPhone, txtBd, txtSchedule;

    private static final String DEFAULT_IMAGE = "/images/default_profile.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = UserSession.getCurrentUser().getUserLoggedIn();
        if (user instanceof Psy psy) {
            loadProfileImage(psy.getProfileImage());
            // Display top card
            lblName.setText(psy.getFirstName() + " " + psy.getLastName());
            lblEmail.setText(psy.getEmail());

            // Fill extended labels
            txtName.setText(psy.getFirstName());
            txtLast.setText(psy.getLastName());
            txtEmail.setText(psy.getEmail());
            txtPhone.setText(String.valueOf(psy.getPhoneNumber()));
            txtBd.setText(String.valueOf(psy.getBirthDay()));
            txtSchedule.setText(psy.getSchedule() != null ? psy.getSchedule() : "No schedule provided");

            txtBio.setText(psy.getBiography() != null ? psy.getBiography() : "No biography available.");
        }

        btnConsult.setOnAction(evt -> openConsultation());
    }

    private void loadProfileImage(String imagePathFromDB) {
        if (imagePathFromDB != null && !imagePathFromDB.trim().isEmpty()) {
            File imageFile = new File(imagePathFromDB);
            if (imageFile.exists()) {
                Image profileImage = new Image(imageFile.toURI().toString());
                ivProfile.setImage(profileImage);
                return;
            } else {
                System.err.println("Image file not found at: " + imagePathFromDB);
            }
        }

        // If image is null, empty, or file does not exist — use default FXML image
        System.out.println("Using default image defined in FXML.");
    }


    private void openConsultation() {
        /*try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Consultation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Consultation");
            stage.setScene(new Scene(root));
            stage.show(); // dashboard remains open
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


}
