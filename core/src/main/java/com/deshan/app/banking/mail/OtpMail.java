package com.deshan.app.banking.mail;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;

public class OtpMail extends Mailable {

    private final String recipientEmail;
    private final String otp;

    public OtpMail(String recipientEmail, String otp) {
        this.recipientEmail = recipientEmail;
        this.otp = otp;
    }

    @Override
    public void build(Message message) throws Exception {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("National Bank - Your One-Time Password (OTP)");

        String emailContent = "<h2>National Bank Transaction Verification</h2>"
                + "<p>Please use the following One-Time Password (OTP) to complete your transaction. Do not share this OTP with anyone.</p>"
                + "<p style='font-size: 24px; font-weight: bold; letter-spacing: 5px; margin: 20px 0;'>" + otp + "</p>"
                + "<p>This OTP is valid for a short period of time.</p>";

        message.setContent(emailContent, "text/html; charset=utf-8");
    }
}