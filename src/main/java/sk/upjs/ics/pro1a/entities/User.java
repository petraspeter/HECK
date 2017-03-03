package sk.upjs.ics.pro1a.entities;

import java.util.List;

/**
 *
 * @author raven
 */
public class User {
    
    private Long idUser;
    
    private String emailUser;
    
    private String passwoedUser;
    
    private String hashUser;
    
    private String phoneUser;
    
    private Double longitudeUser;
    
    private Double latitudeUser;
    
    private List<Appointment> appointments;
    
    /**
     *
     * @return ID pouzivatela
     */
    public Long getIdUser() {
        return idUser;
    }
    
    /**
     *
     *  @param idUser identifikacne cislo pouzivatela
     */
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
    
    /**
     *
     * @return  email pouzivatela
     */
    public String getEmailUser() {
        return emailUser;
    }
    
    /**
     *
     *  @param  emailUser emailova adresa pouzivatela
     */
    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
    
    /**
     *
     * @return heslo pouzivatela
     */
    public String getPasswoedUser() {
        return passwoedUser;
    }
    
    /**
     *
     *  @param passwoedUser heslo pouzivatela
     */
    public void setPasswoedUser(String passwoedUser) {
        this.passwoedUser = passwoedUser;
    }
    
    /**
     *
     * @return Hash kod pouzivatela
     */
    public String getHashUser() {
        return hashUser;
    }
    
    /**
     *
     *  @param hashUser Hash kod pouzivatela
     */
    public void setHashUser(String hashUser) {
        this.hashUser = hashUser;
    }
    
    /**
     *
     * @return  telefonne cislo pouzivatela
     */
    public String getPhoneUser() {
        return phoneUser;
    }
    
    /**
     *
     *  @param phoneUser telefonne cislo pouzivatela
     */
    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }
    
    /**
     *
     * @return zemepisnu dlzku pouzivatela
     */
    public Double getLongitudeUser() {
        return longitudeUser;
    }
    
    /**
     *
     *  @param longitudeUser zemepisna dlzka pouzivatela
     */
    public void setLongitudeUser(Double longitudeUser) {
        this.longitudeUser = longitudeUser;
    }
    
    /**
     *
     * @return zemepisnu  pouzivatela
     */
    public Double getLatitudeUser() {
        return latitudeUser;
    }
    
    /**
     *
     *  @param latitudeUser zemepisna sirka pouzivatela
     */
    public void setLatitudeUser(Double latitudeUser) {
        this.latitudeUser = latitudeUser;
    }
    
    /**
     *
     * @return zoznam terminov pouzivatela
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }
    
    /**
     *
     *  @param appointments zoznam terminov pouzivatela
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
    
    /**
     *
     * @param idUser
     * @param emailUser
     * @param passwoedUser
     * @param hashUser
     * @param phoneUser
     * @param longitudeUser
     * @param latitudeUser
     * @param appointments
     */
    public User(Long idUser, String emailUser, String passwoedUser, String hashUser, String phoneUser,
            Double longitudeUser, Double latitudeUser, List<Appointment> appointments) {
        this.idUser = idUser;
        this.emailUser = emailUser;
        this.passwoedUser = passwoedUser;
        this.hashUser = hashUser;
        this.phoneUser = phoneUser;
        this.longitudeUser = longitudeUser;
        this.latitudeUser = latitudeUser;
        this.appointments = appointments;
    }
    
    /**
     *
     * @param idUser
     * @param emailUser
     * @param passwoedUser
     * @param hashUser
     * @param phoneUser
     */
    public User(Long idUser, String emailUser, String passwoedUser, String hashUser, String phoneUser) {
        this.idUser = idUser;
        this.emailUser = emailUser;
        this.passwoedUser = passwoedUser;
        this.hashUser = hashUser;
        this.phoneUser = phoneUser;
    }
    
    /**
     *
     */
    public User() {
    }
    
}
