package sk.upjs.ics.pro1a.heck;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author raven
 */
public class HeckConfiguration extends Configuration {
    
    
    @NotEmpty
    private String jwtTokenSecret = new BigInteger (130, new SecureRandom ()).toString(35);
    
    @NotEmpty
    private String bearerRealm;
    
    @NotEmpty
    private String login;
    
    @NotEmpty
    private String password;
    
    @NotNull
    @Valid
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();
    
    @Valid
    @NotNull
    private final JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
    
    @JsonProperty
    public String getLogin() {
        return login;
    }
    
    @JsonProperty
    public String getPassword() {
        return password;
    }
    
    public String getBearerRealm() {
        return bearerRealm;
    }
    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
    
    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClientConfiguration;
    }
    
    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return jwtTokenSecret.getBytes("UTF-8");
    }
    
}
