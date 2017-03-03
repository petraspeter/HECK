package sk.upjs.ics.pro1a.databaseDao;

import java.sql.Timestamp;
import java.util.List;
import sk.upjs.ics.pro1a.dao.User.UserDaoAppointment;
import sk.upjs.ics.pro1a.entities.Appointment;
import sk.upjs.ics.pro1a.entities.User;

/**
 *
 * @author raven
 */
public class UserDatabaseDaoAppointment implements UserDaoAppointment{
    
    /**
     * metoda vyhlada zoznam volnych terminov podla zadanych kriterii
     * @param specialist typ specialistu, ktoreho chceme vyhladat
     * @param day den v ktory chceme vyhladat termin
     * @param city nazov obce, ktoru chceme vyhladat.
     *                  Moze ist aj o ciastkovy udaj(napr. Nove Mesto nam vrati aj Nove Mesto nad Vahom)
     * @return zoznam volnych terminov pre zvoleneho specialistu a zvoleny den a mesto
     */
    @Override
    public List<Appointment> searchForAppointments(String specialist, Timestamp day, String city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vyhlada zoznam volnych terminov podla zadanych kriterii
     * @param specialist typ specialistu, ktoreho chceme vyhladat
     * @param from zaciatok casoveho intervalu od ktoreho chceme zobrazit volne terminy
     * @param to koniec casoveho intervali do ktoreho chceme zobrazit volne terminy,
     *                  defaultne sa jedna o koniec dna, t.j. YYYY-MM-DD-23-59-59-999999999
     * @param city nazov obce, ktoru chceme vyhladat.
     *                  Moze ist aj o ciastkovy udaj(napr. Nove Mesto nam vrati aj Nove Mesto nad Vahom)
     * @return zoznam volnych terminov pre zvoleneho specialistu a zvoleny cas od-do a mesto
     */
    @Override
    public List<Appointment> searchForAppointments(String specialist, Timestamp day, Long postcode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vyhlada zoznam volnych terminov podla zadanych kriterii
     * @param specialist typ specialistu, ktoreho chceme vyhladat
     * @param day den v ktory chceme vyhladat termin
     * @param postcode Postove Smerove Cislo obce
     * @return zoznam volnych terminov pre zvoleneho specialistu a zvoleny den a mesto(pomocou PSC)
     */
    @Override
    public List<Appointment> searchForAppointments(String specialist, Timestamp from, Timestamp to, String city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vyhlada zoznam volnych terminov podla zadanych kriterii
     * @param specialist typ specialistu, ktoreho chceme vyhladat
     * @param from zaciatok casoveho intervalu od ktoreho chceme zobrazit volne terminy
     * @param to koniec casoveho intervali do ktoreho chceme zobrazit volne terminy,
     *                  defaultne sa jedna o koniec dna, t.j. YYYY-MM-DD-23-59-59-999999999
     * @param postcode Postove Smerove Cislo obce
     * @return zoznam volnych terminov pre zvoleneho specialistu a zvoleny cas od-do a mesto(pomocou PSC)
     */
    @Override
    public List<Appointment> searchForAppointments(String specialist, Timestamp from, Timestamp to, Long postcode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vyhlada zoznam volnych terminov podla zadanych kriterii
     * @param specialist typ specialistu, ktoreho chceme vyhladat
     * @param from zaciatok casoveho intervalu od ktoreho chceme zobrazit volne terminy
     * @param to koniec casoveho intervali do ktoreho chceme zobrazit volne terminy,
     *                  defaultne sa jedna o koniec dna, t.j. YYYY-MM-DD-23-59-59-999999999
     * @param distance vzdialenost do ktorej moze byt vzdialene zdravotnicke zariadenie
     * @param Latitude zemepisna sirka bodu od ktoreho meriame vzdialenost k zdravotnickemu zariadeniu
     * @param Longitude zemepisna dlzka od ktorej meriame vzdialenost k zdravotnickemu zariadeniu
     * @return zoznam volnych terminov pre zvoleneho specialistu, zvoleny cas od-do v okruhu od urciteho
     * bodu(napr. aktualnej polohy, od trvaleho bydliska, atd.)
     */
    @Override
    public List<Appointment> searchForAppointments(String specialist, Timestamp from, Timestamp to, Double distance, Long Latitude, Long Longitude) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vyhlada zoznam volnych terminov podla zadanych kriterii
     * @param specialist typ specialistu, ktoreho chceme vyhladat
     * @param day den v ktory chceme vyhladat termin
     * @param distance vzdialenost do ktorej moze byt vzdialene zdravotnicke zariadenie
     * @param Latitude zemepisna sirka bodu od ktoreho meriame vzdialenost k zdravotnickemu zariadeniu
     * @param Longitude zemepisna dlzka od ktorej meriame vzdialenost k zdravotnickemu zariadeniu
     * @return zoznam volnych terminov pre zvoleneho specialistu, zvoleny den v okruhu od urciteho
     *                  bodu(napr. aktualnej polohy, od trvaleho bydliska, atd.)
     */
    @Override
    public List<Appointment> searchForAppointments(String specialist, Timestamp day, Double distance, Long Latitude, Long Longitude) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vrati, ci je mozne sa prihlasit na zadany termin
     * @param appointment
     * @return ci je zvoleny termin este volny
     */
    @Override
    public boolean isAppointmentsAviable(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda zarezervuje vybraby termin
     * @param appointment vybrany termin
     */
    @Override
    public void bookAppointment(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda zrusi vybrany termin
     * @param appointment  vybrany termin
     */
    @Override
    public void cancelAppointment(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vrati vsetky nadchadzajuce terminy
     * @param user pouzivatel ktoreho terminy vraciame
     * @return vsetky altualne terminy pouzivatela
     */
    @Override
    public List<Appointment> showYourActualAppointments(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vrati zoznam vsetkych terminov, ktorych sa pouzivatel zucastnil
     * @param user pouzivatel ktoreho terminy vraciame
     * @return vsetky predchadzajuce terminy pouzivatela
     */
    @Override
    public List<Appointment> showYourPreviousAppointments(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * metoda vrati zoznam vsetkych terminov pouzivatela
     * @param user pouzivatel ktoreho terminy vraciame
     * @return vsetky terminy pouzivatela
     */
    @Override
    public List<Appointment> showYourAppointments(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
