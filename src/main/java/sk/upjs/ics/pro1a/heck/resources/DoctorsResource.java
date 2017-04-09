package sk.upjs.ics.pro1a.heck.resources;

import java.util.Optional;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.Consumer;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
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

/**
 *
 * @author raven
 */
@Path("/doctor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorsResource {
    
    private final DoctorDao doctorDao;
    
    public DoctorsResource(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }
    
    @GET
    @UnitOfWork
    @Path("login/{login}")
    public Response findDoctorByName(@Auth Consumer user, @PathParam("login") Optional<String> login) {
        Doctor doctor= doctorDao.findDoctorByLogin(login.get());
        if(doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/id/{id}")
    @UnitOfWork
    public Response findDoctorById(@PathParam("id") Long id) {
        Doctor doctor= doctorDao.findDoctorById(id);
        if(doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @GET
    @Path("/specialization/{id}")
    @UnitOfWork
    public Response findDoctorBySpecialization(@PathParam("id") Long id) {
        List<Doctor> doctors = doctorDao.findAllDoctors();
        if(doctors== null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    @POST
    @Path("register")
    @UnitOfWork
    public Response registerDoctor(Doctor doctor) {
        Doctor doc = doctorDao.registerDoctor(doctor);
        if(doc == null) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
        return Response.status(Response.Status.CREATED).entity(doc).build();
    }
    
}
