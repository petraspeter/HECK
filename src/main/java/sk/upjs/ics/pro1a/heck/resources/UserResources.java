package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.services.UserService;
import sk.upjs.ics.pro1a.heck.services.dto.*;

/**
 *
 * @author Raven
 */
@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class UserResources {    
    
    private final UserService userService;

    public UserResources(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerUser(UserDto userDto) {
        try {
            LoginResponseDto loginResponse = userService.registerUser(userDto);
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
        LoginResponseDto loginResponse = userService.loginAsUser(loginData.getLogin(), loginData.getPassword());
        if (loginResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(loginResponse).build();
    }
    
    @GET
    @Path("/users/{id}")
    @UnitOfWork
    public Response getUserById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        UserDto userDto = userService.getUserById(id);
        if (userDto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(userDto).build();
    }
    
    @GET
    @Path("/users")
    @UnitOfWork
    public Response getAllUsers(@Auth AuthorizedUserDto user) {
        List<UserDto> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @PUT
    @Path("/users/{id}")
    @UnitOfWork
    public Response updateDoctor(@Auth AuthorizedUserDto user, @PathParam("id") Long id, UserDto userDto){
        userService.updateUser(id, userDto);
        return  Response.ok().build();
    }
}
