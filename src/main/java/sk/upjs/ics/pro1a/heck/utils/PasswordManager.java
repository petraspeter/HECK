package sk.upjs.ics.pro1a.heck.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;


public class PasswordManager {
    
    public static String encryptPassword(String salt, String password) {
        if (chcekQualityOfPassword(password)) {
            String stringForHash = password + salt;
            String hash = "";
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(stringForHash.getBytes());
                byte[] byteHash = md.digest();
                hash = DatatypeConverter.printHexBinary(byteHash);
                
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Hash wasnt created!");
            }
            return hash;
        } else {
            System.err.println("Password is weak!");
            return null;
        }
    }
    
    private static Boolean chcekQualityOfPassword(String password) {
        boolean smallLetter = false;
        boolean capitalLetter = false;
        boolean number = false;
        boolean specialLetter = false;
        if (password.length() < 6) return false;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) > 47 && password.charAt(i) < 58) number = true;
            if (password.charAt(i) > 64 && password.charAt(i) < 91) capitalLetter = true;
            if (password.charAt(i) > 96 && password.charAt(i) < 123) smallLetter = true;
            if ((password.charAt(i) > 32 && password.charAt(i) < 48) ||
                    (password.charAt(i) > 57 && password.charAt(i) < 65) ||
                    (password.charAt(i) > 90 && password.charAt(i) < 97) ||
                    (password.charAt(i) > 122)) specialLetter = true;
        }
        return smallLetter && capitalLetter && number && specialLetter;
    }
    
}
