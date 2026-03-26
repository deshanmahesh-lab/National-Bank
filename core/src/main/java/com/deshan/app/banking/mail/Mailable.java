package com.deshan.app.banking.mail;

import com.deshan.app.banking.provider.MailServiceProvider;
import com.deshan.app.banking.util.Env;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public abstract class Mailable implements Runnable {

    private final MailServiceProvider mailServiceProvider;

    public Mailable() {
        this.mailServiceProvider = MailServiceProvider.getInstance();
    }

    @Override
    public void run() {
        try {
            Session session = Session.getInstance(mailServiceProvider.getProperties(), mailServiceProvider.getAuthenticator());
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Env.get("app.email")));
            build(message);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void build(Message message) throws Exception;
}