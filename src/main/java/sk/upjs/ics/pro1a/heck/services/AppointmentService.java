package sk.upjs.ics.pro1a.heck.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
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
    
    private final SessionFactory sessionFactory;
    
    private final int NUMBER_OF_RETURNED_APPOINTMENTS = 30;
    
    public AppointmentService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    private Appointment findExactAppointmentByDocIdAndDate(long id, Timestamp date) {
        return (Appointment) sessionFactory.getCurrentSession().createCriteria(Appointment.class)
                .setFetchMode("appointmentDoctor", FetchMode.JOIN)
                .add(Restrictions.eq("appointmentDoctor.idDoctor", id))
                .add(Restrictions.eq("dateFromAppointment", date))
                .uniqueResult();
    }
    
    public List<AppointmentDto> generateDoctorAppointmentForDay(Long idDoc, Long idUser, Timestamp date) {
        List<Appointment> appointments = new ArrayList<>();
        Doctor doc =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", idDoc)).uniqueResult();
        int period = doc.getAppointmentInterval();
        LocalDate jodaDate = new org.joda.time.LocalDate(date.getTime());
        List<WorkingTime> docHours = findWorkingTimeByDoctorIdAndDay(idDoc, jodaDate.getDayOfWeek());
        for (WorkingTime docHour : docHours) {
            /**
             * posun o jednu hodinu je 3600000 ms, na vstupe webservici primam format YYYY-MM-DD, ktory parsujem
             * cez simple date format tak ten sice berie do uvahy casove pasmo, ale nie casovy posun zimny/letny cas
             */
            Timestamp start = new Timestamp(docHour.getStartingHour().getTime()+date.getTime()+3600000);
            Timestamp end = new Timestamp((start.getTime() + ((period * 60) * 1000)));
            Timestamp ending = new Timestamp(docHour.getEndingHour().getTime()+date.getTime()+3600001);
            while (end.before(ending)) {    // aby sme vratili aj termin, ktory konci presne na konci pracovnej doby provnavame cas o 1ms neskor
                appointments.add(generateAppointment(idDoc, idUser, start, end));
                start = end;
                end = new Timestamp((start.getTime() + ((period * 60) * 1000)));    //convert period from minutes to miliseconds
            }
        }
        List<Appointment> occupied = findOccupiedByDoctorIdAndDate(idDoc, date);
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
        Appointment appointment = findExactAppointmentByDocIdAndDate(
                appointmentDto.getAppointmentDoctor().getIdDoctor(),
                new Timestamp(Long.parseLong(appointmentDto.getDateFromAppointment())));
        if(appointment.getOccupiedAppointment()) {
            return null;
        } else {
            appointment = createAppointmentDaoFromDto(appointmentDto);
            long id = (long) sessionFactory.getCurrentSession().save(appointment);
            return createAppointmentDtoFromDao(findAppointmentById(id));
        }
    }
    
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = findAppointmentById(appointmentDto.getIdAppointment());
        appointment.setCanceledAppointment(appointmentDto.getCanceledAppointment());
        appointment.setOccupiedAppointment(appointmentDto.getOccupiedAppointment());
        appointment.setHolidayAppointment(appointmentDto.getHolidayAppointment());
        sessionFactory.getCurrentSession().persist(appointment);
        return createAppointmentDtoFromDao(appointment);
    }
    
    /**
     * PRIVATE METHODS
     */
    private Appointment  findAppointmentById(long id) {
        return (Appointment) sessionFactory.getCurrentSession().createCriteria(Appointment.class)
                .add(Restrictions.eq("idAppointment", id))
                .uniqueResult();
    }
    
    private List<WorkingTime> findWorkingTimeByDoctorIdAndDay(long id, int day) {
        return  sessionFactory.getCurrentSession().createCriteria(WorkingTime.class)
                .setFetchMode("doctor", FetchMode.JOIN)
                .add(Restrictions.eq("doctor.idDoctor", id))
                .add(Restrictions.eq("dayOfTheWeek", day))
                .list();
    }
    
    private List<Appointment> findOccupiedByDoctorIdAndDate(long id, Timestamp minDate) {
        Timestamp maxDate = new Timestamp(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
        /*
        Restriction conjunction vyberie vsetky terminy, kde zaciatok terminu je vacsi alebo rovny minDate
        a zaroven je mensi ako maxDate
        */
        return sessionFactory.getCurrentSession().createCriteria(Appointment.class)
                .setFetchMode("appointmentDoctor", FetchMode.JOIN)
                .add(Restrictions.eq("appointmentDoctor.idDoctor", id))
                .add(Restrictions.conjunction()
                        .add(Restrictions.ge("dateFromAppointment", minDate))
                        .add(Restrictions.le("dateFromAppointment", maxDate)))
                .list();
    }
    
    private User findUserById(long id) {
        return (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("idUser", id)).uniqueResult();
    }
    
    private Doctor findDoctorById(long id) {
        return (Doctor) sessionFactory.getCurrentSession()
                .createCriteria(Doctor.class).add(Restrictions.eq("idDoctor", id)).uniqueResult();
    }
    
    private Appointment generateAppointment(Long idDoc, Long idUser, Timestamp start, Timestamp end) {
        Appointment appointment = new Appointment();
        appointment.setDateFromAppointment(start);
        appointment.setDateToAppointment(end);
        appointment.setAppointmentUser(findUserById(idUser));
        appointment.setAppointmentDoctor(findDoctorById(idDoc));
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
        Appointment appointment = findAppointmentById(appointmentDto.getIdAppointment());
        appointment.setAppointmentDoctor(findDoctorById(appointmentDto.getAppointmentDoctor().getIdDoctor()));
        appointment.setAppointmentUser(findUserById(appointmentDto.getAppointmentUser().getIdUser()));
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
