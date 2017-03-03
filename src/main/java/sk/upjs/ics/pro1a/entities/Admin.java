package sk.upjs.ics.pro1a.entities;

/**
 *
 * @author raven
 */
public class Admin {
    
    
    private Long idAdmin;
    
    private String emailAdmin;
    
    private String passwordAdmin;
    
    private String hashAdmin;
    
    /**
     *
     * @return ID admina
     */
    public Long getIdAdmin() {
        return idAdmin;
    }
    
    /**
     *
     *  @param idAdmin identifikacne cislo admina
     */
    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    /**
     *
     * @return email admina
     */
    public String getEmailAdmin() {
        return emailAdmin;
    }
    
    /**
     *
     *  @param emailAdmin emailova adresa admina
     */
    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }
    
    /**
     *
     * @return heslo admina
     */
    public String getPasswordAdmin() {
        return passwordAdmin;
    }
    
    /**
     *
     *  @param passwordAdmin heslo admina
     */
    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }
    
    /**
     *
     * @return Hash kod admina
     */
    public String getHashAdmin() {
        return hashAdmin;
    }
    
    /**
     *
     *  @param hashAdmin Hash kod admina
     */
    public void setHashAdmin(String hashAdmin) {
        this.hashAdmin = hashAdmin;
    }
    
    /**
     *
     * @param idAdmin
     * @param emailAdmin
     * @param passwordAdmin
     * @param hashAdmin
     */
    public Admin(Long idAdmin, String emailAdmin, String passwordAdmin, String hashAdmin) {
        this.idAdmin = idAdmin;
        this.emailAdmin = emailAdmin;
        this.passwordAdmin = passwordAdmin;
        this.hashAdmin = hashAdmin;
    }
    
    /**
     *
     */
    public Admin() {
    }
    
    
}
