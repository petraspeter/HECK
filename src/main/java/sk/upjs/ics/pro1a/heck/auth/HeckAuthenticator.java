package sk.upjs.ics.pro1a.heck.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.UserDao;
import sk.upjs.ics.pro1a.heck.repositories.model.Doctor;
import sk.upjs.ics.pro1a.heck.repositories.model.User;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;

import java.util.Optional;

public class HeckAuthenticator implements Authenticator<JwtContext, AuthorizedUserDto> {

    private DoctorDao doctorDao;
    private UserDao userDao;

    public HeckAuthenticator(DoctorDao doctorDao, UserDao userDao) {
        this.doctorDao = doctorDao;
        this.userDao = userDao;
    }

    @Override
    @UnitOfWork
    public Optional<AuthorizedUserDto> authenticate(JwtContext context) {
        // Provide your own implementation to lookup users based on the principal attribute in the
        // JWT Token. E.g.: lookup users from a database etc.
        // This method will be called once the token's signature has been verified

        // In case you want to verify different parts of the token you can do that here.
        // E.g.: Verifying that the provided token has not expired.

        // All JsonWebTokenExceptions will result in a 401 Unauthorized response.

        try {
            final String role = context.getJwtClaims().getClaimValue("role").toString();
            final String password = context.getJwtClaims().getClaimValue("password").toString();
            final String login = context.getJwtClaims().getSubject();
            if ("user".equals(role)) {
                Doctor doctor = doctorDao.findByLoginAndPassword(login, password);
                if (doctor != null) {
                    return Optional.of(new AuthorizedUserDto(doctor.getIdDoctor(), login, "user"));
                }
            }
            if ("user".equals(role)) {
                User user = userDao.findByLoginAndPassword(login, password);
                if (user != null) {
                    return Optional.of(new AuthorizedUserDto(user.getIdUser(), login, "user"));
                }
            }
            return Optional.empty();
        } catch (MalformedClaimException e) {
            return Optional.empty();
        }
    }
}
