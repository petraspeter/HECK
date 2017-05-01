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
    
     public List<WorkingTime> findWorkingTimeByDoctorIdAndDayName(Long id, String dayName) {
        return list(namedQuery("findAlWorkingHoursByDoctorIdAndDayOfTheWeek")
                .setParameter("idDoctor", id)
                .setParameter("dayOfTheWeek", dayName));
    }
}
