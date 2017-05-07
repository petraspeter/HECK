package sk.upjs.ics.pro1a.heck.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import sk.upjs.ics.pro1a.heck.db.AppointmentDao;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.db.WorkingTimeDao;
import sk.upjs.ics.pro1a.heck.db.core.Appointment;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDto;

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
    
    public List<AppointmentDto> generateDoctorAppointmentForDays(Long idDoc, Long idUser, Timestamp from,
            Timestamp to) {
        return appointmentDao.generateDoctorAppointmentForDays(idDoc, idUser, from, to);
    }
    
    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentDao.findExactAppointmentByDocIdAndDate(
                appointmentDto.getAppointmentDoctor().getIdDoctor(),
                new Timestamp(Long.parseLong(appointmentDto.getDateFromAppointment())));
        if(appointment.getOccupiedAppointment()) {
            return null;
        } else {
            appointment = appointmentDao.createAppointmentDaoFromDto(appointmentDto);
            long id = appointmentDao.create(appointment).getIdAppointment();
            return appointmentDao.createAppointmentDtoFromDao(appointmentDao.findAppointmentById(id));
        }
    }
    
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentDao.findAppointmentById(appointmentDto.getIdAppointment());
        appointment.setCanceledAppointment(appointmentDto.getCanceledAppointment());
        appointment.setOccupiedAppointment(appointmentDto.getOccupiedAppointment());
        appointment.setHolidayAppointment(appointmentDto.getHolidayAppointment());
        appointmentDao.update(appointment);
        return appointmentDao.createAppointmentDtoFromDao(appointment);
    }
    
    private List<AppointmentDto> generateDoctorAppointmentForDay(Long idDoc, Long idUser, Timestamp date) {
        List<Appointment> appointments = new ArrayList<>();
        Doctor doc =  doctorDao.findById(idDoc);
        int period = doc.getAppointmentInterval();
        LocalDate jodaDate = new org.joda.time.LocalDate(date.getTime());
        /*
        den v tyzdni je v intervale 1-7, preco znizujem o 1
        */
        List<WorkingTime> docHours = appointmentDao.findWorkingTimeByDoctorIdAndDay(idDoc,
                jodaDate.getDayOfWeek() - 1);
        for (WorkingTime docHour : docHours) {
            /**
             * posun o jednu hodinu je 3600000 ms, na vstupe webservici primam format YYYY-MM-DD, ktory parsujem
             * cez simple date format tak ten sice berie do uvahy casove pasmo, ale nie casovy posun zimny/letny cas
             */
            Timestamp start = new Timestamp(docHour.getStartingHour().getTime()+date.getTime()+3600000);
            Timestamp end = new Timestamp((start.getTime() + ((period * 60) * 1000)));
            Timestamp ending = new Timestamp(docHour.getEndingHour().getTime()+date.getTime()+3600001);
            while (end.before(ending)) {    // aby sme vratili aj termin, ktory konci presne na konci pracovnej doby provnavame cas o 1ms neskor
                appointments.add(appointmentDao.generateAppointment(idDoc, idUser, start, end));
                start = end;
                end = new Timestamp((start.getTime() + ((period * 60) * 1000)));    //convert period from minutes to miliseconds
            }
        }
        List<Appointment> occupied = appointmentDao.findOccupiedByDoctorIdAndDate(idDoc, date);
        if (occupied != null) {
            for (int i = 0; i < appointments.size(); i++) {
                for (Appointment occupiedAppointment : occupied) {
                    if(occupiedAppointment.getCanceledAppointment() || occupiedAppointment.getHolidayAppointment()
                            || occupiedAppointment.getOccupiedAppointment()) {
                        if(appointments.get(i).getDateFromAppointment().toString()
                                .equals(occupiedAppointment.getDateFromAppointment().toString())) {
                            appointments.remove(i);
                        }
                    }
                }
            }
        }
        List<AppointmentDto> appointmentsDto = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentsDto.add(appointmentDao.createAppointmentDtoFromDao(appointment));
        }
        if (appointmentsDto.size() > NUMBER_OF_RETURNED_APPOINTMENTS) {
            return appointmentsDto.subList(0, NUMBER_OF_RETURNED_APPOINTMENTS);
        } else {
            return appointmentsDto;
        }
    }
    
}
