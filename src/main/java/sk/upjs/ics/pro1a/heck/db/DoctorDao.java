package sk.upjs.ics.pro1a.heck.db;

import sk.upjs.ics.pro1a.heck.core.Doctor;
import io.dropwizard.hibernate.AbstractDAO;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.auth.PasswordManager;

/**
 *
 * @author raven
 */
public class DoctorDao extends AbstractDAO<Doctor> {
    
    public DoctorDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Doctor> findAllDoctors() {
        return list(namedQuery("findAllDoctors"));
    }
    
    public Doctor findDoctorByLogin(String login) {
        return (Doctor) namedQuery("findDoctorByLogin").
                setParameter("login",login).uniqueResult();
    }
    
    public Doctor findDoctorById(Long id) {
        return (Doctor) namedQuery("findDoctorById").
                setParameter("id", id).uniqueResult();
    }
    
    public List<Doctor> findDoctorsBySpecialization(Long id) {
        List<Doctor> allDoctors = list(namedQuery("findAllDoctors"));
        List<Doctor> specificDoctors = new ArrayList<>();
        for (Doctor doctor : allDoctors) {
            if(doctor.getSpecializationDoctor().getId() == id) {
                specificDoctors.add(doctor);
            }
        }
        return allDoctors;
    }
    
    public Doctor registerDoctor(Doctor doctor) {
        PasswordManager passwordManager = new PasswordManager();
        Doctor doc = passwordManager.createDoctorPassword(doctor, doctor.getPasswordDoctor());
        super.currentSession().saveOrUpdate(doc);
        return doc;
    }
    
    
    public Doctor findUserByLoginAndPassword(String login, String password) {
        PasswordManager passwordManager = new PasswordManager();
        Doctor doctor = (Doctor) namedQuery("findUserByLogin").setParameter("login",login).uniqueResult();
        try {
            if(passwordManager.checkDoctorsLoginAndPassword(password, doctor)) {
                return doctor;
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}