package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;

/**
 * @author raven
 */
public class DoctorDao extends AbstractDAO<Doctor> {
    
    public DoctorDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
        
    public Doctor createDoctor(Doctor doctor) {
        return this.persist(doctor);
    }
    
    public void update(Doctor doctor){
        this.persist(doctor);
    }
    
}
