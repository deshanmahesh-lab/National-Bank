package com.deshan.app.banking.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Encryption {

    private Encryption() {
    }

    public static String encrypt(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(plainText.getBytes(), 0, plainText.length());
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}