package sk.upjs.ics.pro1a.heck.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import sk.upjs.ics.pro1a.heck.db.AppointmentDao;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.db.WorkingTimeDao;
import sk.upjs.ics.pro1a.heck.db.core.Appointment;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDto;
import sk.upjs.ics.pro1a.heck.utils.ServiceUtils;

/**
 *
 * @author Raven
 */
public class AppointmentService {
    
    private final AppointmentDao appointmentDao;
    private final DoctorDao doctorDao;
    private final UserDao userDao;
    private final WorkingTimeDao workingTimeDao;
    private final byte[] tokenSecret;
    
    private final int NUMBER_OF_RETURNED_APPOINTMENTS = 30;
    
    public AppointmentService(AppointmentDao appointmentDao, DoctorDao appointmentDoctorDao,
            UserDao appointmentUserDao, WorkingTimeDao workingTimeDao, byte[] tokenSecret) {
        this.appointmentDao = appointmentDao;
        this.userDao = appointmentUserDao;
        this.doctorDao = appointmentDoctorDao;
        this.workingTimeDao = workingTimeDao;
        this.tokenSecret = tokenSecret;
    }
    
    public List<AppointmentDto> getUserAppointment(Long id) {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointmentDao.findUserAppointment(id)) {
            AppointmentDto appointmentDto = appointmentDao.createAppointmentDtoFromDao(appointment);
            appointmentDtos.add(appointmentDto);
        }
        return appointmentDtos;
    }
    
    public List<AppointmentDto> getFutureUserAppointment(Long id) {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointmentDao.findFutureUserAppointment(id)) {
            AppointmentDto appointmentDto = appointmentDao.createAppointmentDtoFromDao(appointment);
            appointmentDtos.add(appointmentDto);
        }
        return appointmentDtos;
        
    }
    
    public List<AppointmentDto> findForPage(int page, int pageSize) {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointmentDao.findForPage(page, pageSize)) {
            AppointmentDto appointmentDto = appointmentDao.createAppointmentDtoFromDao(appointment);
            appointmentDtos.add(appointmentDto);
        }
        return appointmentDtos;
    }
    
    public List<AppointmentDto> generateUserAppointmentForDays(Long idDoc, Long idUser, Timestamp from,
            Timestamp to) {
        return appointmentDao.generateUserAppointmentForDays(idDoc, idUser, from, to);
    }
    
    public List<AppointmentDto> generateDoctorAppointmentForDays(Long idDoc, Timestamp from,
            Timestamp to) {
        return appointmentDao.generateDoctorAppointmentForDays(idDoc, from, to);
    }
    
    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentDao.findExactAppointmentByDocIdAndDate(
                appointmentDto.getAppointmentDoctor().getIdDoctor(),
                ServiceUtils.convertStringToTimestamp(appointmentDto.getDateFromAppointment()));
        if(appointment != null) {
            return null;
        } else {
            appointment = appointmentDao.createAppointmentDaoFromDto(appointmentDto);
            long id = appointmentDao.create(appointment).getIdAppointment();
            return appointmentDao.createAppointmentDtoFromDao(appointmentDao.findAppointmentById(id));
        }
    }
    
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentDao.findAppointmentById(appointmentDto.getIdAppointment());
        if(appointmentDto.getCanceledAppointment() != null) appointment
                .setCanceledAppointment(appointmentDto.getCanceledAppointment());
        if(appointmentDto.getOccupiedAppointment() != null) appointment
                .setOccupiedAppointment(appointmentDto.getOccupiedAppointment());
        if(appointmentDto.getHolidayAppointment() != null) appointment
                .setHolidayAppointment(appointmentDto.getHolidayAppointment());
        appointmentDao.update(appointment);
        return appointmentDao.createAppointmentDtoFromDao(appointment);
    }
    
    public void deleteAppointment(Long id) {
        appointmentDao.deleteAppointment(id);
    }
    
    public  Appointment getById(Long id) {
        return appointmentDao.findById(id);
    }
    
    public List<AppointmentDto> generateDefaultAppointments(Long idDoc, Long idUser) {
        return  appointmentDao.generateDefaultAppointments(idDoc, idUser);
    }
    
}
