package sk.upjs.ics.pro1a.heck.services;

import com.google.common.base.Throwables;
import java.math.BigInteger;
import java.security.SecureRandom;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;
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
        Doctor doctor = new Doctor(
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
                Timestamp.from(Instant.now())
        );
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
        for (Doctor d : doctorDao.findAll()) {
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setId(d.getIdDoctor());
            doctorDto.setAddress(d.getAddressDoctor());
            doctorDto.setCity(d.getCityDoctor());
            doctorDto.setEmail(d.getEmailDoctor());
            doctorDto.setFirstName(d.getFirstNameDoctor());
            doctorDto.setLastName(d.getLastNameDoctor());
            doctorDto.setLogin(d.getLoginDoctor());
            doctorDto.setOffice(d.getBusinessNameDoctor());
            doctorDto.setPhoneNumber(d.getPhoneNumberDoctor());
            doctorDto.setPostalCode(d.getPostalCodeDoctor());
            doctorDto.setSpecialization(d.getSpecializationDoctor().getId());
            //we don't want to get password here
            doctors.add(doctorDto);
        }
        return doctors;
    }

    public DoctorDto getDoctorById(long id) {
        Doctor doctor = doctorDao.findById(id);
        if (doctor != null) {
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setId(doctor.getIdDoctor());
            doctorDto.setAddress(doctor.getAddressDoctor());
            doctorDto.setCity(doctor.getCityDoctor());
            doctorDto.setEmail(doctor.getEmailDoctor());
            doctorDto.setFirstName(doctor.getFirstNameDoctor());
            doctorDto.setLastName(doctor.getLastNameDoctor());
            doctorDto.setLogin(doctor.getLoginDoctor());
            doctorDto.setOffice(doctor.getBusinessNameDoctor());
            doctorDto.setPhoneNumber(doctor.getPhoneNumberDoctor());
            doctorDto.setPostalCode(doctor.getPostalCodeDoctor());
            doctorDto.setSpecialization(doctor.getSpecializationDoctor().getId());
            doctorDto.setPassword(doctor.getPasswordDoctor());
            return doctorDto;
        }
        return null;
    }

    public DoctorDto getDoctorByLogin(String login) {
        Doctor doctor = doctorDao.findByLogin(login);
        if (doctor != null) {
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setId(doctor.getIdDoctor());
            doctorDto.setAddress(doctor.getAddressDoctor());
            doctorDto.setCity(doctor.getCityDoctor());
            doctorDto.setEmail(doctor.getEmailDoctor());
            doctorDto.setFirstName(doctor.getFirstNameDoctor());
            doctorDto.setLastName(doctor.getLastNameDoctor());
            doctorDto.setLogin(doctor.getLoginDoctor());
            doctorDto.setOffice(doctor.getBusinessNameDoctor());
            doctorDto.setPhoneNumber(doctor.getPhoneNumberDoctor());
            doctorDto.setPostalCode(doctor.getPostalCodeDoctor());
            doctorDto.setSpecialization(doctor.getSpecializationDoctor().getId());
            doctorDto.setPassword(doctor.getPasswordDoctor());
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
            UserDto userDto = new UserDto();
            userDto.setId(user.getIdUser());
            userDto.setAddress(user.getAddressUser());
            userDto.setCity(user.getCityUser());
            userDto.setEmail(user.getEmailUser());
            userDto.setFirstName(user.getFirstNameUser());
            userDto.setLastName(user.getLastNameUser());
            userDto.setLogin(user.getLoginUser());
            userDto.setPhoneNumber(user.getPhoneUser());
            userDto.setPostalCode(user.getPostalCodeUser());
            userDto.setPassword(user.getPasswordUser());
            return userDto;
        }
        return null;
    }

    public UserDto getUserByLogin(String login) {
        User user = userDao.findByLogin(login);
        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getIdUser());
            userDto.setAddress(user.getAddressUser());
            userDto.setCity(user.getCityUser());
            userDto.setEmail(user.getEmailUser());
            userDto.setFirstName(user.getFirstNameUser());
            userDto.setLastName(user.getLastNameUser());
            userDto.setLogin(user.getLoginUser());
            userDto.setPhoneNumber(user.getPhoneUser());
            userDto.setPostalCode(user.getPostalCodeUser());
            userDto.setPassword(user.getPasswordUser());
            return userDto;
        }
        return null;
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userDao.findAll()) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getIdUser());
            userDto.setAddress(user.getAddressUser());
            userDto.setCity(user.getCityUser());
            userDto.setEmail(user.getEmailUser());
            userDto.setFirstName(user.getFirstNameUser());
            userDto.setLastName(user.getLastNameUser());
            userDto.setLogin(user.getLoginUser());
            userDto.setPhoneNumber(user.getPhoneUser());
            userDto.setPostalCode(user.getPostalCodeUser());
            //we don't want to get password here
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
        User user = new User(
                userDto.getEmail(),
                userDto.getLogin(),
                password,
                salt,
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getPhoneNumber(),
                userDto.getPostalCode(),
                userDto.getCity(),
                userDto.getAddress());
        user = userDao.createUser(user);

        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(user.getIdUser());
        loginResponse.setLogin(user.getLoginUser());
        loginResponse.setRole("user");
        loginResponse.setToken(generateToken(user.getLoginUser(), user.getPasswordUser(), "user"));
        return loginResponse;
    }

    private String generateToken(String login, String password, String role) {
        final JwtClaims claims = new JwtClaims();
        claims.setStringClaim("password", password);
        claims.setStringClaim("role", role);
        claims.setSubject(login);
        //claims.setExpirationTimeMinutesInTheFuture(30);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw Throwables.propagate(e);
        }
    }
}
