package sk.upjs.ics.pro1a.heck.services;

import sk.upjs.ics.pro1a.heck.db.WorkingTimeDao;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;
import sk.upjs.ics.pro1a.heck.services.dto.*;
import sk.upjs.ics.pro1a.heck.utils.Tokenizer;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jose4j.jwt.NumericDate;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;
import sk.upjs.ics.pro1a.heck.utils.PasswordManager;

/**
 *
 * @author Raven
 */
public class DoctorService {
    
    private DoctorDao doctorDao;
    private final byte[] tokenSecret;
    private Tokenizer tokenizer;
    private final SessionFactory sessionFactory;
    private final WorkingTimeDao workingTimeDao;
    
    public DoctorService(DoctorDao doctorDao, WorkingTimeDao workingTimeDao, byte[] tokenSecret,
            SessionFactory sessionFactory) {
        this.doctorDao = doctorDao;
        this.tokenSecret = tokenSecret;
        tokenizer = new Tokenizer(tokenSecret);
        this.sessionFactory = sessionFactory;
        this.workingTimeDao = workingTimeDao;
    }
    
    /*
    GET methods
    */
    public List<DoctorDto> getAllDoctors() {
        List<DoctorDto> doctors = new ArrayList<>();
        List<Doctor> iterate = sessionFactory.getCurrentSession().createCriteria(Doctor.class).list();
        for (Doctor doctor :iterate) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        
        return doctors;
    }
    
    public DoctorDto getDoctorById(long id) {
        Doctor doctor =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", id)).uniqueResult();
        if (doctor != null) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithPassword(doctor);
            return doctorDto;
        }
        return null;
    }
    
    public DoctorDto getDoctorByLogin(String login) {
        Doctor doctor = (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("loginDoctor", login)).uniqueResult();
        if (doctor != null) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithPassword(doctor);
            return doctorDto;
        }
        return null;
    }
    
    
    public List<DoctorDto> getDoctorsBySpecializationId(Long id) {
        List<DoctorDto> doctorsDto = new ArrayList<>();
        List<Doctor> doctors =  sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id)).list();
        for (Doctor doctor : doctors) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctorsDto.add(doctorDto);
        }
        return doctorsDto;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndCity(Long id, String city) {
        List<DoctorDto> doctors = new ArrayList<>();
        List<Doctor> iterate = sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id))
                .add(Restrictions.like("cityDoctor", city))
                .list();
        for (Doctor doctor : iterate) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndName(Long id, String firstname, String lastName) {
        List<DoctorDto> doctors = new ArrayList<>();
        List<Doctor> iterate = sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id))
                .add(Restrictions.like("firstNameDoctor", firstname))
                .add(Restrictions.like("lastNameDoctor", lastName))
                .list();
        for (Doctor doctor : iterate) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndNameAndCity(Long id, String firstname,
            String lastName, String city) {
        List<DoctorDto> doctors = new ArrayList<>();
        List<Doctor> iterate = sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id))
                .add(Restrictions.like("firstNameDoctor", firstname))
                .add(Restrictions.like("lastNameDoctor", lastName))
                .add(Restrictions.like("cityDoctor", city))
                .list();
        for (Doctor doctor : iterate) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndLastName(Long id, String lastName) {
        List<DoctorDto> doctors = new ArrayList<>();
        List<Doctor> iterate = sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id))
                .add(Restrictions.like("lastNameDoctor", lastName))
                .list();
        for (Doctor doctor : iterate) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndLastNameAndCity(Long id, String lastName,
            String city) {
        List<DoctorDto> doctors = new ArrayList<>();
        List<Doctor> iterate = sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .setFetchMode("specializationDoctor", FetchMode.JOIN)
                .createAlias("specializationDoctor", "sd")
                .add(Restrictions.eq("sd.id", id))
                .add(Restrictions.like("lastNameDoctor", lastName))
                .add(Restrictions.like("cityDoctor", city))
                .list();
        for (Doctor doctor : iterate) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    /*
    Login and registration methods
    */
    public LoginResponseDto loginAsDoctor(String login, String password) {
        Doctor doctor = (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("loginDoctor", login)).uniqueResult();
        if (doctor != null) {
            if (doctor.getPasswordDoctor().equals(PasswordManager.encryptPassword(doctor.getSaltDoctor(),
                    password))) {
                LoginResponseDto loginResponse = new LoginResponseDto();
                loginResponse.setId(doctor.getIdDoctor());
                loginResponse.setLogin(doctor.getLoginDoctor());
                loginResponse.setRole("doctor");
                loginResponse.setToken(tokenizer.generateToken(login, loginResponse.getRole()));
                return loginResponse;
            }
        }
        return null;
    }
    
    public LoginResponseDto registerDoctor(DoctorDto doctorDto) {
        if (doctorDto.getPassword() == null || doctorDto.getPassword().length() < 6) {
            throw new IllegalStateException("Password does not match criteria.");
        }
        String salt = new BigInteger(130, new SecureRandom()).toString(32);
        String password = PasswordManager.encryptPassword(salt, doctorDto.getPassword());
        Specialization specialization =  (Specialization) sessionFactory.getCurrentSession().createCriteria(Specialization.class)
                .add(Restrictions.eq("id", doctorDto.getSpecialization())).uniqueResult();
        Doctor doctor = createDoctorDaoFromDoctorDto(doctorDto, password, salt, specialization);
        doctor = doctorDao.createDoctor(doctor);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(doctor.getIdDoctor());
        loginResponse.setLogin(doctor.getLoginDoctor());
        loginResponse.setRole("doctor");
        loginResponse.setToken(tokenizer.generateToken(doctor.getLoginDoctor(), "doctor"));
        return loginResponse;
    }
    
    /*
    Doctor update methods
    */
    public void updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor doctor =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", id)).uniqueResult();
        doctor.setFirstNameDoctor(doctorDto.getFirstName());
        doctor.setLastNameDoctor(doctorDto.getLastName());
        doctor.setEmailDoctor(doctorDto.getEmail());
        doctor.setBusinessNameDoctor(doctorDto.getOffice());
        doctor.setAddressDoctor(doctorDto.getAddress());
        doctor.setCityDoctor(doctorDto.getCity());
        doctor.setPostalCodeDoctor(doctorDto.getPostalCode());
        doctor.setPhoneNumberDoctor(doctorDto.getPhoneNumber());
        if(!doctor.getSpecializationDoctor().getId().equals(doctorDto.getSpecialization())){
            doctor.setSpecializationDoctor((Specialization) sessionFactory.getCurrentSession().createCriteria(Specialization.class)
                    .add(Restrictions.eq("id", doctorDto.getSpecialization())).uniqueResult());
        }
        doctorDao.update(doctor);
    }
    
    public IsValidDto isLoginValid(String login) {
        IsValidDto isValidDto = new IsValidDto();
        Doctor doctor = (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("loginDoctor", login)).uniqueResult();
        if (doctor== null) {
            isValidDto.setValid(true);
        } else {
            isValidDto.setValid(false);
        }
        return isValidDto;
    }
    
    public IsValidDto isEmailValid(String email, String userEmail) {
        IsValidDto isValidDto = new IsValidDto();
        Doctor doctor = (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("emailDoctor", email)).uniqueResult();
        if (email.equals(userEmail) || doctor == null) {
            isValidDto.setValid(true);
        } else {
            isValidDto.setValid(false);
        }
        return isValidDto;
    }
    
    public void changeDoctorPassword(Long id, ChangePasswordDto changePasswordDto) {
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            Doctor doctor =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                    .add(Restrictions.eq("idDoctor", id)).uniqueResult();
            if (doctor != null) {
                if (doctor.getPasswordDoctor().equals(PasswordManager.encryptPassword(doctor.getSaltDoctor(), changePasswordDto.getPassword()))) {
                    doctor.setPasswordDoctor(PasswordManager.encryptPassword(doctor.getSaltDoctor(), changePasswordDto.getNewPassword()));
                    doctorDao.update(doctor);
                    return;
                }
            }
        }
        throw new IllegalStateException("Change password DTO is not valid.");
    }
    
    public IsValidDto checkDoctorPassword(Long id, String password) {
        IsValidDto isValidDto = new IsValidDto();
        Doctor doctor =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", id)).uniqueResult();
        if (doctor != null) {
            if (doctor.getPasswordDoctor().equals(PasswordManager.encryptPassword(doctor.getSaltDoctor(), password))) {
                isValidDto.setValid(true);
                return isValidDto;
            }
        }
        isValidDto.setValid(false);
        return isValidDto;
    }
    
    /*
    Update token method
    */
    public LoginResponseDto updateDoctorsToken(String login, String role, Long actualExpirationTime) {
        Doctor doctor = (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("loginDoctor", login)).uniqueResult();
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(doctor.getIdDoctor());
        loginResponse.setLogin(doctor.getLoginDoctor());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(tokenizer.updateToken(login, role, expiration));
        return loginResponse;
    }
    
    public WorkingTimeDto getDoctorWorkingTime(long id) {
           List<WorkingTime> workingTimes = sessionFactory.getCurrentSession().createCriteria(WorkingTime.class)
                .add(Restrictions.eq("id", id))
                .list();
        if(workingTimes.size()==0) {
            return null;
        }
        WorkingTimeDto workingTimeDto = new WorkingTimeDto();
        workingTimeDto.setInterval(workingTimes.get(0).getDoctor().getAppointmentInterval());
        
        ArrayList<WorkingDayDto> workingDaysDto = new ArrayList<>();
        for(WorkingTime workingTime: workingTimes){
            WorkingDayDto workingDayDto = new WorkingDayDto();
            workingDayDto.setDay(workingTime.getDayOfTheWeek());
            workingDayDto.setEnd(workingTime.getStartingHour().toString());
            workingDayDto.setStart(workingTime.getEndingHour().toString());
            workingDaysDto.add(workingDayDto);
        }
        workingTimeDto.setWorkingTimes(workingDaysDto);
        
        return workingTimeDto;
    }
    
    public void createDoctorWorkingTime(long doctorId, WorkingTimeDto workingTimeDto) {
        Doctor doctor =  (Doctor) sessionFactory.getCurrentSession().createCriteria(Doctor.class)
                .add(Restrictions.eq("idDoctor", doctorId)).uniqueResult();
        doctor.setAppointmentInterval(workingTimeDto.getInterval());
        
        for (WorkingDayDto day : workingTimeDto.getWorkingTimes()) {
            WorkingTime workingTime = new WorkingTime();
            workingTime.setDayOfTheWeek(day.getDay());
            workingTime.setDoctor(doctor);
            //TODO: implement validation for String for end and start time
            workingTime.setEndingHour(Time.valueOf(day.getEnd() + ":00"));
            workingTime.setStartingHour(Time.valueOf(day.getStart() + ":00"));
            workingTimeDao.createWorkingTime(workingTime);
        }
    }
    
    /*
    Methods for convert between DTO and DAO
    */
    private Doctor createDoctorDaoFromDoctorDto(DoctorDto doctorDto, String password, String salt,
            Specialization specialization) {
        return new Doctor(
                doctorDto.getEmail(),
                doctorDto.getLogin(),
                password,
                salt,
                specialization,
                doctorDto.getOffice(),
                doctorDto.getFirstName(),
                doctorDto.getLastName(),
                doctorDto.getPhoneNumber(),
                doctorDto.getPostalCode(),
                doctorDto.getCity(),
                doctorDto.getAddress(),
                doctorDto.getInterval(),
                doctorDto.getRegistrationTime()
        );
    }
    
    private DoctorDto createDoctorDtoFromDoctorDaoWithoutPassword(Doctor doctor) {
        return new DoctorDto(
                doctor.getIdDoctor(),
                doctor.getLoginDoctor(),
                doctor.getFirstNameDoctor(),
                doctor.getLastNameDoctor(),
                doctor.getEmailDoctor(),
                doctor.getBusinessNameDoctor(),
                doctor.getAddressDoctor(),
                doctor.getPostalCodeDoctor(),
                doctor.getCityDoctor(),
                doctor.getPhoneNumberDoctor(),
                doctor.getSpecializationDoctor().getId(),
                doctor.getAppointmentInterval(),
                doctor.getRegistrationTime()
        );
    }
    
    private DoctorDto createDoctorDtoFromDoctorDaoWithPassword(Doctor doctor) {
        return new DoctorDto(
                doctor.getIdDoctor(),
                doctor.getLoginDoctor(),
                doctor.getPasswordDoctor(),
                doctor.getFirstNameDoctor(),
                doctor.getLastNameDoctor(),
                doctor.getEmailDoctor(),
                doctor.getBusinessNameDoctor(),
                doctor.getAddressDoctor(),
                doctor.getPostalCodeDoctor(),
                doctor.getCityDoctor(),
                doctor.getPhoneNumberDoctor(),
                doctor.getSpecializationDoctor().getId(),
                doctor.getAppointmentInterval(),
                doctor.getRegistrationTime()
        );
    }
    
}
