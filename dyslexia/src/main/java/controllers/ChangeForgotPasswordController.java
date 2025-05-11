package controllers;

import java.sql.SQLException;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.*;

public class ChangeForgotPasswordController {
    @FXML private Text newError;
    @FXML private Text confirmError;
    @FXML private PasswordField fpConfirm;
    @FXML private PasswordField fpNew;

    private final UserDao userDao = new UserDao();
    private String email;

    public void setUserEmail(String email) {
        this.email = email;
    }

    private void clearErrors() {
        newError.setText("");
        confirmError.setText("");
        fpNew.getStyleClass().remove("error");
        fpConfirm.getStyleClass().remove("error");
    }

    private boolean isValidPassword() {
        boolean ok = true;
        newError.setText("");
        confirmError.setText("");

        if (fpNew.getText().isEmpty()) {
            newError.setText("New password is required.");
            ok = false;
        } else if (fpNew.getText().length() < 6) {
            newError.setText("Must be ≥ 6 characters.");
            ok = false;
        }
        if (!fpConfirm.getText().equals(fpNew.getText())) {
            confirmError.setText("Passwords do not match.");
            ok = false;
        }
        return ok;
    }

    @FXML
    void submitNewPassword() {
        if (!isValidPassword()) return;

        try {
            UserDao dao = new UserDao();
            String hashed = BCrypt.hashpw(fpNew.getText(), BCrypt.gensalt());
            dao.changePasswordByEmail(email, hashed);    // implement this in UserDao
            // back to login
            Stage stage = (Stage) fpNew.getScene().getWindow();
            Parent login = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            stage.setScene(new Scene(login));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
            newError.setText("Error updating password.");
        }

    }
    @FXML
    void goToLogin() {
        try {
            Stage stage = (Stage) fpNew.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
            newError.setText("Error loading login page.");
        }
    }
    @FXML
    void initialize() {
        assert newError != null : "fx:id=\"newError\" not injected: check FXML.";
        assert confirmError != null : "fx:id=\"confirmError\" not injected: check FXML.";
        assert fpConfirm != null : "fx:id=\"fpConfirm\" not injected: check FXML.";
        assert fpNew != null : "fx:id=\"fpNew\" not injected: check FXML.";
    }
}
