package sk.upjs.ics.pro1a.heck.db.core;

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
    @NamedQuery(
            name = "findAllDoctors",
            query = "select doc from Doctor doc"),
    @NamedQuery(
            name = "findDoctorByLogin",
            query = "select doc from Doctor doc where doc.loginDoctor = :login"),
    @NamedQuery(name = "findDoctorByEmail",
            query = "select doc from Doctor doc where doc.emailDoctor = :email"),
    @NamedQuery(
            name = "findDoctorByLoginAndPassword",
            query = "select doc from Doctor doc where doc.loginDoctor = :login and doc.passwordDoctor = :password")
})
@NamedNativeQueries({
    @NamedNativeQuery(
            name = "findAllDoctorsBySpecializationNativeSql",
            query = "select * from doctor doc where doc.specialization_doctor = :specialization",
            resultClass = Doctor.class
    ),
    @NamedNativeQuery(
            name = "findAllDoctorsBySpecializationAndCityNativeSql",
            query = "select * from doctor doc where doc.specialization_doctor = :specialization "
                    + "AND doc.city_doctor = :city",
            resultClass = Doctor.class
    ),
    @NamedNativeQuery(
            name = "findAllDoctorsBySpecializationAndFullNameNativeSql",
            query = "select * from doctor doc where doc.specialization_doctor = :specialization AND"
                    + " doc.first_name_doctor LIKE :firstName AND doc.last_name_doctor LIKE :lastName",
            resultClass = Doctor.class
    ),
    @NamedNativeQuery(
            name = "findAllDoctorsBySpecializationAndFullNameAndCityNativeSql",
            query = "select * from doctor doc where doc.specialization_doctor = :specialization AND "
                    + "doc.first_name_doctor LIKE :firstName AND doc.last_name_doctor LIKE :lastName "
                    + "AND doc.city_doctor = :city",
            resultClass = Doctor.class
    ),
    @NamedNativeQuery(
            name = "findAllDoctorsBySpecializationAndLastNameNativeSql",
            query = "select * from doctor doc where doc.specialization_doctor = :specialization AND "
                    + "doc.last_name_doctor LIKE :lastName",
            resultClass = Doctor.class
    ),
    @NamedNativeQuery(
            name = "findAllDoctorsBySpecializationAndLastNameAndCityNativeSql",
            query = "select * from doctor doc where doc.specialization_doctor = :specialization AND "
                    + "doc.last_name_doctor LIKE :lastName"
                    + "AND doc.city_doctor = :city",
            resultClass = Doctor.class
    )
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
    
    @Column(name = "interval_doctor")
    private Short appointmentInterval;
    
    @Column(name = "registration_time_doctor")
    private Timestamp registrationTime;
    
    public Doctor() {
    }
    
    public Doctor(String emailDoctor, String loginDoctor, String passwordDoctor, String salt, Specialization specializationDoctor,
            String businessNameDoctor, String firstNameDoctor, String lastNameDoctor,
            String phoneNumberDoctor, String postalCodeDoctor, String cityDoctor, String addressDoctor,
            Timestamp activationTimeDoctor, Short appointmentInterval, Timestamp registrationTime) {
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
        this.appointmentInterval = appointmentInterval;
        this.registrationTime = registrationTime;
    }
    
    public Doctor(String emailDoctor, String loginDoctor, String passwordDoctor, String saltDoctor,
            Specialization specializationDoctor, String businessNameDoctor, String firstNameDoctor,
            String lastNameDoctor, String phoneNumberDoctor, String postalCodeDoctor, String cityDoctor,
            String addressDoctor, Short appointmentInterval, Timestamp registrationTime) {
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
        this.appointmentInterval = appointmentInterval;
        this.registrationTime = registrationTime;
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
    
    public Short getAppointmentInterval() {
        return appointmentInterval;
    }
    
    public void setAppointmentInterval(Short appointmentInterval) {
        this.appointmentInterval = appointmentInterval;
    }
    
    public Timestamp getRegistrationTime() {
        return registrationTime;
    }
    
    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }
    
}
