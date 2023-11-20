package Ojakgyo.com.example.Ojakgyo.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String changeDateFormat(LocalDateTime localDate) {
        if (localDate == null) {
            return "";
        }
        return localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
