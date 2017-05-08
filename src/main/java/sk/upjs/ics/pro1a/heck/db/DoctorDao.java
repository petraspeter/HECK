package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import java.util.List;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @author raven
 */
public class DoctorDao extends AbstractDAO<Doctor> {

    public DoctorDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Doctor findById(Long id) {
        return get(id);
    }

    public Doctor create(Doctor doctor) {
        return this.persist(doctor);
    }

    public void update(Doctor doctor) {
        this.persist(doctor);
    }

    public List<Doctor> findAll() {
        return list(currentSession().createCriteria(Doctor.class));
    }

    public Doctor findByLogin(String login) {
        return uniqueResult(currentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("loginDoctor", login)));
    }

    public Doctor findByLoginAndPassword(String login, String password) {
        return uniqueResult(currentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("loginDoctor", login))
                .add(Restrictions.eq("passwordDoctor", password)));
    }

    
    public List<Doctor> findDoctorsBySpecializationId(Long id) {
        return list(currentSession().createCriteria(Doctor.class).setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id)));
    }
    
    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        return list(currentSession().createCriteria(Doctor.class).setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization)));
    }

    public List<Doctor> findDoctorsBySpecializationAndPostalCode(String specialization, String pcn) {
        return list(currentSession().createCriteria(Doctor.class).setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.eq("postalCodeDoctor", pcn)));
    }

    public List<Doctor> findDoctorsBySpecializationAndName(String specialization, String firstName, 
            String lastName) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("firstNameDoctor", firstName))/// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("lastNameDoctor", lastName)));/// isto tu ma byt LIKE??? nie EQ???
    }
    
    public List<Doctor> findDoctorsBySpecializationAndNameAndCity(String specialization, 
            String firstName, String lastName, String city) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("firstNameDoctor", firstName))/// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("lastNameDoctor", lastName))  /// isto tu ma byt LIKE??? nie EQ???      
                .add(Restrictions.like("cityDoctor", city)));/// isto tu ma byt LIKE??? nie EQ???
    }

    public List<Doctor> findDoctorsBySpecializationAndNameAndPostalCode(String specialization, 
            String firstName, String lastName, String pcn) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("firstNameDoctor", firstName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("lastNameDoctor", lastName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.eq("postalCodeDoctor", pcn)));
    }

    public List<Doctor> findDoctorsBySpecializationAndLastName(String specialization, String lastName) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("lastNameDoctor", lastName))); /// isto tu ma byt LIKE??? nie EQ???
    }

    public List<Doctor> findDoctorsBySpecializationAndLastNameAndPostalCode(String specialization,
            String lastName, String pcn) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("lastNameDoctor", lastName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.eq("postalCodeDoctor", pcn)));
    }
    
    public List<Doctor> findDoctorsBySpecializationAndLastNameAndCity(String specialization,
            String lastName, String city) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("lastNameDoctor", lastName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("cityDoctor", city)));
    }
    
    public List<Doctor> findDoctorsBySpecializationAndCity(String specialization, String city) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.specializationName", specialization))
                .add(Restrictions.like("cityDoctor", city))); /// isto tu ma byt LIKE??? nie EQ???
    }

    public Doctor findByEmail(String email) {
        return uniqueResult(currentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("emailDoctor", email)));
    }
}
