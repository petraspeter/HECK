package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;

/**
 *
 * @author Raven
 */
public class WorkingTimeDao extends AbstractDAO<WorkingTime>{
    
    public WorkingTimeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<WorkingTime> findByDoctorId(Long doctorId) {
        return list(currentSession().createCriteria(WorkingTime.class)
                .add(Restrictions.eq("doctor", doctorId)));
    }
    
   public List<WorkingTime> findByDoctorIdAndDay(Long doctorId, int dayName) {
        return list(currentSession().createCriteria(WorkingTime.class)
                .add(Restrictions.eq("doctor", doctorId))
                .add(Restrictions.eq("dayOfTheWeek", dayName)));
    }

    public WorkingTime create(WorkingTime workingTime) {
        return this.persist(workingTime);
    }
}
