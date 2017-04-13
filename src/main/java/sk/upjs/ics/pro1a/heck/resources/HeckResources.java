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
import sk.upjs.ics.pro1a.heck.core.Token;

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
        if(user == null) {
            System.err.println("Wrong password or login name!");
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        Token token = null;
        if(user.getIsAdmin()) {
            token = new Token(user.getIdUser(), user.getLoginUser(), "Admin");
        } else {
            token = new Token(user.getIdUser(), user.getLoginUser(), "User");
        }
        return Response.ok(token).build();
    }
    
    @GET
    @UnitOfWork
    @Path("/user/login/{login}")
    public Response findUserByLogin(@PathParam("login") Optional<String> login) {
        User user = userDao.findUserByLogin(login.get());
        if(user == null) {
            System.err.println("Can not find user!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(user).build();
        
    }
    
    @GET
    @Path("/user/id/{id}")
    @UnitOfWork
    public Response findUserById(@PathParam("id") Long id) {
        User user = userDao.findUserById(id);
        
        if(user == null) {
            System.err.println("Can not find user!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(user).build();
    }
    
    @GET
    @Path("/users")
    @UnitOfWork
    public Response findAllUsers() {
        List<User> users = userDao.findAllUsers();
        
        if(users == null) {
            System.err.println("Can not find any user!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(users).build();
    }
    
    @POST
    @Path("/register/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerUser(User user) {
        user.setActiveUser(true);
        try {
            User newUser = userDao.registerUser(user, user.getPasswordUser());
            Token token = null;
            if(newUser.getIsAdmin()) {
                token = new Token(newUser.getIdUser(), newUser.getLoginUser(), "Admin");
            } else {
                token = new Token(newUser.getIdUser(), newUser.getLoginUser(), "User");
            }
            return Response.status(Response.Status.CREATED).entity(token).build();
        } catch(Exception e) {
            System.err.println("User can not be created!");
            throw new WebApplicationException(Response.status(Response.Status.RESET_CONTENT).build());
        }
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
        if(doctor == null) {
            System.err.println("Wrong password or login name!");
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        Token token = new Token(doctor.getIdDoctor(), doctor.getLoginDoctor(), "Doctor");
        return Response.ok(token).build();
    }
    
    @GET
    @UnitOfWork
    @Path("/doctor/login/{login}")
    public Response findDoctorByName(@PathParam("login") Optional<String> login) {
        Doctor doctor= doctorDao.findDoctorByLogin(login.get());
        if(doctor == null) {
            System.err.println("Can not find doctor!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/doctor/id/{id}")
    @UnitOfWork
    public Response findDoctorById(@PathParam("id") Long id) {
        Doctor doctor= doctorDao.findDoctorById(id);
        if(doctor == null) {
            System.err.println("Can not find doctor!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        Token token = new Token(doctor.getIdDoctor(), doctor.getLoginDoctor(), "Doctor");
        return Response.ok(token).build();
    }
    
    @GET
    @Path("/doctors")
    @UnitOfWork
    public Response findAllDoctors() {
        List<Doctor> doctors = doctorDao.findAllDoctors();
        if(doctors == null) {
            System.err.println("Can not find any doctor!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(doctors).build();
        
    }
    
    @POST
    @Path("/doctor/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerDoctor(Doctor doctor) {
        doctor.setActiveDoctor(false);
        try {
            Doctor doc = doctorDao.registerDoctor(doctor, doctor.getPasswordDoctor());
            Token token = new Token(doc.getIdDoctor(), doc.getLoginDoctor(), "Dcotor");
            return Response.status(Response.Status.CREATED).entity(token).build();
        } catch(Exception e) {
            System.err.println("Docotr can not be created!");
            throw new WebApplicationException(Response.status(Response.Status.RESET_CONTENT).build());
        }
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
            System.err.println("Can not find any specialization!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(specialization).build();
    }
    
    @GET
    @Path("/specializations")
    @UnitOfWork
    public Response findAllSpecialization() {
        List<Specialization> specializations = specializationDao.findAllSpecializations();
        if(specializations == null) {
            System.err.println("Can not find any specialization!");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.ok(specializations).build();
    }
    
    /**
     *             END of Specialization resource part
     */
    
    
    
}
