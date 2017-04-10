package sk.upjs.ics.pro1a.heck.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.joda.time.DateTime;

/**
 *
 * @author raven
 */

public class AccessToken {
    
    @JsonProperty("AccessTokenId")
    @NotNull
    private UUID accessTokenId;
    
    
    @JsonProperty("ConsumerId")
    @NotNull
    private Long consumerId;
    
    @JsonProperty("LastAccess")
    @NotNull
    private DateTime lastAccess;
    
    @JsonProperty("ConsumerType")
    @NotNull
    private Boolean isDoctor;
    
    public AccessToken(UUID accessTokenId, Long userId, DateTime lastAccess, Boolean isDoctor) {
        this.accessTokenId = accessTokenId;
        this.consumerId = userId;
        this.lastAccess = lastAccess;
        this.isDoctor = isDoctor;
    }
    
    public UUID getAccessTokenId() {
        return accessTokenId;
    }
    
    public void setAccessTokenId(UUID accessTokenId) {
        this.accessTokenId = accessTokenId;
    }
    
    public Long getConsumerId() {
        return consumerId;
    }
    
    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }
    
    public DateTime getLastAccess() {
        return lastAccess;
    }
    
    public void setLastAccess(DateTime lastAccess) {
        this.lastAccess = lastAccess;
    }
    
    public Boolean getIsDoctor() {
        return isDoctor;
    }
    
    public void setIsDoctor(Boolean isDoctor) {
        this.isDoctor = isDoctor;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.accessTokenId);
        hash = 59 * hash + Objects.hashCode(this.consumerId);
        hash = 59 * hash + Objects.hashCode(this.lastAccess);
        hash = 59 * hash + Objects.hashCode(this.isDoctor);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AccessToken other = (AccessToken) obj;
        
        return !Objects.equals(this.accessTokenId, other.accessTokenId) &&
                !Objects.equals(this.consumerId, other.consumerId) &&
                !Objects.equals(this.lastAccess, other.lastAccess) && !Objects.equals(this.isDoctor, other.isDoctor);
    }
    
    @Override
    public String toString() {
        return "AccessToken{" + "accessTokenId=" + accessTokenId + ", consumerId=" + consumerId +
                ", lastAccess=" + lastAccess + ", isDoctor=" + isDoctor + '}';
    }
    
}
