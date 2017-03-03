package sk.upjs.ics.pro1a.entities;

import java.sql.Timestamp;

/**
 *
 * @author raven
 */
public class Doctor {
    
    private Long idDoc;
    
    private String emailDoc;
    
    private String passwordDoc;
    
    private String hashDoc;
    
    private String phoneNumberDoc;
    
    private String businessNameDoc;
    
    private Long postcodeDoc;
    
    private String cityDoc;
    
    private String addressDoc;
    
    private Boolean  activeDoc;
    
    private Boolean selectedForDeactivationDoc;
    
    private Timestamp deactivationTimeDoc;
    
    private Double longitudeDoc;
    
    private Double latitudeDoc;
    
    /**
     *
     * @return  ID doktora
     */
    public Long getIdDoc() {
        return idDoc;
    }
    
    /**
     *
     *  @param idDoc identifikacne cislo doktora
     */
    public void setIdDoc(Long idDoc) {
        this.idDoc = idDoc;
    }
    
    /**
     *
     * @return  email doktora
     */
    public String getEmailDoc() {
        return emailDoc;
    }
    
    /**
     *
     *  @param  emailDoc emailova adresa doktora
     */
    public void setEmailDoc(String emailDoc) {
        this.emailDoc = emailDoc;
    }
    
    /**
     *
     * @return heslo doktora
     */
    public String getPasswordDoc() {
        return passwordDoc;
    }
    
    /**
     *
     *  @param passwordDoc heslo doktora
     */
    public void setPasswordDoc(String passwordDoc) {
        this.passwordDoc = passwordDoc;
    }
    
    /**
     *
     * @return Hash kod doktora
     */
    public String getHashDoc() {
        return hashDoc;
    }
    
    /**
     *
     *  @param hashDoc Hash kod doktora
     */
    public void setHashDoc(String hashDoc) {
        this.hashDoc = hashDoc;
    }
    
    /**
     *
     * @return telefonne cislo doktora
     */
    public String getPhoneNumberDoc() {
        return phoneNumberDoc;
    }
    
    /**
     *
     *  @param  phoneNumberDoc telefonne cislo doktora
     */
    public void setPhoneNumberDoc(String phoneNumberDoc) {
        this.phoneNumberDoc = phoneNumberDoc;
    }
    
    /**
     *
     * @return obchodne meno doktora(ordinacie)
     */
    public String getBusinessNameDoc() {
        return businessNameDoc;
    }
    /**
     *
     * @param businessNameDoc obchodne meno doktora(ordinacie)
     */
    public void setBusinessNameDoc(String businessNameDoc) {
        this.businessNameDoc = businessNameDoc;
    }
    
    /**
     *
     * @return ci je doktor aktivny
     */
    public Boolean getActiveDoc() {
        return activeDoc;
    }
    
    /**
     *
     * @param activeDoc stav doktora: aktivny/neaktivny
     */
    public void setActiveDoc(Boolean activeDoc) {
        this.activeDoc = activeDoc;
    }
    
    /**
     *
     * @return ci bude doktor deaktivovany
     */
    public Boolean getSelectedForDeactivationDoc() {
        return selectedForDeactivationDoc;
    }
    
    /**
     *
     * @param selectedForDeactivationDoc oznacenie doktora, ktory od urciteho datumu nebude vyzuivat aplikaciu
     */
    public void setSelectedForDeactivationDoc(Boolean selectedForDeactivationDoc) {
        this.selectedForDeactivationDoc = selectedForDeactivationDoc;
    }
    
    /**
     *
     * @return datum dna, kedy bude doktor deaktivovany
     */
    public Timestamp getDeactivationTimeDoc() {
        return deactivationTimeDoc;
    }
    
    /**
     *
     * @param deactivationTimeDoc datum dna, v ktory bude doktor deaktivovany
     */
    public void setDeactivationTimeDoc(Timestamp deactivationTimeDoc) {
        this.deactivationTimeDoc = deactivationTimeDoc;
    }
    
    /**
     *
     * @return zemepisnu dlzku ordinacie
     */
    public Double getLongitudeDoc() {
        return longitudeDoc;
    }
    
    /**
     *
     * @param longitudeDoc zemepisna sirka ordinacie
     */
    public void setLongitudeDoc(Double longitudeDoc) {
        this.longitudeDoc = longitudeDoc;
    }
    
    /**
     *
     * @return zemepisnu sirku ordinacie
     */
    public Double getLatitudeDoc() {
        return latitudeDoc;
    }
    
    /**
     *
     * @param latitudeDoc zemepisna sirka ordinacie
     */
    public void setLatitudeDoc(Double latitudeDoc) {
        this.latitudeDoc = latitudeDoc;
    }
    
    /**
     *
     * @return postove smerove cislo mesta v ktorom sa nachadza ordinacia
     */
    public Long getPostcodeDoc() {
        return postcodeDoc;
    }
    
    /**
     *
     * @param postcodeDoc  postove smerove cislo mesta v ktorom sa nachadza ordinacia
     */
    public void setPostcodeDoc(Long postcodeDoc) {
        this.postcodeDoc = postcodeDoc;
    }
    
    /**
     *
     * @return nazov mesta v ktorom sa nachadza ordinacia
     */
    public String getCityDoc() {
        return cityDoc;
    }
    
    /**
     *
     * @param cityDoc nazov mesta v ktorom sa nachadza ordinacia
     */
    public void setCityDoc(String cityDoc) {
        this.cityDoc = cityDoc;
    }
    
    /**
     *
     * @return konkretnu adresu ordinacie
     */
    public String getAddressDoc() {
        return addressDoc;
    }
    
    /**
     *
     * @param addressDoc konkretna adresa ordinacie
     */
    public void setAddressDoc(String addressDoc) {
        this.addressDoc = addressDoc;
    }
    
    /**
     *
     * @param idDoc
     * @param emailDoc
     * @param passwordDoc
     * @param hashDoc
     * @param phoneNumberDoc
     * @param businessNameDoc
     * @param activeDoc
     * @param selectedForDeactivationDoc
     * @param deactivationTimeDoc
     * @param longitudeDoc
     * @param latitudeDoc
     * @param postcodeDoc
     * @param cityDoc
     * @param addressDoc
     */
    public Doctor(Long idDoc, String emailDoc, String passwordDoc, String hashDoc,
            String phoneNumberDoc, String businessNameDoc, Boolean activeDoc,
            Boolean selectedForDeactivationDoc, Timestamp deactivationTimeDoc, Double longitudeDoc,
            Double latitudeDoc, Long postcodeDoc, String cityDoc, String addressDoc) {
        this.idDoc = idDoc;
        this.emailDoc = emailDoc;
        this.passwordDoc = passwordDoc;
        this.hashDoc = hashDoc;
        this.phoneNumberDoc = phoneNumberDoc;
        this.businessNameDoc = businessNameDoc;
        this.activeDoc = activeDoc;
        this.selectedForDeactivationDoc = selectedForDeactivationDoc;
        this.deactivationTimeDoc = deactivationTimeDoc;
        this.longitudeDoc = longitudeDoc;
        this.latitudeDoc = latitudeDoc;
        this.postcodeDoc = postcodeDoc;
        this.cityDoc = cityDoc;
        this.addressDoc = addressDoc;
    }
    
    /**
     *
     * @param idDoc
     * @param emailDoc
     * @param passwordDoc
     * @param hashDoc
     * @param phoneNumberDoc
     * @param businessNameDoc
     * @param activeDoc
     * @param selectedForDeactivationDoc
     * @param postcodeDoc
     * @param cityDoc
     * @param addressDoc
     */
    public Doctor(Long idDoc, String emailDoc, String passwordDoc, String hashDoc,
            String phoneNumberDoc, String businessNameDoc, Boolean activeDoc,
            Boolean selectedForDeactivationDoc, Long postcodeDoc, String cityDoc, String addressDoc) {
        this.idDoc = idDoc;
        this.emailDoc = emailDoc;
        this.passwordDoc = passwordDoc;
        this.hashDoc = hashDoc;
        this.phoneNumberDoc = phoneNumberDoc;
        this.businessNameDoc = businessNameDoc;
        this.activeDoc = activeDoc;
        this.selectedForDeactivationDoc = selectedForDeactivationDoc;
        this.postcodeDoc = postcodeDoc;
        this.cityDoc = cityDoc;
        this.addressDoc = addressDoc;
    }
    
    /**
     *
     */
    public Doctor() {
    }
    
}
