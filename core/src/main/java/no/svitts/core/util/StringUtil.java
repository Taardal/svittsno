package no.svitts.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class StringUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    private StringUtil() {
    }

    public static String getRandomString(int length) {
        String string = "";
        if (length > 0) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int j = random.nextInt(alphabet.length());
                string += alphabet.charAt(j);
            }
            return string;
        } else {
            LOGGER.warn("Could not get random string because requested length was 0 (zero) or negative.");
        }
        return string;
    }
}
