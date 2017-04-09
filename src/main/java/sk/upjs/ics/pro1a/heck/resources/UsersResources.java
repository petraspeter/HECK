package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.core.Consumer;
import sk.upjs.ics.pro1a.heck.core.User;
import sk.upjs.ics.pro1a.heck.db.UserDao;

/**
 *
 * @author raven
 */

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResources {
    
    private UserDao userDao;
    
    public UsersResources(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @GET
    @UnitOfWork
    @Path("login/{login}")
    public Response findUserByLogin(@Auth Consumer consumer, @PathParam("login") Optional<String> login) {
        User user = userDao.findUserByLogin(login.get());
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
    
    @GET
    @Path("/id/{id}")
    @UnitOfWork
    public Response findUserById(@PathParam("id") Long id) {
        User user = userDao.findUserById(id);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
    
    @GET
    @UnitOfWork
    public Response findAllUsers() {
        List<User> users = userDao.findAllUsers();
        if(users == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(users).build();
    }
    
    @POST
    @Path("register")
    @UnitOfWork
    public Response registerDoctor(User user) {
        User newUser = userDao.registerUser(user);
        if(newUser == null) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }
    
}
