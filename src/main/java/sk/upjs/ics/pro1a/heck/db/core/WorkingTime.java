package sk.upjs.ics.pro1a.heck.db.core;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

/**
 *
 * @author Raven
 */
@Entity
@Table(name = "working_time")
@NamedNativeQueries({    
    @NamedNativeQuery(
            name = "findAlWorkingHoursByDoctorId",
            query = "select * from working_time wt WHERE wt.id_doctor = :idDoctor",
            resultClass = WorkingTime.class
    ),
    @NamedNativeQuery(
            name = "findAlWorkingHoursByDoctorIdAndDayOfTheWeek",
            query = "select * from working_time  wt WHERE wt.id_doctor = :idDoctor AND "
                    + "wt.day_of_the_week LIKE :dayOfTheWeek",
            resultClass = WorkingTime.class
    )
})
public class WorkingTime implements Serializable{
    
    @Id
    @Column(name = "id_doctor")
    private Long idDoctor;
    
    @Column(name = "day_of_the_week")
    private String dayOfTheWeek;
    
    @Column(name = "starting_hour")
    private Timestamp startingHour;
    
    @Column(name = "ending_hour")
    private Timestamp endingHour;
    
    public Long getIdDoctor() {
        return idDoctor;
    }
    
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }
    
    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }
    
    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
    
    public Timestamp getStartingHour() {
        return startingHour;
    }
    
    public void setStartingHour(Timestamp startingHour) {
        this.startingHour = startingHour;
    }
    
    public Timestamp getEndingHour() {
        return endingHour;
    }
    
    public void setEndingHour(Timestamp endingHour) {
        this.endingHour = endingHour;
    }
    
    public WorkingTime(Long idDoctor, String dayOfTheWeek, Timestamp startingHour, Timestamp endingHour) {
        this.idDoctor = idDoctor;
        this.dayOfTheWeek = dayOfTheWeek;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
    }
    
    public WorkingTime() {
    }
    
}
