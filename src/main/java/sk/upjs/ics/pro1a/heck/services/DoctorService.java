package sk.upjs.ics.pro1a.heck.services;

import sk.upjs.ics.pro1a.heck.utils.Tokenizer;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.jose4j.jwt.NumericDate;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.core.Doctor;
import sk.upjs.ics.pro1a.heck.db.core.Specialization;
import sk.upjs.ics.pro1a.heck.services.dto.ChangePasswordDto;
import sk.upjs.ics.pro1a.heck.services.dto.DoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.IsValidDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;
import sk.upjs.ics.pro1a.heck.utils.PasswordManager;

/**
 *
 * @author Raven
 */
public class DoctorService {
    
    private DoctorDao doctorDao;
    private SpecializationDao specializationDao;
    private final byte[] tokenSecret;
    private Tokenizer tokenizer;
    
    public DoctorService(DoctorDao doctorDao, SpecializationDao specializationDao, byte[] tokenSecret) {
        this.doctorDao = doctorDao;
        this.specializationDao = specializationDao;
        this.tokenSecret = tokenSecret;
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
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecialization(id)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndPostalCode(Long id, String pcn) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndPostalCode(id, pcn)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndName(Long id, String firstname, String LastName) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndName(id, firstname, LastName)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndNameAndPostalCode(Long id, String firstname,
            String LastName, String pcn) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndNameAndPostalCode(id, firstname, LastName,
                pcn)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
        
    public List<DoctorDto> getDoctorsBySpecializationIdAndLastName(Long id, String LastName) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndLastName(id, LastName)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
        }
        return doctors;
    }
    
    public List<DoctorDto> getDoctorsBySpecializationIdAndLastNameAndPostalCode(Long id, String LastName,
            String pcn) {
        List<DoctorDto> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDao.findDoctorsBySpecializationAndLastNameAndPostalCode(id, LastName, pcn)) {
            DoctorDto doctorDto = createDoctorDtoFromDoctorDaoWithoutPassword(doctor);
            doctors.add(doctorDto);
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
        Doctor doctor = doctorDao.findById(id);
        doctor.setFirstNameDoctor(doctorDto.getFirstName());
        doctor.setLastNameDoctor(doctorDto.getLastName());
        doctor.setEmailDoctor(doctorDto.getEmail());
        doctor.setBusinessNameDoctor(doctorDto.getOffice());
        doctor.setAddressDoctor(doctorDto.getAddress());
        doctor.setCityDoctor(doctorDto.getCity());
        doctor.setPostalCodeDoctor(doctorDto.getPostalCode());
        doctor.setPhoneNumberDoctor(doctorDto.getPhoneNumber());
        if(!doctor.getSpecializationDoctor().getId().equals(doctorDto.getSpecialization())){
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
    public LoginResponseDto updateDoctorsToken(String name, String role, Long actualExpirationTime) {
        Doctor doctor = doctorDao.findByLogin(name);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(doctor.getIdDoctor());
        loginResponse.setLogin(doctor.getLoginDoctor());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(tokenizer.updateToken(name, role, expiration));
        return loginResponse;
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
