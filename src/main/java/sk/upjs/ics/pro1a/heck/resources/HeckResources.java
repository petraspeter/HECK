package sk.upjs.ics.pro1a.heck.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.SpecializationDao;
import sk.upjs.ics.pro1a.heck.repositories.UserDao;
import sk.upjs.ics.pro1a.heck.services.HeckService;
import javax.ws.rs.core.MediaType;

/**
 * @author raven
 */

@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class HeckResources {
    
    private final HeckService heckService;
    
    public HeckResources(DoctorDao doctorDao, UserDao userDao, SpecializationDao specializationDao,
            byte[] tokenSecret) {
        this.heckService = new HeckService(doctorDao, specializationDao, userDao, tokenSecret);
    }
    
    /*
        
    @GET
    @Path("/updateToken/{actualTime}")
    @UnitOfWork
    public Response updateToken(@Auth AuthorizedUserDto user, @PathParam("actualTime") Long actualExpirationTime) {
    switch (user.getRole()) {
    case "user":
    {
    LoginResponseDto loginResponse = heckService.updateUsersToken( user.getName(), user.getRole(),
    actualExpirationTime);
    return  Response.ok(loginResponse).build();
    }
    case "doctor":
    {
    LoginResponseDto loginResponse = heckService.updateDoctorsToken( user.getName(), user.getRole(),
    actualExpirationTime);
    return  Response.ok(loginResponse).build();
    }
    case "admin":
    {
    LoginResponseDto loginResponse = heckService.updateDoctorsToken( user.getName(), user.getRole(),
    actualExpirationTime);
    return  Response.ok(loginResponse).build();
    }
    default:
    break;
    }
    return  Response.status(Response.Status.NO_CONTENT).build();
    
    }
    
    
    */
    
    
    
}
