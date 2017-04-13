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
    
    private final PasswordManager passwordManager = new PasswordManager();
    
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
    
    public Doctor registerDoctor(Doctor doctor, String password) throws Exception {
        Doctor doc = passwordManager.createDoctorPassword(doctor, password);
        try {
            super.currentSession().saveOrUpdate(doc);
            return doc;
        } catch(Exception e) {
            throw new Exception("Docotr can not be created!");
        }
    }
    
    
    public Doctor findDoctorByLoginAndPassword(String login, String password) {
        Doctor doctor = (Doctor) namedQuery("findDoctorByLogin").setParameter("login",login).
                uniqueResult();
        try {
            if(passwordManager.checkDoctorsLoginAndPassword(password, doctor)) {
                return doctor;
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DoctorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
