package sk.upjs.ics.pro1a.heck.services;

import java.util.ArrayList;
import java.util.List;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;
import sk.upjs.ics.pro1a.heck.services.dto.SpecializationDto;

/**
 *
 * @author Raven
 */
public class SpecializationService {

    private SpecializationDao specializationDao;

    public SpecializationService(SpecializationDao specializationDao) {
        this.specializationDao = specializationDao;
    }

    /*
    GET methods
     */
    public List<SpecializationDto> getAllSpecializations() {
        List<SpecializationDto> specializations = new ArrayList<>();
        for (Specialization s : specializationDao.findAll()) {
            SpecializationDto specializationDto = new SpecializationDto(s.getId(), s.getSpecializationName());
            specializations.add(specializationDto);
        }
        return specializations;
    }

    public SpecializationDto getSpecializationById(long id) {
        Specialization specialization = specializationDao.findById(id);
        if (specialization != null) {
            return new SpecializationDto(specialization.getId(), specialization.getSpecializationName());
        }
        return null;
    }

}
