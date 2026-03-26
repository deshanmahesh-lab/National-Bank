package com.deshan.app.banking.util;

import java.util.Random;

public final class OtpUtil {

    private OtpUtil() {
    }

    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}