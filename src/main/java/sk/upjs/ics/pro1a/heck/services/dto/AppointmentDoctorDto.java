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
    
    @JsonProperty("specialization")
    private String specializationDoctor;

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

    public String getSpecializationDoctor() {
        return specializationDoctor;
    }

    public void setSpecializationDoctor(String specializationDoctor) {
        this.specializationDoctor = specializationDoctor;
    }
    
    public AppointmentDoctorDto(Long idDoctor, String firstNameDoctor, String lastNameDoctor, String officeDoctor, String specializationDoctor) {
        this.idDoctor = idDoctor;
        this.firstNameDoctor = firstNameDoctor;
        this.lastNameDoctor = lastNameDoctor;
        this.officeDoctor = officeDoctor;
        this.specializationDoctor = specializationDoctor;
    }  

    public AppointmentDoctorDto() {
    }

    @Override
    public String toString() {
        return "AppointmentDoctorDto{" + "idDoctor=" + idDoctor + ", firstNameDoctor=" + firstNameDoctor + ", lastNameDoctor=" + lastNameDoctor + ", officeDoctor=" + officeDoctor + ", specializationDoctor=" + specializationDoctor + '}';
    }
        
}


 