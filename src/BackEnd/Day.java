package BackEnd;

/**
 * Created by omer on 18/04/16.
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Day {
    private Shift morningShift;
    private Shift eveningShift;
    private LocalDate date;
    private static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);


    public Day(Shift morningShift, Shift eveningShift, LocalDate date) {
        this.morningShift = morningShift;
        this.eveningShift = eveningShift;
        this.date = date;
    }

    public Shift getMorningShift() {
        return morningShift;
    }

    public Shift getEveningShift() {
        return eveningShift;
    }

    public String getDate() {
        return date.format(formatterDate);
    }
}
