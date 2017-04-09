package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.core.Specialization;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;

/**
 *
 * @author raven
 */

@Path("/specialization")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecializationsResources {
    
    private final SpecializationDao specializationDao;
    
    public SpecializationsResources(SpecializationDao specializationDao) {
        this.specializationDao = specializationDao;
    }
    
    @GET
    @Path("/id/{id}")
    @UnitOfWork
    public Response findSpecializationById(@PathParam("id") Long id) {
        Specialization specialization = specializationDao.findSpecializationById(id);
        if(specialization == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialization).build();
    }
    
    
    @GET
    @UnitOfWork
    public Response findAllSpecialization() {
        List<Specialization> specializations = specializationDao.findAllSpecializations();
        if(specializations == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specializations).build();
    }
    
}
