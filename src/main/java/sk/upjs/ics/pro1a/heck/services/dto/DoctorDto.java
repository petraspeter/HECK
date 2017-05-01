package sk.upjs.ics.pro1a.heck.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("login")
    private String login;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("office")
    private String office;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("postalCode")
    private String postalCode;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("specialization")
    private Long specialization;
    
    @JsonProperty("interval")
    private Short interval;
    
    @JsonProperty("registrationTime")
    private Timestamp registrationTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOffice() {
        return office;
    }
    
    public void setOffice(String office) {
        this.office = office;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public long getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(long specialization) {
        this.specialization = specialization;
    }
    
    public Short getInterval() {
        return interval;
    }
    
    public void setInterval(Short interval) {
        this.interval = interval;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }
    
    public DoctorDto() {
    }
    
    public DoctorDto(Long id, String login, String password, String firstName, String lastName, String email, String office,
            String address, String postalCode, String city, String phoneNumber, Long specialization, Short  interval,
            Timestamp registrationTime) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.office = office;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.interval = interval;
        this.registrationTime = registrationTime;
    }
    
    public DoctorDto(Long id, String login, String firstName, String lastName, String email, String office, String address,
            String postalCode, String city, String phoneNumber, Long specialization, Short  interval,
            Timestamp registrationTime) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.office = office;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.interval = interval;
        this.registrationTime = registrationTime;
    }
    
    
    
}
