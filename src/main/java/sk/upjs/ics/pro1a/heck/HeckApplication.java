package sk.upjs.ics.pro1a.heck;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import sk.upjs.ics.pro1a.heck.auth.HeckAuthenticator;
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.SpecializationDao;
import sk.upjs.ics.pro1a.heck.repositories.UserDao;
import sk.upjs.ics.pro1a.heck.repositories.model.Doctor;
import sk.upjs.ics.pro1a.heck.repositories.model.Specialization;
import sk.upjs.ics.pro1a.heck.repositories.model.User;
import sk.upjs.ics.pro1a.heck.resources.HeckResources;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;
import java.security.Principal;
import java.util.EnumSet;

/**
 * @author raven
 */
public class HeckApplication extends Application<HeckConfiguration> {

    private final HibernateBundle<HeckConfiguration> hibernateBundle = new HibernateBundle<HeckConfiguration>(Specialization.class, Doctor.class, User.class) {

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
    public void run(final HeckConfiguration configuration, final Environment environment) throws Exception {
        /**
         * Create final DAO's
         */
        final DoctorDao doctorDao = new DoctorDao(hibernateBundle.getSessionFactory());
        final UserDao userDao = new UserDao(hibernateBundle.getSessionFactory());
        final SpecializationDao specializationDao = new SpecializationDao(hibernateBundle.getSessionFactory());

        /**
         * Create Jesrsey client
         */
        final Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build(getName());


        /**
         * Register resources and authorization
         */

        final byte[] key = configuration.getJwtTokenSecret();

        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(new HmacKey(key)) // verify the signature with the public key
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build(); // create the JwtConsumer instance

        HeckAuthenticator heckAuthenticator = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(HeckAuthenticator.class, new Class[]{DoctorDao.class, UserDao.class}, new Object[]{doctorDao, userDao});

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<AuthorizedUserDto>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(heckAuthenticator)
                        .buildAuthFilter()));


        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AuthorizedUserDto.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new HeckResources(doctorDao, userDao, specializationDao, key));

        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
