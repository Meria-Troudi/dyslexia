package user.controllers;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import user.services.*;

public class ResetPasswordController {
    @FXML private Text emailError;
    @FXML private TextField ftCode;
    @FXML private TextField ftEmail;

    private final OTPHandler otpHandler = new OTPHandler();

    private String email;

    /** Called by ForgotPasswordController after loading. */
    public void setUserEmail(String email) {
        this.email = email;
    }

    @FXML
    public void confirmeOTP(javafx.event.ActionEvent ae) {
        emailError.setText("");
        String code = ftCode.getText().trim();
        if (code.isEmpty()) {
            emailError.setText("Please enter the code.");
            return;
        }

        if (!new OTPHandler().validateOtp(email, code)) {
            emailError.setText("Invalid or expired code.");
            return;
        }

        // OTP valid → go to ChangeForgotPassword.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ChangeForgotPassword.fxml"));
            Parent root = loader.load();
            ChangeForgotPasswordController ctrl = loader.getController();
            ctrl.setUserEmail(email);

            Stage stage = (Stage) ftCode.getScene().getWindow();
            stage.setTitle("Choose New Password");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            emailError.setText("Failed to load change‐password screen.");
        }
    }

    @FXML
    void goToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) ftCode.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert emailError != null : "fx:id=\"emailError\" was not injected: check your FXML file 'ResetPassword.fxml'.";
        assert ftCode != null : "fx:id=\"ftCode\" was not injected: check your FXML file 'ResetPassword.fxml'.";
        assert ftEmail != null : "fx:id=\"ftEmail\" was not injected: check your FXML file 'ResetPassword.fxml'.";
    }

    // Call this method when user submits email to trigger OTP
    public void sendResetPasswordOtp(String email) {
        try {
            if (otpHandler.sendResetPasswordOtp(email)) {
                emailError.setText("OTP sent successfully.");
            } else {
                emailError.setText("Failed to send OTP.");
            }
        } catch (SQLException e) {
            emailError.setText("Error sending OTP.");
            e.printStackTrace();
        }
    }
}
