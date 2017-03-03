package sk.upjs.ics.pro1a.databaseDao;

import sk.upjs.ics.pro1a.dao.User.UserDaoAccount;
import sk.upjs.ics.pro1a.entities.User;

/**
 *
 * @author raven
 */
public class UserDatabaseDaoAccount implements UserDaoAccount{
    
    /**
     * metoda zaregistruje noveho pouzivatela
     * @param user novy pouzivatel, ktoreho registrujeme
     */
    @Override
    public void registerNewUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda ktora upravi informacie o pouzivatelovi
     * @param user pouzivatel, ktoreho nastavenia menime
     */
    @Override
    public void changeUserSetting(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda zmeni emailovu adresu pouzivatela
     * @param user pouzivatel, ktoreho email zmenime
     * @param newEmail nova emailova adresa pouzivatela
     */
    @Override
    public void changeUserEmail(User user, String newEmail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda overi ci je zadana emailova adresa pouzivana alebo nie
     * @param email emailova adresa, ktoru kontrolujeme
     * @return ci sa uz zadana emailova adresa nachadza v databaze
     */
    @Override
    public boolean checkEmailAddress(String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
