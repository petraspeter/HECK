package sk.upjs.ics.pro1a.heck.services;

import sk.upjs.ics.pro1a.heck.utils.ServiceUtils;
import sk.upjs.ics.pro1a.heck.db.core.WorkingTime;
import sk.upjs.ics.pro1a.heck.services.dto.*;
import sk.upjs.ics.pro1a.heck.utils.Tokenizer;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

    public DoctorService(DoctorDao doctorDao, SpecializationDao specializationDao, WorkingTimeDao workingTimeDao, AppointmentDao appointmentDao, byte[] tokenSecret) {
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

    public List<DoctorDto> getDoctorsBySpecializationIdAndCityAndDate(Long id, String city, Timestamp from,
            Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationIdAndCity(id, city)) {
            List<AppointmentDto> appointments = appointmentDao.generateDoctorAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }

    public List<DoctorDto> getDoctorsBySpecializationIdAndFullNameAndDate(Long id, String firstname,
            String lastName, Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationIdAndName(id, firstname, lastName)) {
            List<AppointmentDto> appointments = appointmentDao.generateDoctorAppointmentForDays(doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }

    public List<DoctorDto> getDoctorsBySpecializationIdAndFullNameAndCityAndDate(Long id, String firstname,
            String lastName, String city, Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationIdAndNameAndCity(id, firstname, lastName, city)) {
            List<AppointmentDto> appointments = appointmentDao.generateDoctorAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }

    public List<DoctorDto> getDoctorsBySpecializationIdAndLastNameAndDate(Long id, String lastName,
            Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationIdAndLastName(id, lastName)) {
            List<AppointmentDto> appointments = appointmentDao.generateDoctorAppointmentForDays(
                    doctor.getIdDoctor(), 0L, from, to);
            if (appointments.size() > 0) {
                DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
                doctors.add(doctorDto);
            }
        }
        return doctors;
    }

    public List<DoctorDto> getDoctorsBySpecializationIdAndLastNameAndCityAndDate(Long id, String lastName,
            String city, Timestamp from, Timestamp to) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationIdAndLastNameAndCity(id, lastName, city)) {
            List<AppointmentDto> appointments = appointmentDao.generateDoctorAppointmentForDays(
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
        doctorDto.setRegistrationTime(new Date().toString());
        Doctor doctor = createDoctorDaoFromDoctorDto(doctorDto, password, salt, specialization);
        doctor.setActivationTimeDoctor(new Timestamp(System.currentTimeMillis()));
        doctor = doctorDao.create(doctor);
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
        Doctor doctor = doctorDao.findById(id);
        doctor.setFirstNameDoctor(doctorDto.getFirstName());
        doctor.setLastNameDoctor(doctorDto.getLastName());
        doctor.setEmailDoctor(doctorDto.getEmail());
        doctor.setBusinessNameDoctor(doctorDto.getOffice());
        doctor.setAddressDoctor(doctorDto.getAddress());
        doctor.setCityDoctor(doctorDto.getCity());
        doctor.setPostalCodeDoctor(doctorDto.getPostalCode());
        doctor.setPhoneNumberDoctor(doctorDto.getPhoneNumber());
        doctor.setActiveDoctor((doctorDto.getActive() != null) ? doctorDto.getActive() : doctor.getActiveDoctor());
        if (!doctor.getSpecializationDoctor().getId().equals(doctorDto.getSpecialization())) {
            doctor.setSpecializationDoctor(specializationDao.findById(doctorDto.getSpecialization()));
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
        if (workingTimes.isEmpty()) {
            return new WorkingTimeDto();
        }
        WorkingTimeDto workingTimeDto = new WorkingTimeDto();
        workingTimeDto.setInterval(workingTimes.get(0).getDoctor().getAppointmentInterval());

        ArrayList<WorkingDayDto> workingDaysDto = new ArrayList<>();
        for (WorkingTime workingTime : workingTimes) {
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
        Doctor doctor = doctorDao.findById(doctorId);
        if(doctor != null) {
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
        } else {
            throw new IllegalStateException("Doctor does not exist.");
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
                ServiceUtils.convertStringToTimestamp(doctorDto.getRegistrationTime()),
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

}
