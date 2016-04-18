package BackEnd;

/**
 * Created by omer on 18/04/16.
 */
import java.util.Date;
public class Day {
    private Shift morningShift;
    private Shift eveningShift;
    private Date date;


    public Day(Shift morningShift, Shift eveningShift, Date date) {
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

    public Date getDate() {
        return date;
    }
}
