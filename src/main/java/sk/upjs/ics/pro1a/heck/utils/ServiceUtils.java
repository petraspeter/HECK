package sk.upjs.ics.pro1a.heck.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceUtils {
    
    public static Timestamp convertStringToTimestamp(String timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(timestamp);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) { // string je zle naformatovany
            return null;
        }
    }
    
    public static String convertTimestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timestamp.getTime()));
    }
}
