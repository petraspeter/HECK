package sk.upjs.ics.pro1a.heck.services;

import sk.upjs.ics.pro1a.heck.db.core.Appointment;
import sk.upjs.ics.pro1a.heck.utils.ServiceUtils;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;
import sk.upjs.ics.pro1a.heck.services.dto.*;
import sk.upjs.ics.pro1a.heck.utils.Tokenizer;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.jose4j.jwt.NumericDate;
import sk.upjs.ics.pro1a.heck.db.AppointmentDao;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.WorkingTimeDao;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;
import sk.upjs.ics.pro1a.heck.utils.PasswordManager;

/**
 *
 * @author Raven
 */
public class DoctorService {
    
    private DoctorDao doctorDao;
    private SpecializationDao specializationDao;
    private WorkingTimeDao workingTimeDao;
    private AppointmentDao appointmentDao;
    private final byte[] tokenSecret;
    private Tokenizer tokenizer;
    
    public DoctorService(DoctorDao doctorDao, SpecializationDao specializationDao,
            WorkingTimeDao workingTimeDao, AppointmentDao appointmentDao, byte[] tokenSecret) {
        this.doctorDao = doctorDao;
        this.specializationDao = specializationDao;
        this.tokenSecret = tokenSecret;
        this.workingTimeDao = workingTimeDao;
        this.appointmentDao = appointmentDao;
        tokenizer = new Tokenizer(tokenSecret);
    }
    
    /*
    GET methods
    */
    public List<DoctorDto> getAllDoctors() {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findAll()) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getAllDoctorsForPage(int page, int pageSize) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findForPage(page, pageSize)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public DoctorDto getDoctorById(long id) {
        Doctor doctor = doctorDao.findById(id);
        if (doctor != null) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithPassword(doctor);
            return doctorDto;
        }
        return null;
    }
    
    public DoctorDto getDoctorByLogin(String login) {
        Doctor doctor = doctorDao.findByLogin(login);
        if (doctor != null) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithPassword(doctor);
            return doctorDto;
        }
        return null;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationId(Long id) {
        List<DoctorDto> doctorsDto = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationId(id)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctorsDto.add(doctorDto);
        }
        return doctorsDto;
    }
    
    public List<DoctorDto> getDoctorsBySpecialization(String specialization) {
        List<DoctorDto> doctorsDto = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecialization(specialization)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctorsDto.add(doctorDto);
        }
        return doctorsDto;
    }
    
    
    public List<DoctorDto> getDoctorsBySpecializationAndDate(String specialization, Timestamp from,
            Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecialization(specialization)) {
            List<AppointmentDto> appointments = appointmentDao.generateUserAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndCityAndDate(String specialization, String city, Timestamp from,
            Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndCity(specialization, city)) {
            List<AppointmentDto> appointments = appointmentDao.generateUserAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndFullNameAndDate(String specialization, String firstname,
            String lastName, Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndName(specialization, firstname, lastName)) {
            List<AppointmentDto> appointments = appointmentDao
                    .generateUserAppointmentForDays(doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndFullNameAndCityAndDate(String specialization, String firstname,
            String lastName, String city, Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndNameAndCity(specialization, firstname, lastName, city)) {
            List<AppointmentDto> appointments = appointmentDao.generateUserAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndLastNameAndDate(String specialization, String lastName,
            Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndLastName(specialization, lastName)) {
            List<AppointmentDto> appointments = appointmentDao.generateUserAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndLastName(String specialization, String lastName) {
        List<DoctorDto> doctorsDto = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndLastName(specialization, lastName)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctorsDto.add(doctorDto);
        }
        return doctorsDto;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndLastNameAndCityAndDate(String specialization, String lastName,
            String city, Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndLastNameAndCity(specialization, lastName, city)) {
            List<AppointmentDto> appointments = appointmentDao.generateUserAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }
    
    /*
    Login and registration methods
    */
    public LoginResponseDto loginAsDoctor(String login, String password) {
        Doctor doctor = doctorDao.findByLogin(login);
        if (doctor != null) {
            if (doctor.getPasswordDoctor().equals(PasswordManager.encryptPassword(doctor.getSaltDoctor(),
                    password))) {
                LoginResponseDto loginResponse = new LoginResponseDto();
                loginResponse.setId(doctor.getIdDoctor());
                loginResponse.setLogin(doctor.getLoginDoctor());
                loginResponse.setRole("doctor");
                loginResponse.setEmail(doctor.getEmailDoctor());
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
        Specialization specialization = specializationDao.findById(doctorDto.getSpecialization());
        doctorDto.setActive(true);
        Doctor doctor = createDoctorDaoFromDoctorDto(doctorDto, password, salt, specialization);
        doctor.setRegistrationTime(new Timestamp(System.currentTimeMillis()));
        doctor.setActivationTimeDoctor(new Timestamp(System.currentTimeMillis()));
        doctor = doctorDao.create(doctor);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(doctor.getIdDoctor());
        loginResponse.setLogin(doctor.getLoginDoctor());
        loginResponse.setRole("doctor");
        loginResponse.setEmail(doctor.getEmailDoctor());
        loginResponse.setToken(tokenizer.generateToken(doctor.getLoginDoctor(), "doctor"));
        return loginResponse;
    }
    
    /*
    Doctor update methods
    */
    public void updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor doctor = doctorDao.findById(id);
        if (doctorDto.getFirstName() != null) {
            doctor.setFirstNameDoctor(doctorDto.getFirstName());
        }
        if (doctorDto.getLastName() != null) {
            doctor.setLastNameDoctor(doctorDto.getLastName());
        }
        if (doctorDto.getEmail() != null) {
            doctor.setEmailDoctor(doctorDto.getEmail());
        }
        if (doctorDto.getOffice() != null) {
            doctor.setBusinessNameDoctor(doctorDto.getOffice());
        }
        if (doctorDto.getAddress() != null) {
            doctor.setAddressDoctor(doctorDto.getAddress());
        }
        if (doctorDto.getCity() != null) {
            doctor.setCityDoctor(doctorDto.getCity());
        }
        if (doctorDto.getPostalCode() != null) {
            doctor.setPostalCodeDoctor(doctorDto.getPostalCode());
        }
        if (doctorDto.getPhoneNumber() != null) {
            doctor.setPhoneNumberDoctor(doctorDto.getPhoneNumber());
        }
        if (doctorDto.getActive() != null) {
            doctor.setActiveDoctor(doctorDto.getActive());
        }
        if (doctorDto.getSpecialization() != null) {
            if (!doctor.getSpecializationDoctor().getId().equals(doctorDto.getSpecialization())) {
                doctor.setSpecializationDoctor(specializationDao.findById(doctorDto.getSpecialization()));
            }
        }
        doctorDao.update(doctor);
    }
    
    public IsValidDto isLoginValid(String login) {
        IsValidDto isValidDto = new IsValidDto();
        if (doctorDao.findByLogin(login) == null) {
            isValidDto.setValid(true);
        } else {
            isValidDto.setValid(false);
        }
        return isValidDto;
    }
    
    public IsValidDto isEmailValid(String email, String userEmail) {
        IsValidDto isValidDto = new IsValidDto();
        if (email.equals(userEmail) || doctorDao.findByEmail(email) == null) {
            isValidDto.setValid(true);
        } else {
            isValidDto.setValid(false);
        }
        return isValidDto;
    }
    
    public void changeDoctorPassword(Long id, ChangePasswordDto changePasswordDto) {
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            Doctor doctor = doctorDao.findById(id);
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
        Doctor doctor = doctorDao.findById(id);
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
        Doctor doctor = doctorDao.findByLogin(login);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(doctor.getIdDoctor());
        loginResponse.setLogin(doctor.getLoginDoctor());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(tokenizer.updateToken(login, role, expiration));
        return loginResponse;
    }
    
    public WorkingTimeDto getDoctorWorkingTime(long id) {
        List<WorkingTime> workingTimes = workingTimeDao.findByDoctorId(id);
        WorkingTimeDto workingTimeDto = new WorkingTimeDto();
        if (workingTimes.isEmpty()) {
            return workingTimeDto;
        }
        workingTimeDto.setInterval(workingTimes.get(0).getDoctor().getAppointmentInterval());
        
        ArrayList<WorkingDayDto> workingDaysDto = new ArrayList<>();
        for (WorkingTime workingTime : workingTimes) {
            WorkingDayDto workingDayDto = new WorkingDayDto();
            workingDayDto.setDay(workingTime.getDayOfTheWeek());
            workingDayDto.setEnd(workingTime.getEndingHour().toString());
            workingDayDto.setStart(workingTime.getStartingHour().toString());
            workingDaysDto.add(workingDayDto);
        }
        workingTimeDto.setWorkingTimes(workingDaysDto);
        
        return workingTimeDto;
    }
    
    public void createDoctorWorkingTime(long doctorId, WorkingTimeDto workingTimeDto) {
        Doctor doctor = doctorDao.findById(doctorId);
        doctor.setAppointmentInterval(workingTimeDto.getInterval());
        
        for (WorkingDayDto day : workingTimeDto.getWorkingTimes()) {
            WorkingTime workingTime = new WorkingTime();
            workingTime.setDayOfTheWeek(day.getDay());
            workingTime.setDoctor(doctor);
            //TODO: implement validation for String for end and start time
            workingTime.setEndingHour(Time.valueOf(day.getEnd() + ":00"));
            workingTime.setStartingHour(Time.valueOf(day.getStart() + ":00"));
            workingTimeDao.create(workingTime);
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
                null,
                doctorDto.getActive());
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
                doctor.getSpecializationDoctor().getSpecializationName(),
                doctor.getAppointmentInterval(),
                doctor.getActiveDoctor(),
                ServiceUtils.convertTimestampToString(doctor.getRegistrationTime())
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
                doctor.getSpecializationDoctor().getSpecializationName(),
                doctor.getAppointmentInterval(),
                doctor.getActiveDoctor(),
                ServiceUtils.convertTimestampToString(doctor.getRegistrationTime())
        );
    }
    
    public List<AppointmentDto> getDoctorAppointments(long id) {
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        for (Appointment app : appointmentDao.findByDoctorId(id)) {
            appointmentDtoList.add(createAppointmentDtoFromAppointmentDao(app));
        }
        return appointmentDtoList;
    }
    
    private AppointmentDto createAppointmentDtoFromAppointmentDao(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setAppointmentDoctor(new AppointmentDoctorDto(
                appointment.getAppointmentDoctor().getIdDoctor(),
                appointment.getAppointmentDoctor().getFirstNameDoctor(),
                appointment.getAppointmentDoctor().getLastNameDoctor(),
                appointment.getAppointmentDoctor().getBusinessNameDoctor(),
                appointment.getAppointmentDoctor().getSpecializationDoctor().getSpecializationName()
        ));
        appointmentDto.setAppointmentUser(new AppointmentUserDto(
                appointment.getAppointmentUser().getIdUser(),
                appointment.getAppointmentUser().getFirstNameUser(),
                appointment.getAppointmentUser().getLastNameUser()
        ));
        appointmentDto.setCanceledAppointment(appointment.getCanceledAppointment());
        appointmentDto.setDateFromAppointment(ServiceUtils
                .convertTimestampToString(appointment.getDateFromAppointment()));
        appointmentDto.setDateToAppointment(ServiceUtils
                .convertTimestampToString(appointment.getDateToAppointment()));
        appointmentDto.setHolidayAppointment(appointment.getHolidayAppointment());
        appointmentDto.setIdAppointment(appointment.getIdAppointment());
        appointmentDto.setNoteAppointment(appointment.getNoteAppointment());
        appointmentDto.setOccupiedAppointment(appointment.getOccupiedAppointment());
        appointmentDto.setPatitentName(appointment.getPatitentName());
        appointmentDto.setCanceledAppointment(appointment.getCanceledAppointment());
        appointmentDto.setSubjectAppointment(appointment.getSubjectAppointment());
        return appointmentDto;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationAndCity(String specialization, String city) {
        List<DoctorDto> doctorsDto = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndCity(specialization, city)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctorsDto.add(doctorDto);
        }
        return doctorsDto;
    }
    
    
    public List<DoctorDto> getFavourite(Long id) {
        List<DoctorDto> docs = new ArrayList<>();
        List<Doctor> doctors = doctorDao.findFavourite(id);
        for (Doctor doctor : doctors) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            docs.add(doctorDto);
        }
        return docs;
    }
    
    public Integer addFavourite(Long idUser, Long idDoc) {
        return  doctorDao.addFavourite(idUser, idDoc);
    }
    
    public Integer deleteFavourite(Long idUser, Long idDoc) {
        return  doctorDao.deleteFavourite(idUser, idDoc);
    }
    
}
