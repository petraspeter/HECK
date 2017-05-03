package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;

/**
 *
 * @author Raven
 */
public class WorkingTimeDao extends AbstractDAO<WorkingTime>{
    
    public WorkingTimeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<WorkingTime> findWorkingTimeByDoctorId(Long id) {
        return list(namedQuery("findAlWorkingHoursByDoctorId")
                .setParameter("idDoctor", id));
    }
    
   public List<WorkingTime> findWorkingTimeByDoctorIdAndDay(Long id, int dayName) {
        return list(namedQuery("findAlWorkingHoursByDoctorIdAndDayOfTheWeekNativeQuery")
                .setParameter("idDoctor", id)
                .setParameter("dayOfTheWeek", dayName));
    }

    public WorkingTime createWorkingTime(WorkingTime workingTime) {
        return this.persist(workingTime);
    }

    public List<WorkingTime> findByDoctorId(long doctorId) {
        return list(namedQuery("findWorkingTimeByDoctorId").setParameter("doctorId", doctorId));
    }
}
