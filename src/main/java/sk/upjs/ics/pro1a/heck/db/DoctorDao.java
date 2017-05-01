package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;

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
        return uniqueResult(namedQuery("findDoctorByLogin")
                .setParameter("login", login));
    }
    
    public Doctor findById(Long id) {
        return get(id);
    }
    
    public Doctor createDoctor(Doctor doctor) {
        return this.persist(doctor);
    }
    
    public Doctor findByLoginAndPassword(String login, String password) {
        return uniqueResult(namedQuery("findDoctorByLoginAndPassword")
                .setParameter("login", login)
                .setParameter("password", password));
    }
    
    public void update(Doctor doctor){
        this.persist(doctor);
    }
    
    public List<Doctor> findDoctorsBySpecialization(Long id) {
        return list(namedQuery("findAllDoctorsBySpecializationNativeSql")
                .setParameter("specialization", id));
    }
    
    public List<Doctor> findDoctorsBySpecializationAndPostalCode(Long id, String pcn) {
        return list(namedQuery("findAllDoctorsBySpecializationAndCityNativeSql")
                .setParameter("specialization", id)
                .setParameter("pcn", pcn));
    }
    
    public List<Doctor> findDoctorsBySpecializationAndName(Long id, String firstName, String lastName) {
        return list(namedQuery("findAllDoctorsBySpecializationAndNameNativeSql")
                .setParameter("specialization", id)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName));
    }
    
    public List<Doctor> findDoctorsBySpecializationAndNameAndPostalCode(Long id, String firstName,
            String lastName, String pcn) {
        return list(namedQuery("findAllDoctorsBySpecializationAndNameAndCityNativeSql")
                .setParameter("specialization", id)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("pcn", pcn));
    }
    public List<Doctor> findDoctorsBySpecializationAndLastName(Long id, String lastName) {
        return list(namedQuery("findAllDoctorsBySpecializationAndLastNameNativeSql")
                .setParameter("specialization", id)
                .setParameter("lastName", lastName));
    }
    
    public List<Doctor> findDoctorsBySpecializationAndLastNameAndPostalCode(Long id, String lastName,
            String pcn) {
        return list(namedQuery("findAllDoctorsBySpecializationAndLastNameAndCityNativeSql")
                .setParameter("specialization", id)
                .setParameter("lastName", lastName)
                .setParameter("pcn", pcn));
    }
    
    
    public Doctor findByEmail(String email) {
        return uniqueResult(namedQuery("findDoctorByEmail").setParameter("email", email));
    }
    
}
