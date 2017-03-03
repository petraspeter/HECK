package sk.upjs.ics.pro1a.dao.User;

import sk.upjs.ics.pro1a.entities.User;

/**
 *
 * @author raven
 */
public interface UserDaoAccount {
    
    public void registerNewUser(User user);
    
    public void changeUserSetting(User user);
  
    public void changeUserEmail(User user, String newEmail);
  
    public boolean checkEmailAddress(String email);
    
}
