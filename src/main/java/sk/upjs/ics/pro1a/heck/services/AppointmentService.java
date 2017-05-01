package sk.upjs.ics.pro1a.heck.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sk.upjs.ics.pro1a.heck.db.AppointmentDao;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.db.WorkingTimeDao;
import sk.upjs.ics.pro1a.heck.db.core.Appointment;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import sk.upjs.ics.pro1a.heck.db.core.User;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDto;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentUserDto;

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
    
    public List<AppointmentDto> generateDoctorAppointmentForDay(Long idDoc, Long idUser, Timestamp date) {
        List<Appointment> appointments = new ArrayList<>();
        Doctor doc = doctorDao.findById(idDoc);
        int period = doc.getAppointmentInterval();
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date.getTime());
        String dayOfWeek = calendar.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.ENGLISH);
        List<WorkingTime> docHours = workingTimeDao.findWorkingTimeByDoctorIdAndDayName(idDoc,
                dayOfWeek);
        for (WorkingTime docHour : docHours) {
            Timestamp start = new Timestamp(docHour.getStartingHour().getTime());
            Timestamp end = new Timestamp((start.getTime() + ((period * 60) * 1000)));
            while (end.before(new Timestamp(docHour.getEndingHour().getTime()+1))) {    // aby sme vratili aj termin, ktory konci presne na konci pracovnej doby provnavame cas o 1ms neskor
                appointments.add(generateAppointment(idDoc, idUser, start, end));
                start = end;
                end = new Timestamp((start.getTime() + ((period * 60) * 1000)));    //convert period from minutes to miliseconds
            }
        }
        List<Appointment> occupied = appointmentDao.findOccupiedByDoctorIdAndDate(idDoc, date);
        if (occupied != null) {
            for (int i = 0; i < appointments.size(); i++) {
                for (Appointment occupiedAppointment : occupied) {
                    if(appointments.get(i).getDateFromAppointment().toString()
                            .equals(occupiedAppointment.getDateFromAppointment().toString())) {
                        appointments.remove(i);
                    }
                }
            }
        }
        List<AppointmentDto> appointmentsDto = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentsDto.add(createAppointmentDtoFromDao(appointment));
        }
        if (appointmentsDto.size() > NUMBER_OF_RETURNED_APPOINTMENTS) {
            return appointmentsDto.subList(0, NUMBER_OF_RETURNED_APPOINTMENTS);
        } else {
            return appointmentsDto;
        }
    }
    
    public List<AppointmentDto> generateDoctorAppointmentForDays(Long idDoc, Long idUser, Timestamp from,
            Timestamp to) {
        List<AppointmentDto> appointments = new ArrayList<>();
        List<Timestamp> timestamps = generateTimestamps(from, to);
        for (Timestamp timestamp : timestamps) {
            appointments.addAll(generateDoctorAppointmentForDay(idDoc, idUser, timestamp));
        }
        if (appointments.size() > NUMBER_OF_RETURNED_APPOINTMENTS) {
            return appointments.subList(0, NUMBER_OF_RETURNED_APPOINTMENTS);
        } else {
            return appointments;
        }
    }
    
    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentDao.findExactAppointmentByDocIdAndDate(
                appointmentDto.getAppointmentDoctor().getIdDoctor(),
                new Timestamp(Long.parseLong(appointmentDto.getDateFromAppointment())));
        if(appointment.getOccupiedAppointment()) {
            return null;
        } else {
            appointment = createAppointmentDaoFromDto(appointmentDto);
            appointmentDao.createAppointment(appointment);
            return appointmentDto;
        }
    }
    
    /**
     * PRIVATE METHODS
     */
    
    private Appointment generateAppointment(Long idDoc, Long idUser, Timestamp start, Timestamp end) {
        Appointment appointment = new Appointment();
        appointment.setDateFromAppointment(start);
        appointment.setDateToAppointment(end);
        appointment.setAppointmentUser(userDao.findById(idUser));
        appointment.setAppointmentDoctor(doctorDao.findById(idDoc));
        appointment.setCanceledAppointment(false);
        appointment.setHolidayAppointment(false);
        appointment.setOccupiedAppointment(false);
        return appointment;
    }
    
    private List<Timestamp> generateTimestamps(Timestamp from, Timestamp to) {
        List<Timestamp> timestamps = new ArrayList<>();
        while (from.before(to)) {
            timestamps.add(from);
            from = new Timestamp((from.getTime() + ((24 * 60 * 60) * 1000))); // hours * minutes * seconds
        }
        timestamps.add(to);
        return timestamps;
    }
    
    private Appointment createAppointmentDaoFromDto(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentDao.findAppointmentById(appointmentDto.getIdAppointment());
        appointment.setAppointmentDoctor(doctorDao.findById(appointmentDto.getAppointmentDoctor().getIdDoctor()));
        appointment.setAppointmentUser(userDao.findById(appointmentDto.getAppointmentUser().getIdUser()));
        appointment.setOccupiedAppointment(appointmentDto.getOccupiedAppointment());
        appointment.setDateFromAppointment(new Timestamp(Long.parseLong(appointmentDto.getDateFromAppointment())));
        appointment.setDateToAppointment(new Timestamp(Long.parseLong(appointmentDto.getDateToAppointment())));
        appointment.setHolidayAppointment(appointmentDto.getHolidayAppointment());
        appointment.setCanceledAppointment(appointmentDto.getCanceledAppointment());
        appointment.setPatitentName(appointmentDto.getPatitentName());
        appointment.setNoteAppointment(appointmentDto.getNoteAppointment());
        appointment.setSubjectAppointment(appointmentDto.getSubjectAppointment());
        return appointment;
    }
    
    private AppointmentDto createAppointmentDtoFromDao(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setIdAppointment(appointment.getIdAppointment());
        appointmentDto.setDateFromAppointment(appointment.getDateFromAppointment().toString());
        appointmentDto.setDateToAppointment(appointment.getDateToAppointment().toString());
        appointmentDto.setAppointmentDoctor(createAppointmentDoctorDto(appointment.getAppointmentDoctor()));
        appointmentDto.setAppointmentUser(createAppointmentUserDto(appointment.getAppointmentUser()));
        appointmentDto.setPatitentName(appointment.getPatitentName());
        appointmentDto.setNoteAppointment(appointment.getNoteAppointment());
        appointmentDto.setSubjectAppointment(appointment.getSubjectAppointment());
        appointmentDto.setOccupiedAppointment(appointment.getOccupiedAppointment());
        appointmentDto.setCanceledAppointment(appointment.getCanceledAppointment());
        appointmentDto.setHolidayAppointment(appointment.getHolidayAppointment());
        return appointmentDto;
    }
    
    private AppointmentDoctorDto createAppointmentDoctorDto(Doctor doc) {
        return new AppointmentDoctorDto(
                doc.getIdDoctor(),
                doc.getFirstNameDoctor(),
                doc.getLastNameDoctor(),
                doc.getBusinessNameDoctor()
        );
    }
    
    private AppointmentUserDto createAppointmentUserDto(User user) {
        return new AppointmentUserDto(
                user.getIdUser(),
                user.getFirstNameUser(),
                user.getLastNameUser()
        );
    }
    
}
