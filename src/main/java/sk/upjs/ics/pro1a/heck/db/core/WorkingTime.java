package sk.upjs.ics.pro1a.heck.db.core;

import java.io.Serializable;
import java.sql.Time;
import javax.persistence.*;

/**
 *
 * @author Raven
 */
@Entity
@Table(name = "working_time")
public class WorkingTime implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @JoinColumn(name = "id_doctor")
    @ManyToOne(cascade = CascadeType.ALL)
    private Doctor doctor;
    
    @Column(name = "day_of_the_week")
    private int dayOfTheWeek;
    
    @Column(name = "starting_hour")
    private Time startingHour;
    
    @Column(name = "ending_hour")
    private Time endingHour;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }
    
    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
    
    public Time getStartingHour() {
        return startingHour;
    }
    
    public void setStartingHour(Time startingHour) {
        this.startingHour = startingHour;
    }
    
    public Time getEndingHour() {
        return endingHour;
    }
    
    public void setEndingHour(Time endingHour) {
        this.endingHour = endingHour;
    }
}
