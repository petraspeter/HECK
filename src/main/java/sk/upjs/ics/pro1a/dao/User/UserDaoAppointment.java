package sk.upjs.ics.pro1a.dao.User;

import java.sql.Timestamp;
import java.util.List;
import sk.upjs.ics.pro1a.entities.Appointment;
import sk.upjs.ics.pro1a.entities.User;

/**
 *
 * @author raven
 */
public interface UserDaoAppointment {
    
    public List<Appointment> searchForAppointments(String specialist, Timestamp day, String city);
    
    public List<Appointment> searchForAppointments(String specialist, Timestamp day, Long postcode);
    
    public List<Appointment> searchForAppointments(String specialist, Timestamp from, Timestamp to,
            String city);
    
    public List<Appointment> searchForAppointments(String specialist, Timestamp from, Timestamp to,
            Long postcode);
    
    public List<Appointment> searchForAppointments(String specialist, Timestamp from, Timestamp to,
            Double distance, Long Latitude, Long Longitude);
    
    public List<Appointment> searchForAppointments(String specialist, Timestamp day,  Double distance,
            Long Latitude, Long Longitude);
    
    public boolean isAppointmentsAviable(Appointment appointment);
    
    public void bookAppointment(Appointment appointment);
    
    public void cancelAppointment(Appointment appointment);
    
    public List<Appointment> showYourActualAppointments(User user);
    
    public List<Appointment> showYourPreviousAppointments(User user);
    
    public List<Appointment> showYourAppointments(User user);
    
    
}
