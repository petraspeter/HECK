package sk.upjs.ics.pro1a.heck.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.xml.bind.DatatypeConverter;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.User;

/**
 *
 * @author raven
 */
public class PasswordManager {
    
    public User createUserPassword(User user, String password) {
        if (chcekQualityOfPassword(password)) {
            /*
            * generujeme sol hashujeme heslo
            */
            String salt = new BigInteger (130, new SecureRandom ()).toString(32);
            String preHash = password + salt;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(preHash.getBytes());
                byte[] byteHash = md.digest();
                String hash = DatatypeConverter.printHexBinary(byteHash);
                user.setSaltUser(salt);
                user.setPasswordUser(hash);
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Hash wasnt created!");
            }
            return user;
        } else {
            System.err.println("Password is weak!");
            return null;
        }
    }
    
    public Doctor createDoctorPassword(Doctor doctor, String password) {
        if (chcekQualityOfPassword(password)) {
            /*
            * generujeme sol hashujeme heslo
            */
            String salt = new BigInteger (130, new SecureRandom ()).toString(32);
            String stringForHas = password + salt;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(stringForHas.getBytes());
                byte[] byteHash = md.digest();
                String hash = DatatypeConverter.printHexBinary(byteHash);
                doctor.setSaltDoctor(salt);
                doctor.setPasswordDoctor(hash);
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Hash wasnt created!");
            }
            return doctor;
        } else {
            System.err.println("Password is weak!");
            return null;
        }
    }
    
    public boolean checkDoctorsLoginAndPassword(String password, Doctor doctor) throws NoSuchAlgorithmException  {
        password = password + doctor.getSaltDoctor();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((password).getBytes());
        byte[] byteHash = md.digest();
        return DatatypeConverter.printHexBinary(byteHash).equals(doctor.getPasswordDoctor());
    }
    
    public boolean checkUsersLoginAndPassword(String password, User user) throws NoSuchAlgorithmException  {
        System.out.println(password);
        password = password + user.getSaltUser();
        System.out.println(password);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((password).getBytes());
        byte[] byteHash = md.digest();
        return DatatypeConverter.printHexBinary(byteHash).equals(user.getPasswordUser());
    }
    
    private Boolean chcekQualityOfPassword(String password) {
        boolean smallLetter = false;
        boolean capitalLetter = false;
        boolean number = false;
        boolean specialLetter = false;
        if(password.length() < 6) return false;
        for (int i = 0; i < password.length(); i++) {
            if(password.charAt(i) > 47 && password.charAt(i) < 58) number = true;
            if(password.charAt(i) > 64 && password.charAt(i) < 91) capitalLetter = true;
            if(password.charAt(i) > 96 && password.charAt(i) < 123) smallLetter = true;
            if((password.charAt(i) > 32 && password.charAt(i) < 48) ||
                    (password.charAt(i) > 57 && password.charAt(i) < 65) ||
                    (password.charAt(i) > 90 && password.charAt(i) < 97) ||
                    (password.charAt(i) > 122)) specialLetter = true;
        }
        return smallLetter && capitalLetter && number && specialLetter;
    }

    public PasswordManager() {
    }
    
    
        
    
    
    
}
