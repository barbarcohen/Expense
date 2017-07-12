package cz.rudypokorny.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;


public class DateUtil {

    public static LocalDate getCurrentDate(){
        return getCurrentDateTime().toLocalDate();
    }

    public static ZonedDateTime getCurrentDateTime(){
        return ZonedDateTime.now();
    }
}
