package sk.upjs.ics.pro1a.heck.repositories;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.repositories.model.Doctor;

import java.util.List;

/**
 * @author raven
 */
public class DoctorDao extends AbstractDAO<Doctor> {

    public DoctorDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Doctor> findAll() {
        return list(namedQuery("findAllDoctors"));
    }

    public Doctor findByLogin(String login) {
        return uniqueResult(namedQuery("findDoctorByLogin").setParameter("login", login));
    }

    public Doctor findById(Long id) {
        return get(id);
    }

    public Doctor createDoctor(Doctor doctor) {
        return this.persist(doctor);
    }

    public Doctor findByLoginAndPassword(String login, String password) {
        return uniqueResult(namedQuery("findDoctorByLoginAndPassword").setParameter("login", login).setParameter("password", password));

    }
    
    public void update(Doctor doctor){
        this.persist(doctor);
    }
}
