package sk.upjs.ics.pro1a.heck.repositories.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author raven
 */
@Entity()
@Table(name = "specialization")
@NamedQueries({
        @NamedQuery(name = "findAllSpecializations",
                query = "select spec from Specialization spec"),
        @NamedQuery(name = "findSpecializationById",
                query = "select spec from Specialization spec WHERE spec.id = :id"),
        @NamedQuery(name = "findSpecializationByName",
                query = "select spec from Specialization spec WHERE spec.specializationName = :name")
})
public class Specialization implements Serializable {

    @Id
    @Column(name = "specialization_doctor")
    private Long id;

    @Column(name = "type_specialization")
    private String specializationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    public Specialization(Long id, String specializationName) {
        this.id = id;
        this.specializationName = specializationName;
    }

    public Specialization() {
    }

}
