package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.services.SpecializationService;
import sk.upjs.ics.pro1a.heck.services.dto.SpecializationDto;

/**
 *
 * @author Raven
 */
@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class SpecializationResources {
    
    private final SpecializationService specializationService;
    
    public SpecializationResources(SpecializationDao specializationDao, byte[] tokenSecret) {
        this.specializationService = new SpecializationService(specializationDao);
    }
    
    @GET
    @Path("specializations/{id}")
    @UnitOfWork
    public Response getSpecializationById(@PathParam("id") Long id) {
        SpecializationDto specialization = specializationService.getSpecializationById(id);
        if (specialization == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.accepted(specialization).build();
    }
    
    @GET
    @Path("/specializations")
    @UnitOfWork
    public Response getAllSpecializations() {
        List<SpecializationDto> specializations = specializationService.getAllSpecializations();
        return Response.ok(specializations).build();
    }
    
}
