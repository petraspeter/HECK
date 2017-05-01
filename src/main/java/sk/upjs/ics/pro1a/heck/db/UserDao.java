package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.User;

import java.util.List;

/**
 * @author raven
 */
public class UserDao extends AbstractDAO<User> {
    
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<User> findAll() {
        return list(namedQuery("findAllUsers"));
    }
    
    public User findByLogin(String login) {
        return uniqueResult(namedQuery("findUserByLogin").setParameter("login", login));
    }
    
    public User findById(Long id) {
        return get(id);
    }
    
    public User createUser(User user) {
        return this.persist(user);
    }
    
    public User findByLoginAndPassword(String login, String password) {
        return uniqueResult(namedQuery("findUserByLoginAndPassword").setParameter("login", login).
                setParameter("password", password));
    }
    
    public void update(User user){
        this.persist(user);
    }
    
}
