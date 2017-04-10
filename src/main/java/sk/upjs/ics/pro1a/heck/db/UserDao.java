package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.auth.PasswordManager;
import sk.upjs.ics.pro1a.heck.core.User;

/**
 *
 * @author raven
 */
public class UserDao extends AbstractDAO<User>{
    
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<User> findAllUsers() {
        return list(namedQuery("findAllUsers"));
    }
    
    public User findUserByLogin(String login) {
        return (User) namedQuery("findUserByLogin").
                setParameter("login",login).uniqueResult();
    }
    
    public User findUserById(Long id) {
        return (User) namedQuery("findUserById").
                setParameter("id", id).uniqueResult();
    }
    
    public User registerUser(User user, String password) {
        PasswordManager passwordManager = new PasswordManager();
        User newUser = passwordManager.createUserPassword(user, password);
        super.currentSession().saveOrUpdate(newUser);
        return newUser;
    }
    
    public User findUserByLoginAndPassword(String login, String password) {
        PasswordManager passwordManager = new PasswordManager();
        User user = (User) namedQuery("findUserByLogin").setParameter("login",login).uniqueResult();
        try {
            if(passwordManager.checkUsersLoginAndPassword(password, user)) {
                return user;
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
