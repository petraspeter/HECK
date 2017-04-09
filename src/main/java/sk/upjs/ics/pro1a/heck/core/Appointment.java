package sk.upjs.ics.pro1a.heck.core;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

/**
 *
 * @author raven
 */
public class Appointment {
    
    
    /**
     *          TODO
     * 
     *          dorobit dopyty, namapovat pre termin usera a doktora
     *          konstruktor + get/set
     */
    
    
    @Id
    @Column(name="id_appointment")
    private Long idAppointment;
    
    /*    
    private Doctor doctorAppointment;
    
    private User userAppointment;
    */
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name="occupied_appointment")
    private Boolean occupiedAppointment;
    
    @Column(name="date_from_appointment")
    private Timestamp dateFromAppointment;
    
    @Column(name="date_to_appointment")
    private Timestamp dateToAppointment;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name="holiday_appointment")
    private Boolean holidayAppointment;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name="canceled_appointment")
    private Boolean canceledAppointment;
    
    @Column(name="patitent_name")
    private String patitentName;
    
    @Column(name="note_appointment")
    private String noteAppointment;
    
    @Column(name="result_appointment")
    private String resultAppointment;
    
    
}
