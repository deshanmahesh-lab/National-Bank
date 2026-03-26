package com.deshan.app.banking.provider;

import com.deshan.app.banking.mail.Mailable;
import com.deshan.app.banking.util.Env;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailServiceProvider {

    private static MailServiceProvider instance;
    private final Properties properties = new Properties();
    private Authenticator authenticator;
    private ExecutorService executor;

    private MailServiceProvider() {
        properties.put("mail.smtp.host", Env.get("mailtrap.host"));
        properties.put("mail.smtp.port", Env.get("mailtrap.port"));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", Env.get("mailtrap.host"));
    }

    public static synchronized MailServiceProvider getInstance() {
        if (instance == null) {
            instance = new MailServiceProvider();
        }
        return instance;
    }

    public void start() {
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Env.get("mailtrap.username"), Env.get("mailtrap.password"));
            }
        };
        executor = Executors.newCachedThreadPool();
    }

    public void sendMail(Mailable mailable) {
        if (executor != null && !executor.isShutdown()) {
            executor.submit(mailable);
        }
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }
}