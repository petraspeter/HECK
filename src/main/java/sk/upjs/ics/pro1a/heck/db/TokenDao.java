package sk.upjs.ics.pro1a.heck.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import sk.upjs.ics.pro1a.heck.core.Token;
import sk.upjs.ics.pro1a.heck.core.User;
import org.joda.time.DateTime;
import sk.upjs.ics.pro1a.heck.core.Doctor;

/**
 *
 * @author raven
 */
public class TokenDao {
    
    private Map<UUID, Token> accessTokenTable = new HashMap<>();
    
    public Optional<Token> findAccessTokenById(UUID tokenID) {
        Token token = accessTokenTable.get(tokenID);
        if(token == null) {
            return Optional.empty();
        }
        return Optional.of(token);
    }
    
    public Token generateNewTokenUser(User user, DateTime time) {
        Token token = new Token(user.getIdUser(), UUID.randomUUID(), time);
        accessTokenTable.put(token.getTokenId(), token);
        return token;
    }
    
    public Token generateNewTokenDoctor(Doctor doctor, DateTime time) {
        Token token = new Token(doctor.getIdDoctor(), UUID.randomUUID(), time);
        accessTokenTable.put(token.getTokenId(), token);
        return token;
    }
    
    public void changeAccessTime(UUID tokenUUID, DateTime time) {
        Token token = accessTokenTable.get(tokenUUID);
        token.setLastAccess(time);
        accessTokenTable.put(tokenUUID, token);
    }
    
    
}
