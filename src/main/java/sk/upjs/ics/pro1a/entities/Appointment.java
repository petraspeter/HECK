package sk.upjs.ics.pro1a.entities;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author raven
 */
public class Appointment {
    
    private Long idAppointment;
    
    private Doctor doctor;
    
    private User user;
    
    private Boolean  occupied;
    
    private Timestamp dateFrom;
    
    private Timestamp dateTo;
    
    private Boolean holiday;
    
    private Boolean canceled;
    
    private String note;
    
    private String result;
    
    /**
     *
     * @return ID terminu
     */
    public Long getIdAppointment() {
        return idAppointment;
    }
    
    /**
     *
     * @param idAppointment identifikacne cislo terminu
     */
    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }
    
    /**
     *
     * @return vysetrujuceho doktora
     */
    public Doctor getDoctor() {
        return doctor;
    }
    
    /**
     *
     * @param doctor vysetrujuci doktor
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    /**
     *
     * @return objednaneho pouzivatela
     */
    public User getUser() {
        return user;
    }
    
    /**
     *
     * @param user objednany pacient
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     *
     * @return pacientove poznamky k terminu
     */
    public String getNote() {
        return note;
    }
    
    /**
     *
     * @param note pacientove poznamky k terminu
     */
    public void setNote(String note) {
        this.note = note;
    }
    
    /**
     *
     * @return vysledky vysetrenia
     */
    public String getResult() {
        return result;
    }
    
    /**
     *
     * @param result vysledky vysetrenia
     */
    public void setResult(String result) {
        this.result = result;
    }
    
    /**
     *
     * @return  ci je mozne sa objednat na tento termin
     */
    public boolean isOccupied() {
        return occupied;
    }
    
    /**
     *
     *  @param occupied moznost rezervacie terminu
     */
    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }
    
    /**
     *
     * @return  cas zaciatku vysetrenia
     */
    public Timestamp getDateFrom() {
        return dateFrom;
    }
    
    /**
     *
     *  @param dateFrom cas zaciatku vysetrenia
     */
    public void setDateFrom(Timestamp dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    /**
     *
     * @return cas konca vysetrenia
     */
    public Timestamp getDateTo() {
        return dateTo;
    }
    
    /**
     *
     *  @param dateTo cas konca vysetrenia
     */
    public void setDateTo(Timestamp dateTo) {
        this.dateTo = dateTo;
    }
    
    /**
     *
     * @return  ci v dany den lekar ordinuje(alebo ma dovolenku)
     */
    public boolean isHoliday() {
        return holiday;
    }
    
    /**
     *
     *  @param holiday ohlasenie / zrusenie dovolenky
     */
    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
    
    /**
     *
     * @return ci lekar tento termin zrusil
     */
    public boolean isCanceled() {
        return canceled;
    }
    
    /**
     *
     *  @param canceled zrusenie alebo znovu pridanie zruseneho terminu
     */
    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }
    
    /**
     *
     * @param idAppointment
     * @param doctor
     * @param user
     * @param note
     * @param occupied
     * @param dateFrom
     * @param dateTo
     * @param holiday
     * @param canceled
     */
    public Appointment(Long idAppointment, Doctor doctor, User user, String note, Boolean occupied,
            Timestamp dateFrom, Timestamp dateTo, Boolean holiday, Boolean canceled) {
        this.idAppointment = idAppointment;
        this.doctor = doctor;
        this.user = user;
        this.note = note;
        this.occupied = occupied;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.holiday = holiday;
        this.canceled = canceled;
    }
    
    /**
     *
     */
    public Appointment() {
    }
    
}
