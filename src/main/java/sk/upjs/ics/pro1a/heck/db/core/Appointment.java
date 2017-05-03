package sk.upjs.ics.pro1a.heck.db.core;

import java.io.Serializable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author raven
 */
@Entity
@Table(name = "appointment")
@NamedQueries({
    @NamedQuery(
            name = "findAllAppointments",
            query = "select app from Appointment app"),
    @NamedQuery(
            name = "findAppointmentById",
            query = "select app from Appointment app where app.idAppointment = :id")
})
@NamedNativeQueries({
    @NamedNativeQuery(
            name = "findAppointmentsByDoctorIdNativeSql",
            query = "select * from appointment app where app.id_doctor = :id",
            resultClass = Appointment.class
    ),
    @NamedNativeQuery(
            name = "findAppointmentsByUserIdNativeSql",
            query = "select * from appointment app where app.id_user = :id",
            resultClass = Appointment.class
    ),
    @NamedNativeQuery(
            name = "findAppointmentsByDoctorIdAndDateNativeSql",
            query = "select * from appointment app where app.id_doctor = :id AND "
                    + "DATE(app.date_from_appointment) = :date",
            resultClass = Appointment.class
    ),
    @NamedNativeQuery(
            name = "findAppointmentsByUserIdAndDateNativeSql",
            query = "select * from appointment app where app.id_user = :id AND "
                    + "DATE(app.date_from_appointment) = :date",
            resultClass = Appointment.class
    ),
    @NamedNativeQuery(
            name = "findAppointmentsByDocIdAndExactDateNativeSql",
            query = "select * from appointment app where app.id_user = :id AND "
                    + "app.date_from_appointment = :date",
            resultClass = Appointment.class
    )
})
public class Appointment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appointment")
    private Long idAppointment;
    
//    @Formula("SELECT CONCAT(first_name_doctor, ' ', last_name_doctor) FROM "
//            + "doctor doc JOIN appointment on  doc.id_doctor = id_doctor")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_doctor")
    private Doctor appointmentDoctor;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User appointmentUser;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "occupied_appointment")
    private Boolean occupiedAppointment;
    
    @Column(name = "date_from_appointment")
    private Timestamp dateFromAppointment;
    
    @Column(name = "date_to_appointment")
    private Timestamp dateToAppointment;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "holiday_appointment")
    private Boolean holidayAppointment;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "canceled_appointment")
    private Boolean canceledAppointment;
    
    @Column(name = "patitent_name")
    private String patitentName;
    
    @Column(name = "note_appointment")
    private String noteAppointment;
    
    @Column(name = "subject_appointment")
    private String subjectAppointment;
    
    public Appointment(Long idAppointment, Doctor doctorAppointment,User appointmentUser,
            Boolean occupiedAppointment, Timestamp dateFromAppointment, Timestamp dateToAppointment,
            Boolean holidayAppointment, Boolean canceledAppointment, String patitentName, String noteAppointment,
            String subjectAppointment) {
        this.idAppointment = idAppointment;
        this.appointmentDoctor = doctorAppointment;
        this.appointmentUser = appointmentUser;
        this.occupiedAppointment = occupiedAppointment;
        this.dateFromAppointment = dateFromAppointment;
        this.dateToAppointment = dateToAppointment;
        this.holidayAppointment = holidayAppointment;
        this.canceledAppointment = canceledAppointment;
        this.patitentName = patitentName;
        this.noteAppointment = noteAppointment;
        this.subjectAppointment = subjectAppointment;
    }
    
    public Appointment(Doctor doctorAppointment, User appointmentUser, Boolean occupiedAppointment,
            Timestamp dateFromAppointment, Timestamp dateToAppointment, Boolean holidayAppointment,
            Boolean canceledAppointment, String patitentName, String noteAppointment, String subjectAppointment) {
        this.appointmentDoctor = doctorAppointment;
        this.appointmentUser = appointmentUser;
        this.occupiedAppointment = occupiedAppointment;
        this.dateFromAppointment = dateFromAppointment;
        this.dateToAppointment = dateToAppointment;
        this.holidayAppointment = holidayAppointment;
        this.canceledAppointment = canceledAppointment;
        this.patitentName = patitentName;
        this.noteAppointment = noteAppointment;
        this.subjectAppointment = subjectAppointment;
    }
    
    public Appointment() {
    }
    
    public Long getIdAppointment() {
        return idAppointment;
    }
    
    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }
    
    public Doctor getAppointmentDoctor() {
        return appointmentDoctor;
    }
    
    public void setAppointmentDoctor(Doctor doctorAppointment) {
        this.appointmentDoctor = doctorAppointment;
    }
    
    public User getAppointmentUser() {
        return appointmentUser;
    }
    
    public void setAppointmentUser(User appointmentUser) {
        this.appointmentUser = appointmentUser;
    }
    
    public Boolean getOccupiedAppointment() {
        return occupiedAppointment;
    }
    
    public void setOccupiedAppointment(Boolean occupiedAppointment) {
        this.occupiedAppointment = occupiedAppointment;
    }
    
    public Timestamp getDateFromAppointment() {
        return dateFromAppointment;
    }
    
    public void setDateFromAppointment(Timestamp dateFromAppointment) {
        this.dateFromAppointment = dateFromAppointment;
    }
    
    public Timestamp getDateToAppointment() {
        return dateToAppointment;
    }
    
    public void setDateToAppointment(Timestamp dateToAppointment) {
        this.dateToAppointment = dateToAppointment;
    }
    
    public Boolean getHolidayAppointment() {
        return holidayAppointment;
    }
    
    public void setHolidayAppointment(Boolean holidayAppointment) {
        this.holidayAppointment = holidayAppointment;
    }
    
    public Boolean getCanceledAppointment() {
        return canceledAppointment;
    }
    
    public void setCanceledAppointment(Boolean canceledAppointment) {
        this.canceledAppointment = canceledAppointment;
    }
    
    public String getPatitentName() {
        return patitentName;
    }
    
    public void setPatitentName(String patitentName) {
        this.patitentName = patitentName;
    }
    
    public String getNoteAppointment() {
        return noteAppointment;
    }
    
    public void setNoteAppointment(String noteAppointment) {
        this.noteAppointment = noteAppointment;
    }
    
    public String getSubjectAppointment() {
        return subjectAppointment;
    }
    
    public void setSubjectAppointment(String subjectAppointment) {
        this.subjectAppointment = subjectAppointment;
    }
    
    @Override
    public String toString() {
        return "Appointment{" + "idAppointment=" + idAppointment + ", appointmentDoctor=" + appointmentDoctor + ", appointmentUser=" + appointmentUser + ", occupiedAppointment=" + occupiedAppointment + ", dateFromAppointment=" + dateFromAppointment + ", dateToAppointment=" + dateToAppointment + ", holidayAppointment=" + holidayAppointment + ", canceledAppointment=" + canceledAppointment + ", patitentName=" + patitentName + ", noteAppointment=" + noteAppointment + ", subjectAppointment=" + subjectAppointment + '}';
    }
    
    
}
