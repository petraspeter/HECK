package sk.upjs.ics.pro1a.heck.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;
import org.jose4j.jwt.NumericDate;

public class AuthorizedUserDto implements Principal {

    @JsonProperty("id")
    private long id;

    @JsonProperty("login")
    private String login;

    @JsonProperty("role")
    private String role;
    
    public AuthorizedUserDto() {
    }
    
    public AuthorizedUserDto(long id, String login, String role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
       
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getName() {
        return login;
    }
    
}
