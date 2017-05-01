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
public class AppointmentDoctorDto {
        
    @JsonProperty("id")
    private Long idDoctor;    
        
    @JsonProperty("firstName")
    private String firstNameDoctor;
    
    @JsonProperty("lastName")
    private String lastNameDoctor;
        
    @JsonProperty("office")
    private String officeDoctor;

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
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

    public String getOfficeDoctor() {
        return officeDoctor;
    }

    public void setOfficeDoctor(String officeDoctor) {
        this.officeDoctor = officeDoctor;
    }

    public AppointmentDoctorDto(Long id, String firstName, String lastName, String office) {
        this.idDoctor = id;
        this.firstNameDoctor = firstName;
        this.lastNameDoctor = lastName;
        this.officeDoctor = office;
    }

    public AppointmentDoctorDto() {
    }
        
}


 