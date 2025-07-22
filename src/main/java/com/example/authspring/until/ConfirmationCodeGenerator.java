package com.example.authspring.until;


import java.util.concurrent.ThreadLocalRandom;

public final class ConfirmationCodeGenerator {

    public static String generateNumericCode(int length) {
        int max = (int) Math.pow(10, length);
        int code = ThreadLocalRandom.current().nextInt(0, max);
        return String.format("%0" + length + "d", code);
    }

}
