package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import sk.upjs.ics.pro1a.heck.db.core.Appointment;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import sk.upjs.ics.pro1a.heck.db.core.Holiday;
import sk.upjs.ics.pro1a.heck.db.core.User;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDto;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentUserDto;
import sk.upjs.ics.pro1a.heck.utils.ServiceUtils;

/**
 *
 * @author Raven
 */
public class AppointmentDao extends AbstractDAO<Appointment> {
    
    private final SessionFactory sessionFactory;
    
    private final int NUMBER_OF_RETURNED_APPOINTMENTS = 30;
    
    public AppointmentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    
    public Appointment create(Appointment appointment) {
        return this.persist(appointment);
    }
    
    public Appointment update(Appointment appointment) {
        return this.persist(appointment);
    }
    
    public List<AppointmentDto> generateUserAppointmentForDay(Long idDoc, Long idUser, Timestamp date) {
        List<Appointment> appointments = generateBasicAppointmentForDay(idDoc, idUser, date);
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
    
    public List<AppointmentDto> generateDoctorAppointmentForDay(Long idDoc, Long idUser, Timestamp date) {
        List<Appointment> appointments = generateBasicAppointmentForDay(idDoc, idUser, date);
        List<Appointment> occupied = findOccupiedByDoctorIdAndDate(idDoc, date);
        if (occupied != null) {
            for (int i = 0; i < appointments.size(); i++) {
                for (Appointment occupiedAppointment : occupied) {
                    if(occupiedAppointment.getCanceledAppointment() || occupiedAppointment.getHolidayAppointment()
                            || occupiedAppointment.getOccupiedAppointment()) {
                        if(appointments.get(i).getDateFromAppointment().toString()
                                .equals(occupiedAppointment.getDateFromAppointment().toString())) {
                            appointments.get(i).setAppointmentUser(occupiedAppointment.getAppointmentUser());
                            appointments.get(i).setCanceledAppointment(occupiedAppointment.getCanceledAppointment());
                            appointments.get(i).setHolidayAppointment(occupiedAppointment.getHolidayAppointment());
                            appointments.get(i).setIdAppointment(occupiedAppointment.getIdAppointment());
                            appointments.get(i).setNoteAppointment(occupiedAppointment.getNoteAppointment());
                            appointments.get(i).setOccupiedAppointment(occupiedAppointment.getOccupiedAppointment());
                            appointments.get(i).setPatitentName(occupiedAppointment.getPatitentName());
                            appointments.get(i).setSubjectAppointment(occupiedAppointment.getPatitentName());
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
    
    public List<AppointmentDto> generateUserAppointmentForDays(Long idDoc, Long idUser, Timestamp from,
            Timestamp to) {
        List<Timestamp> timestamps = generateTimestamps(from, to);
        List<AppointmentDto> appointments = generateBasicAppointmentForDays(idDoc, idUser, timestamps);
        for (Timestamp timestamp : timestamps) {
            appointments.addAll(generateUserAppointmentForDay(idDoc, idUser, timestamp));
        }
        if (appointments.size() > NUMBER_OF_RETURNED_APPOINTMENTS) {
            return appointments.subList(0, NUMBER_OF_RETURNED_APPOINTMENTS);
        } else {
            return appointments;
        }
    }
    
    public List<AppointmentDto> generateDoctorAppointmentForDays(Long idDoc, Long idUser, Timestamp from,
            Timestamp to) {
        List<Timestamp> timestamps = generateTimestamps(from, to);
        List<AppointmentDto> appointments = generateBasicAppointmentForDays(idDoc, idUser, timestamps);
        for (Timestamp timestamp : timestamps) {
            appointments.addAll(generateDoctorAppointmentForDay(idDoc, idUser, timestamp));
        }
        if (appointments.size() > NUMBER_OF_RETURNED_APPOINTMENTS) {
            return appointments.subList(0, NUMBER_OF_RETURNED_APPOINTMENTS);
        } else {
            return appointments;
        }
    }
    
    public Appointment findAppointmentById(long id) {
        return get(id);
    }
    
    public List<WorkingTime> findWorkingTimeByDoctorIdAndDay(long id, int day) {
        return  sessionFactory.getCurrentSession().createCriteria(WorkingTime.class)
                .setFetchMode("doctor", FetchMode.JOIN)
                .add(Restrictions.eq("doctor.idDoctor", id))
                .add(Restrictions.eq("dayOfTheWeek", day))
                .list();
    }
    
    public List<Holiday> findHolidays() {
        return sessionFactory.getCurrentSession().createCriteria(Holiday.class)
                .list();
    }
    
    public Appointment findExactAppointmentByDocIdAndDate(long id, Timestamp date) {
        return (Appointment) sessionFactory.getCurrentSession().createCriteria(Appointment.class)
                .setFetchMode("appointmentDoctor", FetchMode.JOIN)
                .add(Restrictions.eq("appointmentDoctor.idDoctor", id))
                .add(Restrictions.eq("dateFromAppointment", date))
                .uniqueResult();
    }
    
    public List<Appointment> findOccupiedByDoctorIdAndDate(long id, Timestamp minDate) {
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
    
    public User findUserById(long id) {
        return (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("idUser", id)).uniqueResult();
    }
    
    public Doctor findDoctorById(long id) {
        return (Doctor) sessionFactory.getCurrentSession()
                .createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", id)).uniqueResult();
    }
    
    public Appointment generateAppointment(Long idDoc, Long idUser, Timestamp start, Timestamp end) {
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
    
    public List<Timestamp> generateTimestamps(Timestamp from, Timestamp to) {
        List<Timestamp> timestamps = new ArrayList<>();
        while (from.before(to)) {
            timestamps.add(from);
            from = new Timestamp((from.getTime() + ((24 * 60 * 60) * 1000))); // hours * minutes * seconds
        }
        timestamps.add(to);
        return timestamps;
    }
    
    public Appointment createAppointmentDaoFromDto(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        if (appointmentDto.getIdAppointment() != null) {
            appointment.setIdAppointment(appointmentDto.getIdAppointment());
        }
        appointment.setAppointmentDoctor(findDoctorById(appointmentDto.getAppointmentDoctor().getIdDoctor()));
        appointment.setAppointmentUser(findUserById(appointmentDto.getAppointmentUser().getIdUser()));
        appointment.setOccupiedAppointment(appointmentDto.getOccupiedAppointment());
        appointment.setDateFromAppointment(ServiceUtils.convertStringToTimestamp(appointmentDto.getDateFromAppointment()));
        System.out.println(appointmentDto.getDateToAppointment());
        
        appointment.setDateToAppointment(ServiceUtils.convertStringToTimestamp(appointmentDto.getDateToAppointment()));
        appointment.setHolidayAppointment(appointmentDto.getHolidayAppointment());
        appointment.setCanceledAppointment(appointmentDto.getCanceledAppointment());
        appointment.setPatitentName(appointmentDto.getPatitentName());
        appointment.setNoteAppointment(appointmentDto.getNoteAppointment());
        appointment.setSubjectAppointment(appointmentDto.getSubjectAppointment());
        return appointment;
    }
    
    public AppointmentDto createAppointmentDtoFromDao(Appointment appointment) {
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
    
    public AppointmentDoctorDto createAppointmentDoctorDto(Doctor doc) {
        return new AppointmentDoctorDto(
                doc.getIdDoctor(),
                doc.getFirstNameDoctor(),
                doc.getLastNameDoctor(),
                doc.getBusinessNameDoctor()
        );
    }
    
    public AppointmentUserDto createAppointmentUserDto(User user) {
        return new AppointmentUserDto(
                user.getIdUser(),
                user.getFirstNameUser(),
                user.getLastNameUser()
        );
    }
    
    
    private List<AppointmentDto> generateBasicAppointmentForDays(Long idDoc, Long idUser,
            List<Timestamp> timestamps) {
        List<Holiday> holidays = findHolidays();
        List<AppointmentDto> appointments = new ArrayList<>();
        for (int i = 0; i < timestamps.size(); i++) {
            for (Holiday holiday : holidays) {
                if(timestamps.get(i).equals(holiday.getDate())) {
                    timestamps.remove(timestamps.get(i));
                }
            }
        }
        return appointments;
    }
    
    private List<Appointment> generateBasicAppointmentForDay(Long idDoc, Long idUser, Timestamp date) {
        List<Appointment> appointments = new ArrayList<>();
        Doctor doc =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", idDoc)).uniqueResult();
        int period = doc.getAppointmentInterval();
        LocalDate jodaDate = new org.joda.time.LocalDate(date.getTime());
        /*
        den v tyzdni je v intervale 1-7, preco znizujem o 1
        */
        List<WorkingTime> docHours = findWorkingTimeByDoctorIdAndDay(idDoc, jodaDate.getDayOfWeek() - 1);
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
        return appointments;
    }
    
    public List<Appointment> findByDoctorId(long doctorId) {
        return list(currentSession().createCriteria(Appointment.class).add(Restrictions.eq("appointmentDoctor.idDoctor",doctorId)));
    }
    
    public List<Appointment> findForPage(int page, int pageSize) {
        int start = (page - 1) * pageSize;
        return list(currentSession().createCriteria(Appointment.class)
                .setFirstResult(start)
                .setMaxResults(start+pageSize));
    }
    
    public void deleteAppointment(Long id) {
        Appointment appointment = findAppointmentById(id);
        appointment.setAppointmentDoctor(null);
        appointment.setAppointmentUser(null);
        update(appointment);
        sessionFactory.getCurrentSession().delete(appointment);
    }
    
    public  Appointment findById(Long id) {
        return get(id);
    }
    
    public List<Appointment> findUserAppointment(Long id) {
        return sessionFactory.getCurrentSession().createCriteria(Appointment.class)
                .setFetchMode("appointmentUser", FetchMode.JOIN)
                .add(Restrictions.eq("appointmentUser.idUser", id))
                .list();
    }
    
    public List<Appointment> findFutureUserAppointment(Long id) {
        return sessionFactory.getCurrentSession().createCriteria(Appointment.class)
                .setFetchMode("appointmentUser", FetchMode.JOIN)
                .add(Restrictions.eq("appointmentUser.idUser", id))
                .add(Restrictions.ge("dateFromAppointment", new Timestamp(System.currentTimeMillis())))
                .list();
    }
    
}
