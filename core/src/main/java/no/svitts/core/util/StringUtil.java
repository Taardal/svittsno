package no.svitts.core.util;

import java.util.Random;

public class StringUtil {

    private StringUtil() {
    }

    public static String getRandomString(int length) {
        if (length > 0) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            Random random = new Random();
            String string = "";
            for (int i = 0; i < length; i++) {
                int j = random.nextInt(alphabet.length());
                string += alphabet.charAt(j);
            }
            return string;
        } else {
            throw new IllegalArgumentException("Could not get random string. Length of requested string must be greater than 0");
        }
    }
}
