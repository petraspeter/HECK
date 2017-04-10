package sk.upjs.ics.pro1a.heck.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 *
 * @author raven
 */
@Entity
@Table(name = "doctor")
@NamedQueries({
    @NamedQuery(name = "findAllDoctors", query = "select doc from Doctor doc"),
    @NamedQuery(name = "findDoctorByLogin",
            query = "select doc from Doctor doc where doc.loginDoctor = :login"),
    @NamedQuery(name = "findDoctorById",
            query = "select doc from Doctor doc where doc.idDoctor = :id")
})
public class Doctor implements Serializable {
    
    @JsonProperty("IdDoctor")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_doctor")
    private Long idDoctor;
    
    @JsonProperty("EmailDoctor")
    @Column(name = "email_doctor", unique = true)
    private String emailDoctor;
    
    @JsonProperty("LoginDoctor")
    @Column(name = "login_doctor", unique = true)
    private String loginDoctor;
    
    @Column(name = "password_doctor")
    private String passwordDoctor;
    
    @Column(name = "salt_doctor")
    private String saltDoctor;
    
    @JsonProperty("SpecializationDoctor")
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "specialization_doctor")
    private  Specialization specializationDoctor;
    
    @JsonProperty("BusinessNameDoctor")
    @Column(name = "business_name_doctor")
    private String businessNameDoctor;
    
    @JsonProperty("FirstNameDoctor")
    @Column(name = "first_name_doctor")
    private String firstNameDoctor;
    
    @JsonProperty("LastNameDoctor")
    @Column(name = "last_name_doctor")
    private String lastNameDoctor;
    
    @JsonProperty("PhoneNumberDoctor")
    @Column(name = "phone_number_doctor")
    private String phoneNumberDoctor;
    
    @JsonProperty("PostalCodeDoctor")
    @Column(name = "postal_code_doctor")
    private Integer postalCodeDoctor;
    
    @JsonProperty("CityDoctor")
    @Column(name = "city_doctor")
    private String cityDoctor;
    
    @JsonProperty("AddressDoctor")
    @Column(name = "address_doctor")
    private String addressDoctor;
    
    @Column(name = "activation_time_doctor")
    private Timestamp activationTimeDoctor;
    
    @JsonProperty("ActivateDoctor")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "active_doctor")
    private Boolean activeDoctor;
    
    public Doctor() {
    }
    
    public Doctor(Long idDoctor, String emailDoctor, String loginDoctor, String passwordDoctor,
            String saltDoctor,  Specialization specializationDoctor, String businessNameDoctor,
            String firstNameDoctor, String lastNameDoctor, String phoneNumberDoctor,
            Integer postalCodeDoctor, String cityDoctor, String addressDoctor,
            Timestamp activationTimeDoctor, Boolean activeDoctor) {
        this.idDoctor = idDoctor;
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
        this.activationTimeDoctor = activationTimeDoctor;
        this.activeDoctor = activeDoctor;
    }
    
    public Doctor(Long idDoctor, String emailDoctor, String loginDoctor, String passwordDoctor,
            Specialization specializationDoctor , String businessNameDoctor, String firstNameDoctor,
            String lastNameDoctor, String phoneNumberDoctor, Integer postalCodeDoctor, String cityDoctor,
            String addressDoctor, Boolean activeDoctor) {
        this.idDoctor = idDoctor;
        this.emailDoctor = emailDoctor;
        this.loginDoctor = loginDoctor;
        this.passwordDoctor = passwordDoctor;
        this.specializationDoctor = specializationDoctor;
        this.businessNameDoctor = businessNameDoctor;
        this.firstNameDoctor = firstNameDoctor;
        this.lastNameDoctor = lastNameDoctor;
        this.phoneNumberDoctor = phoneNumberDoctor;
        this.postalCodeDoctor = postalCodeDoctor;
        this.cityDoctor = cityDoctor;
        this.addressDoctor = addressDoctor;
        this.activeDoctor = activeDoctor;
    }
    
    public Doctor(String emailDoctor, String loginDoctor, Specialization specializationDoctor,
            String businessNameDoctor, String firstNameDoctor, String lastNameDoctor,
            String phoneNumberDoctor, Integer postalCodeDoctor, String cityDoctor, String addressDoctor,
            Timestamp activationTimeDoctor) {
        this.emailDoctor = emailDoctor;
        this.loginDoctor = loginDoctor;
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
    }
    
    public Long getIdDoctor() {
        return idDoctor;
    }
    
    @JsonIgnore
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
    
    @JsonIgnore
    public String getPasswordDoctor() {
        return passwordDoctor;
    }
    
    public void setPasswordDoctor(String passwordDoctor) {
        this.passwordDoctor = passwordDoctor;
    }
    
    @JsonIgnore
    public String getSaltDoctor() {
        return saltDoctor;
    }
    
    @JsonIgnore
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
    
    public Integer getPostalCodeDoctor() {
        return postalCodeDoctor;
    }
    
    public void setPostalCodeDoctor(Integer postalCodeDoctor) {
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
    
    @JsonIgnore
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
    
}
