package sk.upjs.ics.pro1a.heck.repositories.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author raven
 */
@Entity
@Table(name = "doctor")
@NamedQueries({
    @NamedQuery(name = "findAllDoctors", query = "select doc from Doctor doc"),
    @NamedQuery(name = "findDoctorByLogin",
            query = "select doc from Doctor doc where doc.loginDoctor = :login"),
        @NamedQuery(name = "findDoctorByEmail",
                query = "select doc from Doctor doc where doc.emailDoctor = :email"),
    @NamedQuery(name = "findDoctorByLoginAndPassword",
            query = "select doc from Doctor doc where doc.loginDoctor = :login and doc.passwordDoctor = :password")
})
public class Doctor implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private Long idDoctor;
    
    @Column(name = "email_doctor", unique = true)
    private String emailDoctor;
    
    @Column(name = "login_doctor", unique = true)
    private String loginDoctor;
    
    @Column(name = "password_doctor")
    private String passwordDoctor;
    
    @Column(name = "salt_doctor")
    private String saltDoctor;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specialization_doctor")
    private Specialization specializationDoctor;
    
    @Column(name = "business_name_doctor")
    private String businessNameDoctor;
    
    @Column(name = "first_name_doctor")
    private String firstNameDoctor;
    
    @Column(name = "last_name_doctor")
    private String lastNameDoctor;
    
    @Column(name = "phone_number_doctor")
    private String phoneNumberDoctor;
    
    @Column(name = "postal_code_doctor")
    private String postalCodeDoctor;
    
    @Column(name = "city_doctor")
    private String cityDoctor;
    
    @Column(name = "address_doctor")
    private String addressDoctor;
    
    @Column(name = "activation_time_doctor")
    private Timestamp activationTimeDoctor;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "active_doctor")
    private Boolean activeDoctor;
    
//    @Type(type = "org.hibernate.type.NumericBooleanType")
//    @Column(name = "is_admin")
//    private Boolean isAdmin;
    
    public Doctor() {
    }
    
    public Doctor(String emailDoctor, String loginDoctor, String passwordDoctor, String salt, Specialization specializationDoctor,
            String businessNameDoctor, String firstNameDoctor, String lastNameDoctor,
            String phoneNumberDoctor, String postalCodeDoctor, String cityDoctor, String addressDoctor,
            Timestamp activationTimeDoctor) {
        this.emailDoctor = emailDoctor;
        this.loginDoctor = loginDoctor;
        this.passwordDoctor = passwordDoctor;
        this.saltDoctor = salt;
        this.specializationDoctor = specializationDoctor;
        this.businessNameDoctor = businessNameDoctor;
        this.firstNameDoctor = firstNameDoctor;
        this.lastNameDoctor = lastNameDoctor;
        this.phoneNumberDoctor = phoneNumberDoctor;
        this.postalCodeDoctor = postalCodeDoctor;
        this.cityDoctor = cityDoctor;
        this.addressDoctor = addressDoctor;
        this.activationTimeDoctor = activationTimeDoctor;
        this.activeDoctor = true;
//        this.isAdmin = false;
    }
    
    public Doctor(String emailDoctor, String loginDoctor, String passwordDoctor, String saltDoctor,
            Specialization specializationDoctor, String businessNameDoctor, String firstNameDoctor,
            String lastNameDoctor, String phoneNumberDoctor, String postalCodeDoctor, String cityDoctor,
            String addressDoctor) {
        this.emailDoctor = emailDoctor;
        this.loginDoctor = loginDoctor;
        this.passwordDoctor = passwordDoctor;
        this.saltDoctor = saltDoctor;
        this.specializationDoctor = specializationDoctor;
        this.businessNameDoctor = businessNameDoctor;
        this.firstNameDoctor = firstNameDoctor;
        this.lastNameDoctor = lastNameDoctor;
        this.phoneNumberDoctor = phoneNumberDoctor;
        this.postalCodeDoctor = postalCodeDoctor;
        this.cityDoctor = cityDoctor;
        this.addressDoctor = addressDoctor;
        this.activeDoctor = false;
     //   this.isAdmin = false;
    }
    
    public Long getIdDoctor() {
        return idDoctor;
    }
    
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }
    
    public String getEmailDoctor() {
        return emailDoctor;
    }
    
    public void setEmailDoctor(String emailDoctor) {
        this.emailDoctor = emailDoctor;
    }
    
    public String getLoginDoctor() {
        return loginDoctor;
    }
    
    public void setLoginDoctor(String loginDoctor) {
        this.loginDoctor = loginDoctor;
    }
    
    public String getPasswordDoctor() {
        return passwordDoctor;
    }
    
    public void setPasswordDoctor(String passwordDoctor) {
        this.passwordDoctor = passwordDoctor;
    }
    
    public String getSaltDoctor() {
        return saltDoctor;
    }
    
    public void setSaltDoctor(String saltDoctor) {
        this.saltDoctor = saltDoctor;
    }
    
    public Specialization getSpecializationDoctor() {
        return specializationDoctor;
    }
    
    public void setSpecializationDoctor(Specialization specializationDoctor) {
        this.specializationDoctor = specializationDoctor;
    }
    
    public String getBusinessNameDoctor() {
        return businessNameDoctor;
    }
    
    public void setBusinessNameDoctor(String businessNameDoctor) {
        this.businessNameDoctor = businessNameDoctor;
    }
    
    public String getFirstNameDoctor() {
        return firstNameDoctor;
    }
    
    public void setFirstNameDoctor(String firstNameDoctor) {
        this.firstNameDoctor = firstNameDoctor;
    }
    
    public String getLastNameDoctor() {
        return lastNameDoctor;
    }
    
    public void setLastNameDoctor(String lastNameDoctor) {
        this.lastNameDoctor = lastNameDoctor;
    }
    
    public String getPhoneNumberDoctor() {
        return phoneNumberDoctor;
    }
    
    public void setPhoneNumberDoctor(String phoneNumberDoctor) {
        this.phoneNumberDoctor = phoneNumberDoctor;
    }
    
    public String getPostalCodeDoctor() {
        return postalCodeDoctor;
    }
    
    public void setPostalCodeDoctor(String postalCodeDoctor) {
        this.postalCodeDoctor = postalCodeDoctor;
    }
    
    public String getCityDoctor() {
        return cityDoctor;
    }
    
    public void setCityDoctor(String cityDoctor) {
        this.cityDoctor = cityDoctor;
    }
    
    public String getAddressDoctor() {
        return addressDoctor;
    }
    
    public void setAddressDoctor(String addressDoctor) {
        this.addressDoctor = addressDoctor;
    }
    
    public Timestamp getActivationTimeDoctor() {
        return activationTimeDoctor;
    }
    
    public void setActivationTimeDoctor(Timestamp activationTimeDoctor) {
        this.activationTimeDoctor = activationTimeDoctor;
    }
    
    public Boolean getActiveDoctor() {
        return activeDoctor;
    }
    
    public void setActiveDoctor(Boolean activeDoctor) {
        this.activeDoctor = activeDoctor;
    }
    
//    public Boolean getIsAdmin() {
//        return isAdmin;
//    }
//    
//    public void setIsAdmin(Boolean isAdmin) {
//        this.isAdmin = isAdmin;
//    }
    
}
