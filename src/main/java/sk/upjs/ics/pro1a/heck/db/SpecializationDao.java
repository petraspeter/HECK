package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;

import java.util.List;
import org.hibernate.criterion.Restrictions;

/**
 * @author raven
 */
public class SpecializationDao extends AbstractDAO<Specialization> {

    public SpecializationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Specialization> findAll() {
        return list(currentSession().createCriteria(Specialization.class));
    }

    public Specialization findById(Long id) {     
        return uniqueResult(currentSession().createCriteria(Specialization.class)
                .add(Restrictions.eq("idDoctor", id)));        
    }

}
