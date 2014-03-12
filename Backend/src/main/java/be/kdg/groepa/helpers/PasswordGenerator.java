package be.kdg.groepa.helpers;

import java.util.Random;

/**
 * Created by Tim on 8/03/14.
 */
public class PasswordGenerator {
    private static final String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLLMNOPQRSTUVWXYZ";
    private static final int amnt = 16;
    private static Random random = new Random();

    public static String generateRandomPassword(){
        StringBuilder sb = new StringBuilder(amnt);
        // Ensure that the password contains an uppercase and lowercase password so it is valid
        sb.append("A");
        sb.append("a");
        for(int i = 0; i < amnt - 3; i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        // Ensure that the password contains a number as well
        sb.append("1");
        return sb.toString();
    }
}
