package sk.upjs.ics.pro1a.heck;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raven
 */
public class LoginParser {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    private JsonNode json;
    
    public LoginParser(String inputJson) {
        try {
            json = objectMapper.readValue(inputJson, JsonNode.class);
        } catch (IOException ex) {
            Logger.getLogger(LoginParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getLogin() {
        try {
            return objectMapper.readValue(json.get("login").toString(), String.class);
        } catch (IOException ex) {
            Logger.getLogger(LoginParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getPassword() {
        try {
            return objectMapper.readValue(json.get("password").toString(), String.class);
        } catch (IOException ex) {
            Logger.getLogger(LoginParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
}
