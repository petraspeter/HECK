package sk.upjs.ics.pro1a.heck.resources;

import sk.upjs.ics.pro1a.heck.services.dto.SpecializationDto;
import sk.upjs.ics.pro1a.heck.services.dto.DoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.UserDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginRequestDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.SpecializationDao;
import sk.upjs.ics.pro1a.heck.repositories.UserDao;
import sk.upjs.ics.pro1a.heck.services.HeckService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;

/**
 * @author raven
 */

@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class HeckResources {
    
    private final HeckService heckService;
    
    public HeckResources(DoctorDao doctorDao, UserDao userDao, SpecializationDao specializationDao, byte[] tokenSecret) {
        this.heckService = new HeckService(doctorDao, specializationDao, userDao, tokenSecret);
    }
    
    /**
     *      POST methods
     */
    
    /**
     *
     * @param userDto
     * @return
     */
    @POST
    @Path("/register/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerUser(UserDto userDto) {
        try {
            LoginResponseDto loginResponse = heckService.registerUser(userDto);
            return Response.status(Response.Status.CREATED).entity(loginResponse).build();
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
    
    @POST
    @Path("/register/doctor")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerDoctor(DoctorDto doctorDto) {
        try {
            LoginResponseDto loginResponse = heckService.registerDoctor(doctorDto);
            return Response.status(Response.Status.CREATED).entity(loginResponse).build();
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
    
    @POST
    @Path("/login/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response loginUser(LoginRequestDto loginData) {
        // Try to find a user with the supplied credentials.
        LoginResponseDto loginResponse = heckService.loginAsUser(loginData.getLogin(), loginData.getPassword());
        if (loginResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(loginResponse).build();
    }
    
    @POST
    @Path("/login/doctor")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response loginAsDoctor(LoginRequestDto loginData) {
        // Try to find a doctor with the supplied credentials.
        LoginResponseDto loginResponse = heckService.loginAsDoctor(loginData.getLogin(), loginData.getPassword());
        if (loginResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(loginResponse).build();
    }
    
    /**
     *      GET methods
     */
    
    /**
     *
     * @param user
     * @param id
     * @return
     */
    @GET
    @Path("/user/{id}")
    @UnitOfWork
    public Response getUserById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        updateToken(user);
        UserDto userDto = heckService.getUserById(id);
        if (userDto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(userDto).build();
    }
    
    @GET
    @Path("/users")
    @UnitOfWork
    public Response getAllUsers(@Auth AuthorizedUserDto user, @QueryParam("login") String login) {
        updateToken(user);
        if (login != null) {
            UserDto userDto = heckService.getUserByLogin(login);
            if (userDto == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(userDto).build();
        } else {
            List<UserDto> users = heckService.getAllUsers();
            return Response.ok(users).build();
        }
    }
    
    @GET
    @Path("/doctors/{id}")
    @UnitOfWork
    public Response getDoctorById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        updateToken(user);        
        DoctorDto doctor = heckService.getDoctorById(id);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/doctors")
    @UnitOfWork
    public Response getAllDoctors(@Auth AuthorizedUserDto user, @QueryParam("login") String login) {
        updateToken(user);
        LoginResponseDto loginResponse = heckService.updateDoctorsToken(user.getId(), user.getName(),
                user.getRole());
        Response.accepted(loginResponse).build();
        if (login != null) {
            DoctorDto doctor = heckService.getDoctorByLogin(login);
            if (doctor == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(doctor).build();
        } else {
            List<DoctorDto> doctors = heckService.getAllDoctors();
            return Response.ok(doctors).build();
        }
    }
    
    @GET
    @Path("specializations/{id}")
    @UnitOfWork
    public Response getSpecializationById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        updateToken(user);
        SpecializationDto specialization = heckService.getSpecializationById(id);
        if (specialization == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialization).build();
    }
    
    @GET
    @Path("/specializations")
    @UnitOfWork
    public Response getAllSpecializations() {
        List<SpecializationDto> specializations = heckService.getAllSpecializations();
        return Response.ok(specializations).build();
    }
    
    /**
     *      PRIVATE METHODS
     */
    
    
    private void updateToken(AuthorizedUserDto user) {
        switch (user.getRole()) {
            case "user":
                {
                    LoginResponseDto loginResponse = heckService.updateUsersToken(user.getId(), user.getName(),
                            user.getRole());
                    Response.accepted(loginResponse).build();
                    break;
                }
            case "doctor":
                {
                    LoginResponseDto loginResponse = heckService.updateDoctorsToken(user.getId(), user.getName(),
                            user.getRole());
                    Response.accepted(loginResponse).build();
                    break;
                }
            case "admin":
                {
                    LoginResponseDto loginResponse = heckService.updateDoctorsToken(user.getId(), user.getName(),
                            user.getRole());
                    Response.accepted(loginResponse).build();
                    break;
                }
            default:
                break;
        }
    }
    
}
