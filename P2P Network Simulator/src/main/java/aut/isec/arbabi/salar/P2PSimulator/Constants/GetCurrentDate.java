package aut.isec.arbabi.salar.P2PSimulator.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class GetCurrentDate {
    public static String getCurrentDate(boolean forDirectoryNaming) {
        DateTimeFormatter dateTimeFormatter;
        if (forDirectoryNaming)
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        else
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }
}
