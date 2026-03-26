package com.deshan.app.banking.mail;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import java.util.Base64;

public class ActivationMail extends Mailable {

    private final String recipientEmail;
    private final String verificationCode;

    public ActivationMail(String recipientEmail, String verificationCode) {
        this.recipientEmail = recipientEmail;
        this.verificationCode = verificationCode;
    }

    @Override
    public void build(Message message) throws Exception {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("National Bank - Activate Your Account and Set Your Password");

        String encodedEmail = Base64.getEncoder().encodeToString(recipientEmail.getBytes());
        String activationLink = "http://localhost:8080/national-bank/auth/activate_account.jsp?id=" + encodedEmail + "&vc=" + verificationCode;

        String emailContent = "<h2>Welcome to National Bank!</h2>"
                + "<p>Your account has been created by an administrator. To complete your registration and activate your account, please click the link below to set your password:</p>"
                + "<p><a href='" + activationLink + "' style='padding: 12px 20px; background-color: #005A9E; color: white; text-decoration: none; border-radius: 5px; font-size: 16px;'>Set Password & Activate</a></p>"
                + "<p>If you did not request this, please disregard this email.</p>";

        message.setContent(emailContent, "text/html; charset=utf-8");
    }
}