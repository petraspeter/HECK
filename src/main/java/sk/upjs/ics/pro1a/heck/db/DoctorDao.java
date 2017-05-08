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

    public List<Doctor> findDoctorsBySpecializationId(Long specializationId) {
        return list(currentSession().createCriteria(Doctor.class).setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId)));
    }

    public List<Doctor> findDoctorsBySpecializationIdAndPostalCode(Long specializationId, String pcn) {
        return list(currentSession().createCriteria(Doctor.class).setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.eq("postalCodeDoctor", pcn)));
    }

    public List<Doctor> findDoctorsBySpecializationIdAndName(Long specializationId, String firstName, 
            String lastName) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("firstNameDoctor", firstName))/// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("lastNameDoctor", lastName)));/// isto tu ma byt LIKE??? nie EQ???
    }
    
    public List<Doctor> findDoctorsBySpecializationIdAndNameAndCity(Long specializationId, 
            String firstName, String lastName, String city) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("firstNameDoctor", firstName))/// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("lastNameDoctor", lastName))  /// isto tu ma byt LIKE??? nie EQ???      
                .add(Restrictions.like("cityDoctor", city)));/// isto tu ma byt LIKE??? nie EQ???
    }

    public List<Doctor> findDoctorsBySpecializationIdAndNameAndPostalCode(Long specializationId, 
            String firstName, String lastName, String pcn) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("firstNameDoctor", firstName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("lastNameDoctor", lastName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.eq("postalCodeDoctor", pcn)));
    }

    public List<Doctor> findDoctorsBySpecializationIdAndLastName(Long specializationId, String lastName) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("lastNameDoctor", lastName))); /// isto tu ma byt LIKE??? nie EQ???
    }

    public List<Doctor> findDoctorsBySpecializationIdAndLastNameAndPostalCode(Long specializationId,
            String lastName, String pcn) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("lastNameDoctor", lastName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.eq("postalCodeDoctor", pcn)));
    }
    
    public List<Doctor> findDoctorsBySpecializationIdAndLastNameAndCity(Long specializationId,
            String lastName, String city) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("lastNameDoctor", lastName)) /// isto tu ma byt LIKE??? nie EQ???
                .add(Restrictions.like("cityDoctor", city)));
    }
    
    public List<Doctor> findDoctorsBySpecializationIdAndCity(Long specializationId, String city) {
        return list(currentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", specializationId))
                .add(Restrictions.like("cityDoctor", city))); /// isto tu ma byt LIKE??? nie EQ???
    }

    public Doctor findByEmail(String email) {
        return uniqueResult(currentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("emailDoctor", email)));
    }
}
