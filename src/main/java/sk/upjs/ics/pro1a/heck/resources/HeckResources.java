package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.hibernate.UnitOfWork;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;
import sk.upjs.ics.pro1a.heck.core.AccessToken;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.Specialization;
import sk.upjs.ics.pro1a.heck.core.User;
import sk.upjs.ics.pro1a.heck.db.AccessTokenDao;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;

/**
 *
 * @author raven
 */


@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class HeckResources {
    
    private AccessTokenDao accessTokenDao;
    private DoctorDao doctorDao;
    private UserDao userDao;
    private SpecializationDao specializationDao;
    
    public HeckResources(AccessTokenDao accessTokenDao, DoctorDao doctorDao, UserDao userDao, SpecializationDao specializationDao) {
        this.accessTokenDao = accessTokenDao;
        this.doctorDao = doctorDao;
        this.userDao = userDao;
        this.specializationDao = specializationDao;
    }
    
    /**
     *              USER resource part
     */
     
    @POST
    @Path("/login/user")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginUser(
            @FormParam("login") String login,
            @FormParam("password") String password
    ) {
        System.out.println("hej hou");
        // Try to find a user with the supplied credentials.
        User user = userDao.findUserByLoginAndPassword(login, password);
        if (user == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        
        // User was found, generate a token and return it.
        AccessToken accessToken = accessTokenDao.generateNewUserAccessToken(user, new DateTime());
        return Response.ok(accessToken.getAccessTokenId().toString()).build();
    }
    
    @GET
    @UnitOfWork
    @Path("/user/login/{login}")
    public Response findUserByLogin(@PathParam("login") Optional<String> login) {
        User user = userDao.findUserByLogin(login.get());
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
    
    @GET
    @Path("/user/id/{id}")
    @UnitOfWork
    public Response findUserById(@PathParam("id") Long id) {
        User user = userDao.findUserById(id);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
    
    @GET
    @Path("/users")
    @UnitOfWork
    public Response findAllUsers() {
        List<User> users = userDao.findAllUsers();
        if(users == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(users).build();
    }
    
    @POST
    @Path("/register/user")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response registerDoctor(
            @FormParam("emailUser") String emailUser,
            @FormParam("loginUser") String loginUser,
            @FormParam("password") String password,
            @FormParam("firstNameUser") String firstNameUser,
            @FormParam("lastNameUser") String lastNameUser,
            @FormParam("phoneUser") String phoneUser,
            @FormParam("postalCodeUser") Integer postalCodeUser,
            @FormParam("cityUser") String cityUser,
            @FormParam("addressUser") String addressUser
            
    ) {
        User user = new User(emailUser, loginUser, firstNameUser, lastNameUser, phoneUser,
                postalCodeUser, cityUser, addressUser);
        
        User newUser = userDao.registerUser(user, password);
        if(newUser.getPasswordUser() == null || newUser.getPasswordUser().length() < 6) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        } else {
            newUser = userDao.findUserByLogin(newUser.getLoginUser());
        }
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }
    
    /**
     *             END of USER resource part
     */
    
    /**
     *              DOCTOR resource part
     */
    @POST
    @Path("/login/doctor")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginDcotor(
            @FormParam("login") String login,
            @FormParam("password") String password
    ) {
        System.out.println("hej hou");
        // Try to find a user with the supplied credentials.
        Doctor doctor = doctorDao.findUserByLoginAndPassword(login, password);
        if (doctor == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        
        // User was found, generate a token and return it.
        AccessToken accessToken = accessTokenDao.generateNewDoctorAccessToken(doctor, new DateTime());
        return Response.ok(accessToken.getAccessTokenId().toString()).build();
    }
    
    @GET
    @UnitOfWork
    @Path("/doctor/login/{login}")
    public Response findDoctorByName(@PathParam("login") Optional<String> login) {
        Doctor doctor= doctorDao.findDoctorByLogin(login.get());
        if(doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/doctor/id/{id}")
    @UnitOfWork
    public Response findDoctorById(@PathParam("id") Long id) {
        Doctor doctor= doctorDao.findDoctorById(id);
        if(doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/doctors")
    @UnitOfWork
    public Response findAllDoctors() {
        List<Doctor> doctors = doctorDao.findAllDoctors();
        if(doctors== null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    @POST
    @Path("/doctor/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response registerDoctor(
            @FormParam("emailDoctor") String emailDoctor,
            @FormParam("loginDoctor") String loginDoctor,
            @FormParam("password") String password,
            @FormParam("specializationId") Long specializationId,
            @FormParam("specializationName") String specializationName,
            @FormParam("businessNameDoctor") String businessNameDoctor,
            @FormParam("firstNameDoctor") String firstNameDoctor,
            @FormParam("lastNameDoctor") String lastNameDoctor,
            @FormParam("phoneNumberDoctor") String phoneNumberDoctor,
            @FormParam("postalCodeDoctor") Integer postalCodeDoctor,
            @FormParam("cityDoctor") String cityDoctor,
            @FormParam("addressDoctor") String addressDoctor,
            @FormParam("activationTimeDoctor") Timestamp activationTimeDoctor
    ) {
        Doctor doctor = new Doctor(emailDoctor, loginDoctor, new Specialization(specializationId,
                specializationName), businessNameDoctor, firstNameDoctor, lastNameDoctor,
                phoneNumberDoctor, postalCodeDoctor, cityDoctor, addressDoctor, activationTimeDoctor);
        
        Doctor doc = doctorDao.registerDoctor(doctor, password);
        if(doc.getPasswordDoctor() == null || doc.getPasswordDoctor().length() < 6) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }else {
            doc = doctorDao.findDoctorByLogin(doc.getLoginDoctor());
        }
        return Response.status(Response.Status.CREATED).entity(doc).build();
    }
    
    /**
     *             END of DOCTOR resource part
     */
    
    /**
     *              Specialization resource part
     */
    
    @GET
    @Path("specialization/id/{id}")
    @UnitOfWork
    public Response findSpecializationById(@PathParam("id") Long id) {
        Specialization specialization = specializationDao.findSpecializationById(id);
        if(specialization == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialization).build();
    }
    
    @GET
    @Path("/specializations")
    @UnitOfWork
    public Response findAllSpecialization() {
        List<Specialization> specializations = specializationDao.findAllSpecializations();
        if(specializations == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specializations).build();
    }
    
    /**
     *             END of Specialization resource part
     */
}
