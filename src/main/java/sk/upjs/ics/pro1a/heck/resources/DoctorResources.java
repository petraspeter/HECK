package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import sk.upjs.ics.pro1a.heck.repositories.DoctorDao;
import sk.upjs.ics.pro1a.heck.repositories.SpecializationDao;
import sk.upjs.ics.pro1a.heck.services.HeckService;
import sk.upjs.ics.pro1a.heck.services.dto.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    
    @PUT
    @Path("/doctors/{id}")
    @UnitOfWork
    public Response updateDoctor(@Auth AuthorizedUserDto user, @PathParam("id") Long id, DoctorDto doctorDto){
        heckService.updateDoctor(id, doctorDto);        
        return  Response.ok().build();
    }
        
    @GET
    @Path("/doctors")
    @UnitOfWork
    public Response getAllDoctors(@Auth AuthorizedUserDto user) {
        List<DoctorDto> doctors = heckService.getAllDoctors();
        return Response.ok(doctors).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/doctors/checkLogin")
    @UnitOfWork
    public Response isLoginValid(@FormParam("login") String login) {
        if (login == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        IsValidDto isValidDto = heckService.isLoginValid(login);
        return Response.ok(isValidDto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/doctors/checkEmail")
    @UnitOfWork
    public Response isEmailValid(@FormParam("email") String email, @FormParam("userEmail") String userEmail) {
        if (email == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        IsValidDto isValidDto = heckService.isEmailValid(email, userEmail);
        return Response.ok(isValidDto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/doctors/{id}/changePassword")
    @UnitOfWork
    public Response changePassword(@PathParam("id") Long id, ChangePasswordDto changePasswordDto) {
        try {
            heckService.changeDoctorPassword(id, changePasswordDto);
            return Response.ok().build();
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/doctors/{id}/checkPassword")
    @UnitOfWork
    public Response checkPassword(@PathParam("id") Long id, @FormParam("currentModalPassword") String password) {
            return Response.ok(heckService.checkDoctorPassword(id, password)).build();
    }
    
}
