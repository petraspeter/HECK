package sk.upjs.ics.pro1a.heck;

import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.Specialization;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
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
import sk.upjs.ics.pro1a.heck.auth.SimpleAuthenticator;
import sk.upjs.ics.pro1a.heck.core.User;
import sk.upjs.ics.pro1a.heck.db.AccessTokenDao;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.resources.HeckResources;
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
        final AccessTokenDao accessTokenDao = new AccessTokenDao();
        
        /**
         * Create Jesrsey client
         */
        final Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build(getName());
        
        
        /**
         * Register resources
         */
        environment.jersey().register(new HeckResources(accessTokenDao, doctorDao, userDao, specializationDao));
        
        
        
    }
    
}
