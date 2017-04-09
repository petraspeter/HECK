package sk.upjs.ics.pro1a.heck.core;

import java.security.Principal;

/**
 * 
 * @author raven
 */
public class Consumer implements Principal {

    private Long consumerId;

    public Consumer(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Consumer() {
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }
    
    
    
    @Override
    public String getName() {
        return "Some name";
    }

}
