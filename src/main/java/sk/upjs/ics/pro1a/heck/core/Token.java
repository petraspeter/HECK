package sk.upjs.ics.pro1a.heck.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.joda.time.DateTime;

/**
 *
 * @author raven
 */
public class Token {
    
    @JsonProperty("ConsumerId")
    @NotNull
    private Long consumerId;
    
    @JsonProperty("TokenId")
    @NotNull
    private UUID tokenId;
    
    @JsonProperty("LastAccess")
    @NotNull
    private DateTime lastAccess;

    public Token(Long consumerId, UUID tokenId, DateTime lastAccess) {
        this.consumerId = consumerId;
        this.tokenId = tokenId;
        this.lastAccess = lastAccess;
    }

    public Token() {
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public DateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(DateTime lastAccess) {
        this.lastAccess = lastAccess;
    }
        
}
