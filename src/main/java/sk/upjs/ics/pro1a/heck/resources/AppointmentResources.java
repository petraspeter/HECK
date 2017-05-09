package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Path("/appointments/day")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findAppointmenstByDoctorIdForDay(
            @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser,
            @QueryParam("date") String date
    ) throws ParseException {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp ts = new Timestamp(sdt.parse(date).getTime());
        List<AppointmentDto> appointments = appointmentService.generateUserAppointmentForDays(idDoc,
                idUser, ts, ts);
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path("/appointments/days")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findAppointmenstByDoctorIdForDays(
            @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser,
            @QueryParam("dateFrom") String dateFrom,
            @QueryParam("dateTo") String dateTo
    ) throws ParseException {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp tsFrom = new Timestamp(sdt.parse(dateFrom).getTime());
        Timestamp tsTo = new Timestamp(sdt.parse(dateTo).getTime());
        List<AppointmentDto> appointments = appointmentService
                .generateUserAppointmentForDays(idDoc, idUser, tsFrom, tsTo);
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path("/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findAppointmenstByDoctorId(
            @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser
    ) throws ParseException {
        Long actualDay = DateUtils.truncate(new Date(), Calendar.DATE).getTime();
        Timestamp tsFrom = new Timestamp(actualDay);
        Timestamp tsTo = new Timestamp(actualDay+(6*24*60*60*1000));
        List<AppointmentDto> appointments = appointmentService
                .generateUserAppointmentForDays(idDoc, idUser, tsFrom, tsTo);
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path("/doctors/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findDoctorsAppointment(
            @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc
    ) {
        Long actualDay = DateUtils.truncate(new Date(), Calendar.DATE).getTime();
        Timestamp tsFrom = new Timestamp(actualDay);
        Timestamp tsTo = new Timestamp(actualDay+(6*24*60*60*1000));
        List<AppointmentDto> appointments = appointmentService
                .generateDoctorAppointmentForDays(idDoc, 0L, tsFrom, tsTo);
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path("/doctors/appointmentsDate")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findDoctorsAppointmentFromTo(
            @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long id,
            @QueryParam("from") String from,
            @QueryParam("to") String to
    ) throws ParseException {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp tsFrom = new Timestamp(sdt.parse(from).getTime());
        Timestamp tsTo = new Timestamp(sdt.parse(to).getTime());
        List<AppointmentDto> appointments = appointmentService
                .generateDoctorAppointmentForDays(id, 0L, tsFrom, tsTo);
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
    public Response getUserAppointment(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        List<AppointmentDto> appointments = appointmentService.getUserAppointment(id);
        if (appointments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(appointments).build();
    }
    
    @GET
    @Path("/users/future/{id}")
    @UnitOfWork
    public Response getFutureUserAppointment(@Auth AuthorizedUserDto user, @PathParam("id") Long id) {
        List<AppointmentDto> appointments = appointmentService.getFutureUserAppointment(id);
        if (appointments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(appointments).build();
    }
    
}
