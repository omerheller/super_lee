package BackEnd;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by omer on 18/04/16.
 */
public class Shift {
    private int ID;
    private Date startTime;
    private Date endTime;
    private int duration;
    private Date date;
    private Employee manager;
    private Vector<Pair>roles;

    public Shift(int ID, Date startTime, Date endTime, int duration, Date date, Employee manager, Vector<Pair> roles) {
        this.ID = ID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.date = date;
        this.manager = manager;
        this.roles = roles;
    }

    public int getID(){
        return ID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public Employee getManager() {
        return manager;
    }

    public Vector<Pair> getRoles() {
        return roles;
    }

    public void addRole(Role role){
        roles.add(new Pair(role,null));
    }

    public boolean enroll(String role,Employee emp){
        for(Pair pi : roles){
            if (pi.getRole().equals(role) && pi.getEmployee()==null){
                pi.setEmployee(emp);
                return true;
            }
        }
        return false;
    }


}