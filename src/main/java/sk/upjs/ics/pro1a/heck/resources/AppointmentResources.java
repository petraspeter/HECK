package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.time.DateUtils;
import sk.upjs.ics.pro1a.heck.db.core.Appointment;
import sk.upjs.ics.pro1a.heck.services.AppointmentService;
import sk.upjs.ics.pro1a.heck.services.dto.AppointmentDto;
import sk.upjs.ics.pro1a.heck.services.dto.AuthorizedUserDto;

/**
 *
 * @author Raven
 */
@Path("/heck")
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentResources {
    
    private final AppointmentService appointmentService;
    
    public AppointmentResources(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    @POST
    @Path("/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response addAppointment(AppointmentDto appointmentDto) {
        System.out.println(appointmentDto.toString());
        AppointmentDto addedAppointment = appointmentService.addAppointment(appointmentDto);
        if(addedAppointment != null) {
            return Response.status(Response.Status.CREATED).entity(addedAppointment).build();
        } else  {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
    
    @POST
    @Path("/appointments/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateAppointment(AppointmentDto appointmentDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(appointmentDto);
        if(updatedAppointment != null) {
            return Response.status(Response.Status.OK).entity(updatedAppointment).build();
        } else  {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
    
    @GET
    @Path("/users/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findAppointmenstByDoctorId(
         //   @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser,
            @QueryParam("dateFrom") String dateFrom,
            @QueryParam("dateTo") String dateTo
    ) throws ParseException {
        List<AppointmentDto> appointments = new ArrayList<>();
        Timestamp tsFrom = null;
        Timestamp tsTo = null;
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tsFrom = new Timestamp(sdt.parse(dateFrom).getTime());
        } catch (Exception e) {
            System.err.println("DateFrom param is missing!");
        }
        try {
            tsTo = new Timestamp(sdt.parse(dateTo).getTime());
        } catch (Exception e) {
            System.err.println("DateTo param is missing!");
        }
        if (tsFrom != null && tsTo != null) {
            appointments = appointmentService
                    .generateUserAppointmentForDays(idDoc, idUser, tsFrom, tsTo);
        } else if(tsFrom == null && tsTo == null) {
            appointments = appointmentService.generateDefaultAppointments(idDoc, idUser);
        } else {
            if (tsFrom != null) {
                appointments = appointmentService
                        .generateUserAppointmentForDays(idDoc, idUser, tsFrom, tsFrom);
            } else {
                appointments = appointmentService
                        .generateUserAppointmentForDays(idDoc, idUser, tsTo, tsTo);
            }
        }
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    /*
    Metoda je len pre doktora
    */
    @GET
    @Path("/doctors/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findDoctorsAppointment(
            @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("from") String from,
            @QueryParam("to") String to
    )  throws ParseException{
        List<AppointmentDto> appointments = new ArrayList<>();
        Timestamp tsTo = null;
        Timestamp tsFrom = null;
        if(from == null || to == null) {
            Long actualDay = DateUtils.truncate(new Date(), Calendar.DATE).getTime();
            tsFrom = new Timestamp(actualDay);
            tsTo = new Timestamp(actualDay+(6*24*60*60*1000));
        } else {
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            tsFrom = new Timestamp(sdt.parse(from).getTime());
            tsTo= new Timestamp(sdt.parse(to).getTime());
        }
        appointments = appointmentService
                .generateDoctorAppointmentForDays(idDoc, tsFrom, tsTo);
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path("/appointments/{page}/{size}")
    @UnitOfWork
    public Response getPage(@Auth AuthorizedUserDto user, @PathParam("page") int page, @PathParam("size") int size) {
        List<AppointmentDto> appointments = appointmentService.findForPage(page, size);
        if (appointments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(appointments).build();
    }
    
    @GET
    @Path("/appointments/delete/{id}")
    @UnitOfWork
    public Response deleteAppointment(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        appointmentService.deleteAppointment(id);
        Appointment appointment = appointmentService.getById(id);
        if (appointment.getIdAppointment() == null) {
            return Response.ok("Appointment deleted").build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
    }
    
    @GET
    @Path("/users/appointments/{id}")
    @UnitOfWork
    public Response getUserAppointment(
      //      @Auth AuthorizedUserDto user,
            @PathParam("id") Long id) {
        List<AppointmentDto> appointments = appointmentService.getUserAppointment(id);
        if (appointments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(appointments).build();
    }
    
    @GET
    @Path("/users/future/{id}")
    @UnitOfWork
    public Response getFutureUserAppointment(
            @Auth AuthorizedUserDto user,
            @PathParam("id") Long id) {
        List<AppointmentDto> appointments = appointmentService.getFutureUserAppointment(id);
        if (appointments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(appointments).build();
    }
    
}
