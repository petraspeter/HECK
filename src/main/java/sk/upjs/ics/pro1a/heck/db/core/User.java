package sk.upjs.ics.pro1a.heck.db.core;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author raven
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "email_user", unique = true)
    private String emailUser;

    @Column(name = "login_user", unique = true)
    private String loginUser;

    @Column(name = "password_user")
    private String passwordUser;

    @Column(name = "salt_user")
    private String saltUser;

    @Column(name = "first_name_user")
    private String firstNameUser;

    @Column(name = "last_name_user")
    private String lastNameUser;

    @Column(name = "phone_user")
    private String phoneUser;

    @Column(name = "postal_code_user")
    private String postalCodeUser;

    @Column(name = "city_user")
    private String cityUser;

    @Column(name = "address_user")
    private String addressUser;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "active_user")
    private Boolean activeUser;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_admin")
    private Boolean isAdmin;
    
    @Column(name = "registration_time_user")
    private Timestamp registrationTime;
    
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getSaltUser() {
        return saltUser;
    }

    public void setSaltUser(String saltUser) {
        this.saltUser = saltUser;
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public void setFirstNameUser(String firstNameUser) {
        this.firstNameUser = firstNameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public void setLastNameUser(String lastNameUser) {
        this.lastNameUser = lastNameUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getPostalCodeUser() {
        return postalCodeUser;
    }

    public void setPostalCodeUser(String postalCodeUser) {
        this.postalCodeUser = postalCodeUser;
    }

    public String getCityUser() {
        return cityUser;
    }

    public void setCityUser(String cityUser) {
        this.cityUser = cityUser;
    }

    public String getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(String addressUser) {
        this.addressUser = addressUser;
    }

    public Boolean getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(Boolean activeUser) {
        this.activeUser = activeUser;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public User() {
    }

  public User(String emailUser, String loginUser, String passwordUser, String salt, String firstNameUser, String lastNameUser, String phoneUser,
              String postalCodeUser, String cityUser, String addressUser, Timestamp registrationTime, Boolean activeUser, Boolean isAdmin) {
        this.emailUser = emailUser;
        this.loginUser = loginUser;
        this.passwordUser = passwordUser;
        this.saltUser = salt;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
        this.phoneUser = phoneUser;
        this.postalCodeUser = postalCodeUser;
        this.cityUser = cityUser;
        this.addressUser = addressUser;
        this.activeUser = activeUser;
        this.registrationTime = registrationTime;
        this.isAdmin = isAdmin;
    }
  
}
