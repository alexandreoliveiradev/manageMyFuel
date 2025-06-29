package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Date;

public class Formatter {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");

    public static String getFormattedDate(Date date) {
        return dateFormatter.format(date);
    }

    public static Date getDateFromString(String date) throws ParseException {
        return dateFormatter.parse(date);
    }

    public static int getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        return Month.from(currentDate).getValue();
    }

}
