package sk.upjs.ics.pro1a.heck.services;

import sk.upjs.ics.pro1a.heck.utils.Tokenizer;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jose4j.jwt.NumericDate;
import sk.upjs.ics.pro1a.heck.db.core.User;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;
import sk.upjs.ics.pro1a.heck.services.dto.UserDto;
import sk.upjs.ics.pro1a.heck.utils.PasswordManager;

/**
 *
 * @author Raven
 */
public class UserService {
    
    
    private final byte[] tokenSecret;
    private final Tokenizer tokenizer;
    private final SessionFactory sessionFactory;
    
    public UserService(byte[] tokenSecret, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.tokenSecret = tokenSecret;
        tokenizer = new Tokenizer(tokenSecret);
    }
    
    /*
    GET methods
    */
    public UserDto getUserById(long id) {
        User user = findById(id);
        if (user != null) {
            UserDto userDto = createUserDtoFromUserDaoWithPassword(user);
            return userDto;
        }
        return null;
    }
    
    public UserDto getUserByLogin(String login) {
        User user = findByLogin(login);
        if (user != null) {
            UserDto userDto = createUserDtoFromUserDaoWithPassword(user);
            return userDto;
        }
        return null;
    }
    
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user :  findAll()) {
            UserDto userDto = createUserDtoFromUserDaoWithoutPassword(user);
            usersDto.add(userDto);
        }
        return usersDto;
    }
    
    /*
    Login and registration methods
    */
    public LoginResponseDto loginAsUser(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            if (user.getPasswordUser().equals(PasswordManager.encryptPassword(user.getSaltUser(), password))) {
                LoginResponseDto loginResponse = new LoginResponseDto();
                loginResponse.setId(user.getIdUser());
                loginResponse.setLogin(user.getLoginUser());
                if(user.getAdmin()){
                    loginResponse.setRole("admin");
                } else {
                    loginResponse.setRole("user");
                }
                loginResponse.setToken(tokenizer.generateToken(login, loginResponse.getRole()));
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
        user.setRegistrationTime(new Timestamp(System.currentTimeMillis()));
        sessionFactory.getCurrentSession().persist(user);
        user = findByLogin(user.getLoginUser());
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
        User user = findById(id);
        user.setFirstNameUser(userDto.getFirstName());
        user.setLastNameUser(userDto.getLastName());
        user.setEmailUser(userDto.getEmail());
        user.setAddressUser(userDto.getAddress());
        user.setCityUser(userDto.getCity());
        user.setPhoneUser(userDto.getPhoneNumber());
        user.setPostalCodeUser(userDto.getPostalCode());
        sessionFactory.getCurrentSession().persist(user);
    }
    
    public void changePassword(Long id, String newPassword) {
        User user = findById(id);
        String salt = new BigInteger(130, new SecureRandom()).toString(32);
        String password = PasswordManager.encryptPassword(salt, newPassword);
        user.setSaltUser(salt);
        user.setPasswordUser(password);
        sessionFactory.getCurrentSession().persist(user);
    }
    
    public void deactivateAccount(Long id) {
        User user = findById(id);
        user.setActiveUser(false);
        sessionFactory.getCurrentSession().persist(user);
    }
    
    public void updateUser(Long id, UserDto userDto) {
        User user = findById(id);
        user.setAdmin(user.getAdmin());
        user.setAddressUser(userDto.getAddress());
        user.setCityUser(userDto.getCity());
        user.setEmailUser(userDto.getEmail());
        user.setFirstNameUser(userDto.getFirstName());
        user.setLastNameUser(userDto.getLastName());
        user.setLoginUser(userDto.getLogin());
        user.setPhoneUser(userDto.getPhoneNumber());
        user.setPostalCodeUser(userDto.getPostalCode());
        sessionFactory.getCurrentSession().persist(user);
    }
    
    /*
    Update token method
    */
    public LoginResponseDto updateUsersToken(String login, String role, Long actualExpirationTime) {
        User user= findByLogin(login);
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setId(user.getIdUser());
        loginResponse.setLogin(user.getLoginUser());
        loginResponse.setRole(role);
        NumericDate expiration = NumericDate.fromSeconds(actualExpirationTime);
        loginResponse.setToken(tokenizer.updateToken(login, role, expiration));
        return loginResponse;
    }
    
    /*
    Private "query"methods
    */
    private User findById(long id) {
        return  (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("idUser", id)).uniqueResult();
    }
    
    private User findByLogin(String login) {
        return  (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("loginUser", login)).uniqueResult();
    }
    
    private List<User> findAll() {
        return (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class).list();
    }
    
    /*
    Methods for convertation between DTO and DAO
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
                userDto.getRegistrationTime(),
                userDto.isAdmin()
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
                user.getRegistrationTime(),
                user.getAdmin()
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
                user.getRegistrationTime(),
                user.getAdmin()
                
        );
    }
    
}
