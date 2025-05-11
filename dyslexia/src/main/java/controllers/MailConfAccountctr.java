package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UserServiceImp;

public class MailConfAccountctr {

    private UserServiceImp userService = new UserServiceImp();

    @FXML
    private TextField codeField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleConfirm() {
        String token = codeField.getText();

        try {
            boolean success = userService.confirmAccount(token);
            if (success) {
                messageLabel.setText("✅ Account confirmed successfully!");
            } else {
                messageLabel.setText("❌ Invalid token or account already confirmed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("❌ Server error.");
        }
    }
}
