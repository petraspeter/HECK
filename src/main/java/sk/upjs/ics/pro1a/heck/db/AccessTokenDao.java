package sk.upjs.ics.pro1a.heck.db;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import sk.upjs.ics.pro1a.heck.core.AccessToken;
import org.joda.time.DateTime;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.User;

/**
 *
 * @author raven
 */
public class AccessTokenDao {
    
    private static Map<UUID, AccessToken> accessTokenTable = new HashMap<>();
    
    public Optional<AccessToken> findAccessTokenById(final UUID accessTokenId) {
        AccessToken accessToken = accessTokenTable.get(accessTokenId);
        if (accessToken == null) {
            return Optional.absent();
        }
        return Optional.of(accessToken);
    }
    
    public AccessToken generateNewUserAccessToken(final User user, final DateTime dateTime) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID(), user.getIdUser(), dateTime, false);
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        return accessToken;
    }
    
    public AccessToken generateNewDoctorAccessToken(final Doctor doctor, final DateTime dateTime) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID(), doctor.getIdDoctor(), dateTime, false);
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        return accessToken;
    }
    
    public void setLastAccessTime(final UUID accessTokenUUID, final DateTime dateTime) {
        AccessToken accessToken = accessTokenTable.get(accessTokenUUID);
        accessToken.setLastAccess(dateTime);
        AccessToken updatedAccessToken = accessToken;
        accessTokenTable.put(accessTokenUUID, updatedAccessToken);
    }
    
    
}
