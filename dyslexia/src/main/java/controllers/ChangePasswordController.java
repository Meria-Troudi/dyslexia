package controllers;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.mindrot.jbcrypt.BCrypt;

import services.UserDao;
import services.session.UserSession;
import entities.User;

public class ChangePasswordController extends NavigationController implements Initializable {
    @FXML private ImageView passwordImageView;

    @FXML private Text confirmError, newError, currError;
    @FXML private PasswordField fpNew, fpConfirm, fpCurrent;

    UserDao userDao = new UserDao();
    private final Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
    private static final String PREF_REMEMBER = "remember_me";
    private static final String PREF_PASSWORD = "remember_password";
    private void clearError() {
        confirmError.setText("");
        newError.setText("");
        currError.setText("");
        removeErrorStyles();
    }
    private void removeErrorStyles() {
        fpConfirm.getStyleClass().remove("error");
        fpNew.getStyleClass().remove("error");
        fpCurrent.getStyleClass().remove("error");
    }

    private boolean validateFields() {
        clearError();
        boolean valid = true;
        String currentInput = fpCurrent.getText().trim();
        String newPwd       = fpNew.getText();
        String confirmPwd   = fpConfirm.getText();
        // 1) Verify current password against stored hash
        String email = UserSession.CURRENT_USER.getUserLoggedIn().getEmail();
        String storedHash;
        try {
            storedHash = userDao.getHashedPasswordByUsername(email);
        } catch (SQLException e) {
            currError.setText("Unable to verify current password.");
            fpCurrent.getStyleClass().add("error");
            return false;
        }
        if (storedHash == null || !BCrypt.checkpw(currentInput, storedHash)) {
            currError.setText("Incorrect current password.");
            fpCurrent.getStyleClass().add("error");
            valid = false;
        }
        // 2) New password length
        if (newPwd == null || newPwd.length() < 8) {
            newError.setText("New password must be at least 8 characters.");
            fpNew.getStyleClass().add("error");
            valid = false;
        }
        // 3) Confirmation match
        if (!newPwd.equals(confirmPwd)) {
            confirmError.setText("Passwords do not match.");
            fpConfirm.getStyleClass().add("error");
            valid = false;
        }
        return valid;
    }
    @FXML
    void saveChanges(ActionEvent event) {
        clearError();
        if (!validateFields()) {
            return;
        }
        String newPwd = fpNew.getText();
        User user    = new User();
        user.setIdUser(UserSession.CURRENT_USER.getUserLoggedIn().getIdUser());
        user.setPassword(newPwd);
        try {
            userDao.changePassword(user);
            UserSession.CURRENT_USER.getUserLoggedIn().setPassword(newPwd);
            // update “remember me” prefs if set
            Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
            if (prefs.getBoolean(PREF_REMEMBER, false)) {
                prefs.put(PREF_PASSWORD, newPwd);
                try {
                    prefs.flush();
                } catch (BackingStoreException x) {
                    x.printStackTrace();
                }
            }
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Password Changed");
            info.setContentText("Your password has been updated successfully.");
            info.show();
            goToOverview(event);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Update Failed");
            err.setContentText("An error occurred while updating your password.");
            err.show();
        }
    }
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

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();
        User u = UserSession.getCurrentUser().getUserLoggedIn();
        if (u.getProfileImage() != null) {
            byte[] bytes = Base64.getDecoder().decode(u.getProfileImage());
            passwordImageView.setImage(new Image(new ByteArrayInputStream(bytes)));
        }
        assert confirmError != null : "fx:id=\"confirmError\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert currError != null : "fx:id=\"currError\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert fpConfirm != null : "fx:id=\"fpConfirm\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert fpCurrent != null : "fx:id=\"fpCurrent\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert fpNew != null : "fx:id=\"fpNew\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert newError != null : "fx:id=\"newError\" was not injected: check your FXML file 'ChangePassword.fxml'.";
    }


}
