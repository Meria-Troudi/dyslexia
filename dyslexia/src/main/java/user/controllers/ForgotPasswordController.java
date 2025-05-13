package user.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import user.services.*;

import java.sql.SQLException;

public class ForgotPasswordController extends NavigationController{
    @FXML public TextField ftEmail;
    @FXML public Text emailError;

    private final UserServiceImp serviceUser = new UserServiceImp();
    private final OTPService otpService = new OTPService();
    private final OTPHandler otpHandler = new OTPHandler();


    public void findAccount(ActionEvent actionEvent) {
        emailError.setText("");
        String email = ftEmail.getText().trim();
        if (email.isEmpty()) {
            emailError.setText("Please enter an email address.");
            return;
        }
        if (!serviceUser.emailExists(email)) {
            emailError.setText("No account found for that email.");
            return;
        }

        // send SMS OTP
        try {
            boolean sent = otpHandler.sendResetPasswordOtp(email);
            if (!sent) {
                emailError.setText("Failed to send code. Try again later.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            emailError.setText("Error sending code. Try again later.");
            return;
        }

        // open ResetPassword.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ResetPassword.fxml"));
            Parent root = loader.load();
            ResetPasswordController ctrl = loader.getController();
            ctrl.setUserEmail(email);

            Stage stage = (Stage) ftEmail.getScene().getWindow();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            emailError.setText("Failed to load Reset screen.");
        }
    }

    // Method to navigate back to the login screen
    public void goToLogin(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ftEmail.getScene().getWindow();
            stage.setTitle("Login");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent p = loader.load();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        assert emailError != null : "fx:id=\"emailError\" was not injected: check your FXML file 'ForgotPassword.fxml'.";
        assert ftEmail != null : "fx:id=\"ftEmail\" was not injected: check your FXML file 'ForgotPassword.fxml'.";
    }
}
