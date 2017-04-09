package sk.upjs.ics.pro1a.heck;

import com.google.common.collect.ImmutableList;
import sk.upjs.ics.pro1a.heck.auth.BasicConsumerAuthenticator;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.Specialization;
import sk.upjs.ics.pro1a.heck.core.Consumer;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.resources.DoctorsResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import javax.ws.rs.client.Client;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.joda.time.DateTimeZone;
import sk.upjs.ics.pro1a.heck.auth.BasicAuth;
import sk.upjs.ics.pro1a.heck.core.User;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.TokenDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.resources.OAuthResources;
import sk.upjs.ics.pro1a.heck.resources.SpecializationsResources;
import sk.upjs.ics.pro1a.heck.resources.UsersResources;

/**
 *
 * @author raven
 */
public class HeckApplication extends Application<HeckConfiguration> {
    
    private final HibernateBundle<HeckConfiguration> hibernateBundle
            = new HibernateBundle<HeckConfiguration>(
                    Specialization.class, Doctor.class, User.class
            ) {
                
                @Override
                public DataSourceFactory getDataSourceFactory(HeckConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
                
            };
    
    public static void main(final String[] args) throws Exception {
        new HeckApplication().run(args);
    }
    
    @Override
    public String getName() {
        return "HECK";
    }
    
    @Override
    public void initialize(final Bootstrap<HeckConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
       // DateTimeZone.setDefault(DateTimeZone.UTC);
    }
    
    @Override
    public void run(final HeckConfiguration configuration, final Environment environment) {
        /**
         * Create final DAO's
         */
        final DoctorDao doctorDao = new DoctorDao(hibernateBundle.getSessionFactory());
        final UserDao userDao = new UserDao(hibernateBundle.getSessionFactory());
        final SpecializationDao specializationDao = new SpecializationDao(hibernateBundle.getSessionFactory());
   //     final TokenDao tokenDao = new TokenDao();
        /*
        ImmutableList<String> type = null;
        type.add("admin"); type.add("doctor"); type.add("user");
        */
        
        /**
         * Create Jesrsey client
         */
        final Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build(getName());
        
        
        /**
         * Register resources
         */
        environment.jersey().register(new DoctorsResource(doctorDao));
        environment.jersey().register(new UsersResources(userDao));
        environment.jersey().register(new SpecializationsResources(specializationDao));
    //   environment.jersey().register(new OAuthResources(tokenDao, userDao, doctorDao));
        
        /**
         * Register Authenticator
         */
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<Consumer>()
                        .setAuthenticator(new BasicConsumerAuthenticator(configuration.getLogin(),
                                configuration.getPassword()))
                        .setRealm("SECURITY REALM")
                        .buildAuthFilter()));
        /**
         * Register roles
         */
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Consumer.class));
        
    }
    
}
