package sk.upjs.ics.pro1a.heck.services;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;
import sk.upjs.ics.pro1a.heck.services.dto.SpecializationDto;

/**
 *
 * @author Raven
 */
public class SpecializationService {
    
    private final SessionFactory sessionFactory;
    
    public SpecializationService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    /*
    GET methods
    */
    public List<SpecializationDto> getAllSpecializations() {
        List<SpecializationDto> specializations = new ArrayList<>();
        List<Specialization> list = sessionFactory.getCurrentSession().createCriteria(Specialization.class).list();
        for (Specialization specialization : list) {
            SpecializationDto specializationDto = new SpecializationDto(specialization.getId(),
                    specialization.getSpecializationName());
            specializations.add(specializationDto);
        }
        return specializations;
    }
    
    public SpecializationDto getSpecializationById(long id) {
        Specialization specialization = (Specialization) sessionFactory.getCurrentSession()
                .createCriteria(Specialization.class).add(Restrictions.eq("idDoctor", id)).uniqueResult();
        if (specialization != null) {
            return new SpecializationDto(specialization.getId(), specialization.getSpecializationName());
        }
        return null;
    }
    
}
