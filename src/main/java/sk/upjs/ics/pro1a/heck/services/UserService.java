package sk.upjs.ics.pro1a.heck.services;

import sk.upjs.ics.pro1a.heck.utils.Tokenizer;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.jose4j.jwt.NumericDate;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.db.core.User;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;
import sk.upjs.ics.pro1a.heck.services.dto.UserDto;
import sk.upjs.ics.pro1a.heck.utils.PasswordManager;

/**
 *
 * @author Raven
 */
public class UserService {
    
    
    private  UserDao userDao;
    private final byte[] tokenSecret;
    private Tokenizer tokenizer;
    
    public UserService(UserDao userDao, byte[] tokenSecret) {
        this.userDao = userDao;
        this.tokenSecret = tokenSecret;
        tokenizer = new Tokenizer(tokenSecret);
    }
    
    /*
    GET methods
    */
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
    
    /*
    Login and registration methods
    */
    public LoginResponseDto loginAsUser(String login, String password) {
        User user = userDao.findByLogin(login);
        if (user != null) {
            if (user.getPasswordUser().equals(PasswordManager.encryptPassword(user.getSaltUser(), password))) {
                LoginResponseDto loginResponse = new LoginResponseDto();
                loginResponse.setId(user.getIdUser());
                loginResponse.setLogin(user.getLoginUser());
                loginResponse.setRole("user");
                loginResponse.setToken(tokenizer.generateToken(login, "user"));
                return loginResponse;
            }
        }
        return null;
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
        loginResponse.setToken(tokenizer.generateToken(user.getLoginUser(), "user"));
        return loginResponse;
    }
    
    /*
    User update methods
    */
    
    public void updateDoctor(Long id, UserDto userDto) {
        User user = userDao.findById(id);
        user.setFirstNameUser(userDto.getFirstName());
        user.setLastNameUser(userDto.getLastName());
        user.setEmailUser(userDto.getEmail());
        user.setAddressUser(userDto.getAddress());
        user.setCityUser(userDto.getCity());
        user.setPhoneUser(userDto.getPhoneNumber());
        user.setPostalCodeUser(userDto.getPostalCode());
        userDao.update(user);
    }
    
    public void changePassword(Long id, String newPassword) {
        User user = userDao.findById(id);
        String salt = new BigInteger(130, new SecureRandom()).toString(32);
        String password = PasswordManager.encryptPassword(salt, newPassword);
        user.setSaltUser(salt);
        user.setPasswordUser(password);
        userDao.update(user);
    }
    
    public void deactivateAccount(Long id) {
        User user = userDao.findById(id);
        user.setActiveUser(false);
        userDao.update(user);
    }
    
    /*
    Update token method
    */
    public LoginResponseDto updateUsersToken(String name, String role, Long actualExpirationTime) {
        User user= userDao.findByLogin(name);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(user.getIdUser());
        loginResponse.setLogin(user.getLoginUser());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(tokenizer.updateToken(name, role, expiration));
        return loginResponse;
    }
    
    
    /*
    Methods for convert between DTO and DAO
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
                userDto.getAddress(),
                userDto.getRegistrationTime()
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
                user.getPhoneUser(),
                user.getRegistrationTime()
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
                user.getPhoneUser(),
                user.getRegistrationTime()
        );
    }
    
}