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
public class AppointmentUserDto {
    
    @JsonProperty("id")
    private Long idUser;
    
    @JsonProperty("firstName")
    private String firstNameUser;
    
    @JsonProperty("lastName")
    private String lastNameUser;
    
    public Long getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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
    
    public AppointmentUserDto(Long idUser, String firstNameUser, String lastNameUser) {
        this.idUser = idUser;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
    }
    
    public AppointmentUserDto() {
    }
    
    @Override
    public String toString() {
        return "AppointmentUserDto{" + "idUser=" + idUser + ", firstNameUser=" + firstNameUser + ", lastNameUser=" + lastNameUser + '}';
    }
    
}
