package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.SpecializationDao;
import sk.upjs.ics.pro1a.heck.services.DoctorService;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;
import sk.upjs.ics.pro1a.heck.services.dto.ChangePasswordDto;
import sk.upjs.ics.pro1a.heck.services.dto.DoctorDto;
import sk.upjs.ics.pro1a.heck.services.dto.IsValidDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginRequestDto;
import sk.upjs.ics.pro1a.heck.services.dto.LoginResponseDto;

/**
 *
 * @author Raven
 */
@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class DoctorResources {
    
    
    private final DoctorService doctorService;
    
    public DoctorResources(DoctorDao doctorDao, SpecializationDao specializationDao, byte[] tokenSecret) {
        this.doctorService = new DoctorService(doctorDao, specializationDao, tokenSecret);
    }
    
    @POST
    @Path("/doctors")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response registerDoctor(DoctorDto doctorDto) {
        LoginResponseDto loginResponse = doctorService.registerDoctor(doctorDto);
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
        LoginResponseDto loginResponse = doctorService.loginAsDoctor(loginData.getLogin(), loginData.getPassword());
        if (loginResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(loginResponse).build();
    }
    
    @GET
    @Path("/doctors/{id}")
    @UnitOfWork
    public Response getDoctorById(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        DoctorDto doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctor).build();
    }
    
    @PUT
    @Path("/doctors/{id}")
    @UnitOfWork
    public Response updateDoctor(@Auth AuthorizedUserDto user, @PathParam("id") Long id, DoctorDto doctorDto){
        doctorService.updateDoctor(id, doctorDto);
        return  Response.ok().build();
    }
    
    @GET
    @Path("/doctors")
    @UnitOfWork
    public Response getAllDoctors(@Auth AuthorizedUserDto user) {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return Response.ok(doctors).build();
    }
        
    @GET
    @Path("/doctors/specialization/{id}")
    @UnitOfWork
    public Response getDoctorsBySpecializationId(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecializationId(id);
        if (doctors == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    
    @GET
    @Path("/doctors/searchP")
    @UnitOfWork
    public Response getDoctorsBySpecializationIdAndPostalCode(
            @Auth AuthorizedUserDto user,
            @QueryParam("id") Long id,
            @QueryParam("pcn") String pcn) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecializationIdAndPostalCode(id, pcn);
        if (doctors == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    @GET
    @Path("/doctors/searchN")
    @UnitOfWork
    public Response getDoctorsBySpecializationIdAndName(
            @Auth AuthorizedUserDto user,
            @QueryParam("id") Long id,
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecializationIdAndName(id, firstName, lastName);
        if (doctors == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    @GET
    @Path("/doctors/searchL")
    @UnitOfWork
    public Response getDoctorsBySpecializationIdAndLastName(
            @Auth AuthorizedUserDto user,
            @QueryParam("id") Long id,
            @QueryParam("lastName") String lastName) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecializationIdAndLastName(id, lastName);
        if (doctors == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    @GET
    @Path("/doctors/search")
    @UnitOfWork
    public Response getDoctorsBySpecializationIdAndNameAndPostalCode(
            @Auth AuthorizedUserDto user,
            @QueryParam("id") Long id,
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("pcn") String pcn) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecializationIdAndNameAndPostalCode(id, firstName,
                lastName, pcn);
        if (doctors == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(doctors).build();
    }
    
    @GET
    @Path("/doctors/searchLP")
    @UnitOfWork
    public Response getDoctorsBySpecializationIdAndLastNameAndPostalCode(
            @Auth AuthorizedUserDto user,
            @QueryParam("id") Long id,
            @QueryParam("lastName") String lastName,
            @QueryParam("pcn") String pcn) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecializationIdAndLastNameAndPostalCode(id,
                lastName, pcn);
        if (doctors == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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
        IsValidDto isValidDto = doctorService.isLoginValid(login);
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
        IsValidDto isValidDto = doctorService.isEmailValid(email, userEmail);
        return Response.ok(isValidDto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/doctors/{id}/changePassword")
    @UnitOfWork
    public Response changePassword(@PathParam("id") Long id, ChangePasswordDto changePasswordDto) {
        try {
            doctorService.changeDoctorPassword(id, changePasswordDto);
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
            return Response.ok(doctorService.checkDoctorPassword(id, password)).build();
    }
}

