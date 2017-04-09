package sk.upjs.ics.pro1a.heck.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 *
 * @author raven
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "findAllUsers", query = "select u from User u"),
    @NamedQuery(name = "findUserByLogin",
            query = "select u from User u where u.loginUser = :login"),
    @NamedQuery(name = "findUserById",
            query = "select u from User u where u.idUser = :id")
})
public class User implements Serializable {
    
    @Id
    @JsonProperty("IdUser")
    @Column(name="id_user")
    private Long idUser;
    
    @JsonProperty("EmailIUser")
    @Column(name = "email_user", unique = true)
    private String emailUser;
    
    @JsonProperty("LoginUser")
    @Column(name = "login_user", unique = true)
    private String loginUser;
    
    @Column(name = "password_user")
    private String passwordUser;
    
    @Column(name = "salt_user")
    private String saltUser;
    
    @JsonProperty("FirstNameUser")
    @Column(name = "first_name_user")
    private String firstNameUser;
    
    @JsonProperty("LastNameUser")
    @Column(name = "last_name_user")
    private String lastNameUser;
    
    @JsonProperty("PhoneUser")
    @Column(name = "phone_user")
    private String phoneUser;
    
    @JsonProperty("PostalCodeUser")
    @Column(name = "postal_code_user")
    private Integer postalCodeUser;
    
    @JsonProperty("CityUser")
    @Column(name = "city_user")
    private String cityUser;
    
    @JsonProperty("AddressUser")
    @Column(name = "address_user")
    private String addressUser;
    
    @JsonProperty("ActiveUser")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "active_user")
    private Boolean activeUser;
    
    @JsonProperty("IsAdmin")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_admin")
    private Boolean isAdmin;
    
    public Long getIdUser() {
        return idUser;
    }
    
    @JsonIgnore
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
    
    @JsonIgnore
    public String getPasswordUser() {
        return passwordUser;
    }
    
    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }
    
    @JsonIgnore
    public String getSaltUser() {
        return saltUser;
    }
    
    @JsonIgnore
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
    
    public Integer getPostalCodeUser() {
        return postalCodeUser;
    }
    
    public void setPostalCodeUser(Integer postalCodeUser) {
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
    
    public Boolean getIsAdmin() {
        return isAdmin;
    }
    
    @JsonIgnore
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public User(Long idUser, String emailUser, String loginUser, String passwordUser, String saltUser,
            String firstNameUser, String lastNameUser, String phoneUser, Integer postalCodeUser,
            String cityUser, String addressUser, Boolean activeUser, Boolean isAdmin) {
        this.idUser = idUser;
        this.emailUser = emailUser;
        this.loginUser = loginUser;
        this.passwordUser = passwordUser;
        this.saltUser = saltUser;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
        this.phoneUser = phoneUser;
        this.postalCodeUser = postalCodeUser;
        this.cityUser = cityUser;
        this.addressUser = addressUser;
        this.activeUser = activeUser;
        this.isAdmin = isAdmin;
    }
    
    
    public User(Long idUser, String emailUser, String loginUser, String passwordUser, String saltUser,
            String firstNameUser, String lastNameUser, String phoneUser, Integer postalCodeUser,
            String cityUser, String addressUser) {
        this.idUser = idUser;
        this.emailUser = emailUser;
        this.loginUser = loginUser;
        this.passwordUser = passwordUser;
        this.saltUser = saltUser;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
        this.phoneUser = phoneUser;
        this.postalCodeUser = postalCodeUser;
        this.cityUser = cityUser;
        this.addressUser = addressUser;
        this.activeUser = true;
        this.isAdmin = false;
    }
    
    /**
     * defaultny konstruktor pre triedu User
     */
    public User() {
    }
    
    
    
    
    
}
