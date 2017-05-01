package sk.upjs.ics.pro1a.heck.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.upjs.ics.pro1a.heck.db.AppointmentDao;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;
import sk.upjs.ics.pro1a.heck.db.WorkingTimeDao;
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
    
    public AppointmentResources(AppointmentDao appointmentDao, DoctorDao doctorDao,
            UserDao userDao, WorkingTimeDao workingTimeDao, byte[] tokenSecret) {
        this.appointmentService = new AppointmentService(appointmentDao, doctorDao, userDao,
                workingTimeDao, tokenSecret);
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
    
    @GET
    @Path("/appointments/day")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findAppointmenstByDoctorIdForDay(
            //  @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser,
            @QueryParam("date") String date
    ) throws ParseException {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp ts = new Timestamp(sdt.parse(date).getTime());
        
        List<AppointmentDto> appointments = appointmentService.generateDoctorAppointmentForDay(idDoc, idUser, ts);
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
            //  @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser,
            @QueryParam("dateFrom") String dateFrom,
            @QueryParam("dateTo") String dateTo
    ) throws ParseException {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp tsFrom = new Timestamp(sdt.parse(dateFrom).getTime());
        Timestamp tsTo = new Timestamp(sdt.parse(dateTo).getTime());
        
        List<AppointmentDto> appointments = appointmentService
                .generateDoctorAppointmentForDays(idDoc, idUser, tsFrom, tsTo);
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
            //  @Auth AuthorizedUserDto user,
            @QueryParam("idDoc") Long idDoc,
            @QueryParam("idUser") Long idUser
    ) throws ParseException {
        Timestamp tsFrom = new Timestamp(System.currentTimeMillis());
        Timestamp tsTo = new Timestamp(System.currentTimeMillis()+(7*24*60*60*1000));
        List<AppointmentDto> appointments = appointmentService
                .generateDoctorAppointmentForDays(idDoc, idUser, tsFrom, tsTo);
        if (appointments.size() > 0) {
            return  Response.ok(appointments).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
  
    
    
}
