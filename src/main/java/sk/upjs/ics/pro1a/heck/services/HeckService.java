package sk.upjs.ics.pro1a.heck.services;

import com.google.common.base.Throwables;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.SpecializationDao;
import sk.upjs.ics.pro1a.heck.repositories.UserDao;
import sk.upjs.ics.pro1a.heck.repositories.model.Doctor;
import sk.upjs.ics.pro1a.heck.repositories.model.Specialization;
import sk.upjs.ics.pro1a.heck.repositories.model.User;
import sk.upjs.ics.pro1a.heck.services.dto.DoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;
import sk.upjs.ics.pro1a.heck.services.dto.SpecializationDto;
import sk.upjs.ics.pro1a.heck.services.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.NumericDateValidator;
import sk.upjs.ics.pro1a.heck.utils.PasswordManager;

public class HeckService {
    
    private final DoctorDao doctorDao;
    private final SpecializationDao specializationDao;
    private final UserDao userDao;
    private final byte[] tokenSecret;
    
    public HeckService(DoctorDao doctorDao, SpecializationDao specializationDao, UserDao userDao, byte[] tokenSecret) {
        this.doctorDao = doctorDao;
        this.specializationDao = specializationDao;
        this.userDao = userDao;
        this.tokenSecret = tokenSecret;
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
        loginResponse.setToken(generateToken(doctor.getLoginDoctor(), doctor.getPasswordDoctor(), "doctor"));
        return loginResponse;
    }
    
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
    
    public LoginResponseDto loginAsDoctor(String login, String password) {
        Doctor doctor = doctorDao.findByLogin(login);
        if (doctor != null) {
            if (doctor.getPasswordDoctor().equals(PasswordManager.encryptPassword(doctor.getSaltDoctor(), password))) {
                LoginResponseDto loginResponse = new LoginResponseDto();
                loginResponse.setId(doctor.getIdDoctor());
                loginResponse.setLogin(doctor.getLoginDoctor());
                loginResponse.setRole("doctor");
                loginResponse.setToken(generateToken(login, doctor.getPasswordDoctor(), "doctor"));
                return loginResponse;
            }
        }
        return null;
    }
    
    public LoginResponseDto loginAsUser(String login, String password) {
        User user = userDao.findByLoginAndPassword(login, password);
        if (user != null) {
            if (user.getPasswordUser().equals(PasswordManager.encryptPassword(user.getSaltUser(), password))) {
                LoginResponseDto loginResponse = new LoginResponseDto();
                loginResponse.setId(user.getIdUser());
                loginResponse.setLogin(user.getLoginUser());
                loginResponse.setRole("user");
                loginResponse.setToken(generateToken(login, user.getPasswordUser(), "user"));
                return loginResponse;
            }
        }
        return null;
    }
    
    public UserDto getUserById(long id) {
        User user = userDao.findById(id);
        if (user != null) {
            UserDto userDto = createUserDtoFromUserDaoWithPassword(user);
            return userDto;
        }
        return null;
    }
    
    public UserDto getUserByLogin(String login) {
        User user = userDao.findByLogin(login);
        if (user != null) {
            UserDto userDto = createUserDtoFromUserDaoWithPassword(user);
            return userDto;
        }
        return null;
    }
    
    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userDao.findAll()) {
            UserDto userDto = createUserDtoFromUserDaoWithoutPassword(user);
            users.add(userDto);
        }
        return users;
    }
    
    
    public LoginResponseDto registerUser(UserDto userDto) {
        if (userDto.getPassword() == null || userDto.getPassword().length() < 6) {
            throw new IllegalStateException("Password does not match criteria.");
        }
        String salt = new BigInteger(130, new SecureRandom()).toString(32);
        String password = PasswordManager.encryptPassword(salt, userDto.getPassword());
        User user = createUserDaoFromUserDto(userDto, password, salt);
        user = userDao.createUser(user);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(user.getIdUser());
        loginResponse.setLogin(user.getLoginUser());
        loginResponse.setRole("user");
        loginResponse.setToken(generateToken(user.getLoginUser(), user.getPasswordUser(), "user"));
        return loginResponse;
    }
    
    /**
     *      UPDATE Tokens
     */
    
    /**
     *
     * @param name AuthorizedUserDto name
     * @param role AuthorizedUserDto role
     * @return new Token(for extended expiration password time)
     */
    public LoginResponseDto updateDoctorsToken(String name, String role, Long actualExpirationTime) {
        Doctor doctor = doctorDao.findByLogin(name);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(doctor.getIdDoctor());
        loginResponse.setLogin(doctor.getLoginDoctor());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(updateToken(name, doctor.getPasswordDoctor(), role, expiration));
        return loginResponse;
    }
    
    /**
     *
     * @param name AuthorizedUserDto name
     * @param role AuthorizedUserDto role
     * @param actualExpirationTime
     * @return
     */
    public LoginResponseDto updateUsersToken(String name, String role, Long actualExpirationTime) {
        User user= userDao.findByLogin(name);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(user.getIdUser());
        loginResponse.setLogin(user.getLoginUser());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(updateToken(name, user.getPasswordUser(), role, expiration));
        return loginResponse;
    }
    
    
    /**
     *      Private methods
     */
    
    private String generateToken(String login, String password, String role) {
        final JwtClaims claims = new JwtClaims();
        claims.setStringClaim("password", password);
        claims.setStringClaim("role", role);
        claims.setSubject(login);
        claims.setExpirationTimeMinutesInTheFuture(30);
        
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        
        try {
            return jws.getCompactSerialization();
        } catch (JoseException javier) {
            throw Throwables.propagate(javier);
        }
    }
    
    private String updateToken(String login, String password, String role, NumericDate expiration) {        
        final JwtClaims claims = new JwtClaims();
        claims.setStringClaim("password", password);
        claims.setStringClaim("role", role);
        claims.setSubject(login);
        /**
         * increase expiration time
         */
        expiration.addSeconds(900);
        long newExpirationTime = new Timestamp(expiration.getValueInMillis()).getTime();
        long realTime = new Timestamp(System.currentTimeMillis()).getTime();        
        long addMinutes = (newExpirationTime - realTime) / (60 * 1000);  
        
        claims.setExpirationTimeMinutesInTheFuture(addMinutes);        
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        try {
            return jws.getCompactSerialization();
        } catch (JoseException javier) {
            throw Throwables.propagate(javier);
        }
    }
    
    /**
     *
     * @param doctorDto JSON Doctor DTO representation
     * @param password Doctor's password(encrypted)
     * @param salt Doctor's salt
     * @param specialization Doctor's specialization
     * @return new Doctor object
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
                doctorDto.getAddress()
        );
    }
    
    /**
     *
     * @param userDto JSON User DTO representation
     * @param password User's password(encrypted)
     * @param salt User's salt
     * @return new User object
     */
    private User createUserDaoFromUserDto(UserDto userDto, String password, String salt) {
        return new User(
                userDto.getEmail(),
                userDto.getLogin(),
                password,
                salt,
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getPhoneNumber(),
                userDto.getPostalCode(),
                userDto.getCity(),
                userDto.getAddress()
        );
    }
    
    private UserDto createUserDtoFromUserDaoWithoutPassword(User user) {
        return new UserDto(
                user.getIdUser(),
                user.getLoginUser(),
                user.getFirstNameUser(),
                user.getLastNameUser(),
                user.getEmailUser(),
                user.getAddressUser(),
                user.getPostalCodeUser(),
                user.getCityUser(),
                user.getPhoneUser()
        );
    }
    
    private UserDto createUserDtoFromUserDaoWithPassword(User user) {
        return new UserDto(
                user.getIdUser(),
                user.getLoginUser(),
                user.getPasswordUser(),
                user.getFirstNameUser(),
                user.getLastNameUser(),
                user.getEmailUser(),
                user.getAddressUser(),
                user.getPostalCodeUser(),
                user.getCityUser(),
                user.getPhoneUser()
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
                doctor.getSpecializationDoctor().getId()
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
                doctor.getSpecializationDoctor().getId()
        );
    }
    
}
