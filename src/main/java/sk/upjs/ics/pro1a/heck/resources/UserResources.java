package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.repositories.UserDao;
import sk.upjs.ics.pro1a.heck.services.HeckService;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginRequestDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;
import sk.upjs.ics.pro1a.heck.services.dto.UserDto;

/**
 *
 * @author Raven
 */
@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class UserResources {    
    
    private final HeckService heckService;
    
    public UserResources(UserDao userDao, byte[] tokenSecret) {
        this.heckService = new HeckService(userDao, tokenSecret);
    }
    
    @POST
    @Path("/users")
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
    
    @GET
    @Path("/users/{id}")
    @UnitOfWork
    public Response getUserById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        UserDto userDto = heckService.getUserById(id);
        if (userDto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(userDto).build();
    }
    
    @GET
    @Path("/users")
    @UnitOfWork
    public Response getAllUsers(@Auth AuthorizedUserDto user) {
        List<UserDto> users = heckService.getAllUsers();
        return Response.ok(users).build();
    }
    
}
