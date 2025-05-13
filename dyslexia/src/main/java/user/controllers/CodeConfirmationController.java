package user.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import user.services.OTPService;
import user.services.UserDao;

public class CodeConfirmationController {

    @FXML private TextField codeField;
    @FXML private Label messageLabel;
    @FXML private Label instructionLabel;
    @FXML private Button confirmButton;

    // The email for which we're verifying the code
    private String userEmail;

    /**
     * Called by the loader after the FXML is loaded.
     */
    @FXML
    private void initialize() {
        messageLabel.setText("");
    }

    /**
     * Inject the email before showing the dialog.
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    /**
     * Fired when the user clicks "Validate".
     */
    @FXML
    private void handleConfirm() {
        String code = codeField.getText().trim();

        if (code.isEmpty()) {
            messageLabel.setText("⚠ Please enter the confirmation code.");
            return;
        }

        // 1) Validate OTP against the service
        boolean valid = OTPService.validateOTP(userEmail, code);

        if (!valid) {
            messageLabel.setText("❌ Invalid or expired code.");
            return;
        }

        // 2) Token is valid → enable the user in the database
        try {
            boolean enabled = new UserDao().enableUser(userEmail);
            if (enabled) {
                showSuccessAlert();
                closeWindow();
            } else {
                messageLabel.setText("❌ Could not activate account. Try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("❌ Server error. Please try later.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) codeField.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Activation Successful");
        alert.setHeaderText(null);
        alert.setContentText("✅ Your account has been activated!");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
