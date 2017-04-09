package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.core.Specialization;

/**
 *
 * @author raven
 */
public class SpecializationDao  extends AbstractDAO<Specialization> {
    
    public SpecializationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Specialization> findAllSpecializations() {
        return list(namedQuery("findAllSpecializations"));
    }
    
    public Specialization findSpecializationById(Long id) {
        return (Specialization) namedQuery("findSpecializationById").
                setParameter("id", id).uniqueResult();
    }
          
}
