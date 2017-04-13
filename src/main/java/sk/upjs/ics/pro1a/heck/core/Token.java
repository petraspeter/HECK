package sk.upjs.ics.pro1a.heck.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Raven
 */
public class Token {
    
    private Long id;
    
    private String login;
    
    private String role;
    
    public Token(Long id, String login, String role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }
    
    @JsonProperty
    public Long getId() {
        return id;
    }
    
    @JsonProperty
    public String getLogin() {
        return login;
    }
    
    @JsonProperty
    public String getRole() {
        return role;
    }
    
    
    
    
}
