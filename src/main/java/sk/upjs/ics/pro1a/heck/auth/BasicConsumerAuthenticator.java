package sk.upjs.ics.pro1a.heck.auth;

import java.util.Optional;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import sk.upjs.ics.pro1a.heck.core.Consumer;
import io.dropwizard.auth.AuthenticationException;

/**
 *
 * @author raven
 */
public class BasicConsumerAuthenticator implements Authenticator<BasicCredentials, Consumer> {
    
    private final String login;
    
    private final String password;
    
    public BasicConsumerAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    @Override
    public Optional<Consumer> authenticate(BasicCredentials credentials)
            throws AuthenticationException {
        if (password.equals(credentials.getPassword())
                && login.equals(credentials.getUsername())) {
            return Optional.of(new Consumer());
        } else {
            return Optional.empty();
        }
    }
    
}
