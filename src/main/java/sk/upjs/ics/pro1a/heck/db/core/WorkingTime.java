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
@NamedQueries({
        @NamedQuery(name = "findWorkingTimeByDoctorId",
                query = "select w from WorkingTime w where w.doctor.idDoctor = :doctorId")

})
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
