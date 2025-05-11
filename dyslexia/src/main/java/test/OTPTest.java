package test;

import java.sql.SQLException;
import java.util.Scanner;
import services.OTPHandler;

public class OTPTest {
    public static void main(String[] args) throws SQLException {
        OTPHandler handler = new OTPHandler();
        Scanner scan = new Scanner(System.in);

        System.out.print("Email for SIGNUP OTP: ");
        String signupEmail = scan.nextLine().trim();
        if (handler.sendSignupOtp(signupEmail)) {
            System.out.print("Enter EMAIL code: ");
            String emailCode = scan.nextLine().trim();
            System.out.println("✅ Email OTP valid? " +
                    handler.validateOtp(signupEmail, emailCode));
        } else {
            System.err.println("❌ Failed to send signup OTP.");
        }

        System.out.print("\nEmail for RESET‑PASSWORD SMS: ");
        String resetEmail = scan.nextLine().trim();
        if (handler.sendResetPasswordOtp(resetEmail)) {
            System.out.print("Enter SMS code: ");
            String smsCode = scan.nextLine().trim();
            System.out.println("✅ SMS OTP valid? " +
                    handler.validateOtp(resetEmail, smsCode));
        } else {
            System.err.println("❌ Failed to send reset SMS OTP.");
        }

        scan.close();
    }
}