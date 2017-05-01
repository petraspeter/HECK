package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.Appointment;

/**
 *
 * @author Raven
 */
public class AppointmentDao extends AbstractDAO<Appointment>{
    
    public AppointmentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Appointment> findAll() {
        return list(namedQuery("findAllAppointments"));
    }
    
    public List<Appointment> findByDoctorId(Long id) {
        return list(namedQuery("findAppointmentsByDoctorIdNativeSql")
                .setParameter("id", id));
    }
    
    public List<Appointment> findByUserId(Long id) {
        return list(namedQuery("findAppointmentsByUserIdNativeSql")
                .setParameter("id", id));
    }
    
    public List<Appointment> findOccupiedByDoctorIdAndDate(Long id, Timestamp date) {
        return list(namedQuery("findAppointmentsByDoctorIdAndDateNativeSql")
                .setParameter("id", id)
                .setParameter("date", date));
    }
    
    public List<Appointment> findByUserIdAndDate(Long id, Timestamp date) {
        return list(namedQuery("findAppointmentsByUserIdAndDateNativeSql")
                .setParameter("id", id)
                .setParameter("date", date));
    }
    
    public Appointment createAppointment(Appointment appointment) {
        return this.persist(appointment);
    }
    
    public void update(Appointment appointment){
        this.persist(appointment);
    }
    
    public Appointment findExactAppointmentByDocIdAndDate(Long id, Timestamp date) {
        return uniqueResult(namedQuery("findAppointmentsByDocIdAndExactDateNativeSql")
                .setParameter("id", id)
                .setParameter("dayDate", date));
    }
    
        public Appointment findAppointmentById(Long id) {
        return uniqueResult(namedQuery("findAppointmentById")
                .setParameter("id", id));
    }
    
}
