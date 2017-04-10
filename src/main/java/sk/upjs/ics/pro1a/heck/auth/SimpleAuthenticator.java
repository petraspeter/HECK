package sk.upjs.ics.pro1a.heck.auth;

import com.google.common.base.Optional;
import sk.upjs.ics.pro1a.heck.db.AccessTokenDao;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.joda.time.DateTime;
import org.joda.time.Period;
import java.util.UUID;
import sk.upjs.ics.pro1a.heck.core.AccessToken;


/**
 *
 * @author raven
 */
public class SimpleAuthenticator implements Authenticator {
    
    public static final int ACCESS_TOKEN_EXPIRE_TIME_MIN = 30;
    private AccessTokenDao accessTokenDao;
    
    public SimpleAuthenticator(AccessTokenDao accessTokenDao) {
        this.accessTokenDao = accessTokenDao;
    }
    
    public Optional<Long> authenticate(String accessTokenId) throws AuthenticationException {
        // Check input, must be a valid UUID
        UUID accessTokenUUID;
        try {
            accessTokenUUID = UUID.fromString(accessTokenId);
        } catch (IllegalArgumentException e) {
            return Optional.absent();
        }
        
        // Get the access token from the database
        Optional<AccessToken> accessToken = accessTokenDao.findAccessTokenById(accessTokenUUID);
        if (accessToken == null || !accessToken.isPresent()) {
            return Optional.absent();
        }
        
        // Check if the last access time is not too far in the past (the access token is expired)
        Period period = new Period(accessToken.get().getLastAccess(), new DateTime());
        if (period.getMinutes() > ACCESS_TOKEN_EXPIRE_TIME_MIN) {
            return Optional.absent();
        }
        
        // Update the access time for the token
        accessTokenDao.setLastAccessTime(accessTokenUUID, new DateTime());
        
        // Return the user's id for processing
        return Optional.of(accessToken.get().getConsumerId());
    }

    @Override
    public java.util.Optional authenticate(Object c) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
