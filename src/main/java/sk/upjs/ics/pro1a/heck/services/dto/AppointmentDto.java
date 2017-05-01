package sk.upjs.ics.pro1a.heck.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Raven
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDto {
        
    @JsonProperty("id")
    private Long idAppointment;
    
    @JsonProperty("from")
    private String dateFromAppointment;
    
    @JsonProperty("to")
    private String dateToAppointment;
        
    @JsonProperty("doctor")
    private AppointmentDoctorDto appointmentDoctor;
    
    @JsonProperty("user")
    private AppointmentUserDto appointmentUser;
        
    @JsonProperty("patient")
    private String patitentName;
    
    @JsonProperty("note")
    private String noteAppointment;
    
    @JsonProperty("subject")
    private String subjectAppointment;
    
    @JsonProperty("occupied")
    private Boolean occupiedAppointment;
    
    @JsonProperty("canceled")
    private Boolean canceledAppointment;
    
    @JsonProperty("holiday")
    private Boolean holidayAppointment;
            
    public Long getIdAppointment() {
        return idAppointment;
    }
    
    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }
    
    public AppointmentDoctorDto getAppointmentDoctor() {
        return appointmentDoctor;
    }
    
    public void setAppointmentDoctor(AppointmentDoctorDto doctorAppointment) {
        this.appointmentDoctor = doctorAppointment;
    }
    
    public AppointmentUserDto getAppointmentUser() {
        return appointmentUser;
    }
    
    public void setAppointmentUser(AppointmentUserDto appointmentUser) {
        this.appointmentUser = appointmentUser;
    }
    
    public Boolean getOccupiedAppointment() {
        return occupiedAppointment;
    }
    
    public void setOccupiedAppointment(Boolean occupiedAppointment) {
        this.occupiedAppointment = occupiedAppointment;
    }
    
    public String getDateFromAppointment() {
        return dateFromAppointment;
    }
    
    public void setDateFromAppointment(String dateFromAppointment) {
        this.dateFromAppointment = dateFromAppointment;
    }
    
    public String getDateToAppointment() {
        return dateToAppointment;
    }
    
    public void setDateToAppointment(String dateToAppointment) {
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
        return "AppointmentDto{" + "idAppointment=" + idAppointment + ", dateFromAppointment=" + dateFromAppointment + ", dateToAppointment=" + dateToAppointment + ", doctorAppointment=" + appointmentDoctor + ", appointmentUser=" + appointmentUser + ", patitentName=" + patitentName + ", noteAppointment=" + noteAppointment + ", subjectAppointment=" + subjectAppointment + ", occupiedAppointment=" + occupiedAppointment + ", canceledAppointment=" + canceledAppointment + ", holidayAppointment=" + holidayAppointment + '}';
    }
    
    
    
}
