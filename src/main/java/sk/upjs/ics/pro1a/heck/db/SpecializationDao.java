package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;

import java.util.List;

/**
 * @author raven
 */
public class SpecializationDao extends AbstractDAO<Specialization> {

    public SpecializationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Specialization> findAll() {
        return list(namedQuery("findAllSpecializations"));
    }

    public Specialization findById(Long id) {     
        return uniqueResult(namedQuery("findSpecializationById").setParameter("id", id));        
    }

}
