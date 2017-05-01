package sk.upjs.ics.pro1a.heck.utils;

import com.google.common.base.Throwables;
import java.sql.Timestamp;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Raven
 */
public class Tokenizer {
    
    private byte[] tokenSecret;

    public Tokenizer(byte[] tokenSecret) {
        this.tokenSecret = tokenSecret;
    }    
    
    public String generateToken(String login, String role) {
        final JwtClaims claims = new JwtClaims();
        claims.setStringClaim("role", role);
        claims.setSubject(login);
        claims.setExpirationTimeMinutesInTheFuture(300);
        
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        
        try {
            return jws.getCompactSerialization();
        } catch (JoseException javier) {
            throw Throwables.propagate(javier);
        }
    }
    
    public String updateToken(String login, String role, NumericDate expiration) {
        final JwtClaims claims = new JwtClaims();
        claims.setStringClaim("role", role);
        claims.setSubject(login);
        /**
         * increase expiration time
         */
        expiration.addSeconds(900);
        long newExpirationTime = new Timestamp(expiration.getValueInMillis()).getTime();
        long realTime = new Timestamp(System.currentTimeMillis()).getTime();
        long addMinutes = (newExpirationTime - realTime) / (60 * 1000);
        
        claims.setExpirationTimeMinutesInTheFuture(addMinutes);
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        try {
            return jws.getCompactSerialization();
        } catch (JoseException javier) {
            throw Throwables.propagate(javier);
        }
    }
    
}
