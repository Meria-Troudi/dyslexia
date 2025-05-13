package user.services;

import java.sql.SQLException;
import user.services.*;
import user.entities.User;

public class OTPHandler {
    private final OTPService otpService = new OTPService();
    private final UserDao     userDao    = new UserDao();

    /** Send an 8‑char email OTP (e.g. for signup). */
    public boolean sendSignupOtp(String email) {
        String code = otpService.generateOTP(email, 8);
        return otpService.sendOTP(email, code);
    }

    /**
     * Send a 6-digit SMS OTP for "forgot password" to the user's phone.
     * @param email The user's email associated with the phone number.
     * @return true if the OTP is sent successfully, false otherwise.
     */
    public boolean sendResetPasswordOtp(String email) throws SQLException {
        // 1) generate & store OTP
        String code = otpService.generateOTP(email, 6);

        // 2) look up user to get phone
        User user = userDao.findByEmail(email);
        if (user == null || user.getPhoneNumber() <= 0) {
            return false;
        }

        // 3) format E.164 (here we hard‑code country code +216; adjust as needed)
        String e164 = "+216" + user.getPhoneNumber();

        // 4) build message and send
        String body = "Your reset code: " + code + "\nIt expires in 1 minute.";
        return SmsService.sendSms(e164, body);
    }

    /** Validate that the provided code matches and hasn’t expired. */
    public boolean validateOtp(String email, String code) {
        return OTPService.validateOTP(email, code);
    }
}
