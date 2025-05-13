package user.controllers;

import user.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import user.services.UserService;
import user.services.UserServiceImp;
import user.services.session.UserSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsController extends NavigationController implements Initializable {

    @FXML private ImageView overviewImageView;
    @FXML private Text txtName, txtLast, txtEmail, txtPhone, txtbd;
    @FXML public Text type, needs, level, subject, availability;
    @FXML private VBox studentFields, teacherFields, psyFields;
    @FXML private Text txtSchedule, txtBio;

    private final UserService userService = new UserServiceImp();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();
        User user = UserSession.getCurrentUser().getUserLoggedIn();

        if (user != null) {
            // Profile Image
            if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                byte[] bytes = Base64.getDecoder().decode(user.getProfileImage());
                overviewImageView.setImage(new Image(new ByteArrayInputStream(bytes)));
            }
            // Common fields
            txtName.setText(user.getFirstName());
            txtLast.setText(user.getLastName());
            txtEmail.setText(user.getEmail());
            txtPhone.setText(String.valueOf(user.getPhoneNumber()));
            txtbd.setText(String.valueOf(user.getBirthDay()));

            // Hide all
            studentFields.setVisible(false);
            teacherFields.setVisible(false);
            psyFields.setVisible(false);

            // Role-specific
            if (user instanceof DyslexicStudent) {
                studentFields.setVisible(true);
                DyslexicStudent ds = (DyslexicStudent) user;
                type.setText(ds.getDyslexiaType().toString());
                needs.setText(ds.getSpecificNeeds());
                level.setText(ds.getSeverityLevel().toString());

            } else if (user instanceof Teacher) {
                teacherFields.setVisible(true);
                Teacher t = (Teacher) user;
                availability.setText(t.getAvailability());
                subject.setText(t.getSubjectArea());

            } else if (user instanceof Psy) {
                psyFields.setVisible(true);
                Psy p = (Psy) user;
                txtSchedule.setText(p.getSchedule());
                txtBio.setText(p.getBiography());

            }
        }
    }

    @FXML public void goToOverview(ActionEvent e)      { super.goToOverview(e); }
    @FXML public void goToEditProfile(ActionEvent e)   { super.goToEditProfile(e); }
    @FXML public void goToChangePassword(ActionEvent e){ super.goToChangePassword(e); }

    @FXML
    public void deleteAccount(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Account");
        confirm.setHeaderText("Permanently delete your account?");
        confirm.setContentText("This cannot be undone.");

        Optional<ButtonType> choice = confirm.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            User currentUser = UserSession.getCurrentUser().getUserLoggedIn();
            try {
                userService.removeUSER(currentUser.getIdUser());
                UserSession.logout();
                navigateToLogin(event);
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Could not delete your account. Please try again later.")
                        .showAndWait();
            }
        }
    }

    private void navigateToLogin(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(
                    getClass().getResource("/path/to/login.fxml"));
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage)((Node)event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Unable to load login screen.")
                    .showAndWait();
        }
    }
}