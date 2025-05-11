package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;

public class SmsService {
    private static final String ACCOUNT_SID   = "AC3a2077b1fae1532aec6f15b5a245c026";
    private static final String AUTH_TOKEN    = "c4214250e82faaccd9fc29987de44ae9";
    private static final String TWILIO_NUMBER = "+19787986076";
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Send an SMS to a given phone number.
     * @param toE164 The recipient phone number in E.164 format (e.g., +21612345678).
     * @param body The message body.
     * @return boolean indicating success or failure of sending the SMS.
     */
    public static boolean sendSms(String toE164, String body) {
        try {
            Message msg = Message.creator(
                    new PhoneNumber(toE164),
                    new PhoneNumber(TWILIO_NUMBER),
                    body
            ).create();
            System.out.println("✅ SMS SID: " + msg.getSid());
            return true;
        } catch (ApiException e) {
            System.err.println("❌ SMS failed: " + e.getMessage());
            return false;
        }
    }
}
