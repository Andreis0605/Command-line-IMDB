package org.example;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+";

    private static final String PASSWORD_CHARS = CHAR_LOWER + CHAR_UPPER + DIGITS + SPECIAL_CHARS;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(25);
        for (int i = 0; i < 25; i++) {
            int randomIndex = secureRandom.nextInt(PASSWORD_CHARS.length());
            sb.append(PASSWORD_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
