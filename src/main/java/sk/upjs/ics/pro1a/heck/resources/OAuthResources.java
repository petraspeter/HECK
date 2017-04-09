package sk.upjs.ics.pro1a.heck.resources;

import com.google.common.collect.ImmutableList;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;
import sk.upjs.ics.pro1a.heck.core.Doctor;
import sk.upjs.ics.pro1a.heck.core.Token;
import sk.upjs.ics.pro1a.heck.core.User;
import sk.upjs.ics.pro1a.heck.db.DoctorDao;
import sk.upjs.ics.pro1a.heck.db.TokenDao;
import sk.upjs.ics.pro1a.heck.db.UserDao;

/**
 *
 * @author raven
 */

@Path("/auth/token")
@Produces(MediaType.APPLICATION_JSON)
public class OAuthResources {
    
    // private ImmutableList<String> allowedGrantTypes;
    private TokenDao tokenDao;
    private UserDao userDao;
    private DoctorDao doctorDao;
    
    /*
    public OAuthResources(ImmutableList<String> allowedGrantTypes, TokenDao tokenDao, UserDao userDao, DoctorDao doctorDao) {
    this.allowedGrantTypes = allowedGrantTypes;
    this.tokenDao = tokenDao;
    this.userDao = userDao;
    this.doctorDao = doctorDao;
    }
    */
    
    public OAuthResources(TokenDao tokenDao, UserDao userDao, DoctorDao doctorDao) {
        this.tokenDao = tokenDao;
        this.userDao = userDao;
        this.doctorDao = doctorDao;
    }
    
    
    
    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String postForToken(
            //     @FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("consumer") String clientId
    ) {
        
        /**
         * check role
         * if (!allowedGrantTypes.contains(grantType)) {
         * Response response = Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
         * throw new WebApplicationException(response);
         * }
         */
        
        /**
         * finding doctor
         */
        
        Doctor doc = doctorDao.findUserByLoginAndPassword(username, password);
        if(doc == null) {
            /**
             * finding user because doctor was not founded
             */
            User user = userDao.findUserByLoginAndPassword(username, password);
            if (user == null) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
            } else {
                /**
                 * generate token for finded user
                 */
                Token token = tokenDao.generateNewTokenUser(user, new DateTime());
                return token.getTokenId().toString();
            }
        } else {
            Token token = tokenDao.generateNewTokenDoctor(doc, new DateTime());
            return token.getTokenId().toString();
        }
    }
    
}
