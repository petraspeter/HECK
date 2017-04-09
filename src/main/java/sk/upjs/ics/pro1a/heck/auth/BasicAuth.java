package sk.upjs.ics.pro1a.heck.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.Period;
import sk.upjs.ics.pro1a.heck.core.Consumer;
import sk.upjs.ics.pro1a.heck.core.Token;
import sk.upjs.ics.pro1a.heck.db.TokenDao;

/**
 *
 * @author raven
 */
public class BasicAuth implements Authenticator<String, Consumer>{
    
    public static final int TOKEN_EXPIRE_TIME_MIN = 45;
    private TokenDao tokenDao;
    
    @Override
    public Optional<Consumer> authenticate(String tokenId) throws AuthenticationException {
        
        /**
         * input has to be formated as UUID
         */
        UUID accessTokenUUID;
        try {
            accessTokenUUID = UUID.fromString(tokenId);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        
        /**
         * get token
         */
        Optional<Token> token = tokenDao.findAccessTokenById(accessTokenUUID);
        if (token == null || !token.isPresent()) {
            return Optional.empty();
        }
        
        /**
         * check token expiration
         */
        Period period = new Period(token.get().getLastAccess(), new DateTime());
        if (period.getMinutes() > TOKEN_EXPIRE_TIME_MIN) {
            return Optional.empty();
        }
        
        /**
         * update access time for token
         */
        tokenDao.changeAccessTime(accessTokenUUID, new DateTime());
        
        /**
         * return consumers id
         */
       // return Optional.of(new Consumer(token.get().getConsumerId()));
       return null;
    }
    
    
}
