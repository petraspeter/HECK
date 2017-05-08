package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.User;

import java.util.List;
import org.hibernate.criterion.Restrictions;

/**
 * @author raven
 */
public class UserDao extends AbstractDAO<User> {
    
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public User findById(Long id) {
        return get(id);
    }
    
    public User create(User user) {
        return this.persist(user);
    }
    
    public void update(User user){
        this.persist(user);
    }
    
    public User findByLoginAndPassword(String login, String password) {
        return uniqueResult(currentSession().createCriteria(User.class)
                .add(Restrictions.eq("loginUser", login))
                .add(Restrictions.eq("passwordUser", password)));
    }
    
    public User findByLogin(String login) {
        return uniqueResult(currentSession().createCriteria(User.class)
                .add(Restrictions.eq("loginUser", login)));
    }
    
    public List<User> findAll() {
        return list(currentSession().createCriteria(User.class));
    }
}
