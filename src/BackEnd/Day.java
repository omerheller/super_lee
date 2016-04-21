package BackEnd;

/**
 * Created by omer on 18/04/16.
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Day {
    private Shift morningShift;
    private Shift eveningShift;
    private LocalDate date;


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
        return date.toString();
    }
}
