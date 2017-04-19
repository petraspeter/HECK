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
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.SpecializationDao;
import sk.upjs.ics.pro1a.heck.services.HeckService;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;
import sk.upjs.ics.pro1a.heck.services.dto.DoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginRequestDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;

/**
 *
 * @author Raven
 */
@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class DoctorResources {
    
    
    private final HeckService heckService;
    
    public DoctorResources(DoctorDao doctorDao, SpecializationDao specializationDao, byte[] tokenSecret) {
        this.heckService = new HeckService(doctorDao, specializationDao, tokenSecret);
    }
    
    @POST
    @Path("/doctors")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerDoctor(DoctorDto doctorDto) {
        LoginResponseDto loginResponse = heckService.registerDoctor(doctorDto);
        if(loginResponse != null) {
            return Response.status(Response.Status.CREATED).entity(loginResponse).build();
        } else  {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
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
    
    @GET
    @Path("/doctors/{id}")
    @UnitOfWork
    public Response getDoctorById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        DoctorDto doctor = heckService.getDoctorById(id);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/doctors")
    @UnitOfWork
    public Response getAllDoctors(@Auth AuthorizedUserDto user) {
        List<DoctorDto> doctors = heckService.getAllDoctors();
        return Response.ok(doctors).build();
    }
    
}
