package cz.rudypokorny.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;


public class DateUtil {

    public static LocalDate getCurrentDate(){
        return getCurrentDateTime().toLocalDate();
    }

    public static ZonedDateTime getCurrentDateTime(){
        return ZonedDateTime.now();
    }

    public static ZoneId getCurrentTimeZone(){
        return getCurrentDateTime().getZone();
    }
}
