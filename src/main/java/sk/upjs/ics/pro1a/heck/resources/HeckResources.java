package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.hibernate.UnitOfWork;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.Specialization;
import sk.upjs.ics.pro1a.heck.core.User;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.LoginParser;

/**
 *
 * @author raven
 */


@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class HeckResources {
    
    
    private DoctorDao doctorDao;
    private UserDao userDao;
    private SpecializationDao specializationDao;
    
    public HeckResources(DoctorDao doctorDao, UserDao userDao,
            SpecializationDao specializationDao) {
        this.doctorDao = doctorDao;
        this.userDao = userDao;
        this.specializationDao = specializationDao;
    }
    
    /**
     *              USER resource part
     */
    
    @POST
    @Path("/login/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response loginUser(String input) {
        LoginParser loginParser = new LoginParser(input);
        User user = userDao.findUserByLoginAndPassword(loginParser.getLogin(), loginParser.getPassword());
        if (user == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        return Response.ok(user).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerUser(User user) {
        user.setActiveUser(true);
        User newUser = userDao.registerUser(user, user.getPasswordUser());
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
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response loginDcotor(String input) throws NoSuchAlgorithmException {
        LoginParser loginParser = new LoginParser(input);
        Doctor doctor = doctorDao.findDoctorByLoginAndPassword(loginParser.getLogin(),
                loginParser.getPassword());
        if (doctor == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        return Response.ok(doctor).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerDoctor(Doctor doctor) {        
        doctor.setActiveDoctor(false);        
        Doctor doc = doctorDao.registerDoctor(doctor, doctor.getPasswordDoctor());        
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
    
    /**
     *              Toast shaman
     */
    
    
}
