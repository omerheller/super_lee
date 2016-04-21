package BackEnd;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by omer on 18/04/16.
 */
public class Shift {
    private int ID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private LocalDate date;
    private Employee manager;
    private Vector<Pair>roles;
    private HashMap<Integer,Integer> amountOfRoles;

    public Shift(int ID, LocalDateTime startTime, LocalDateTime endTime, int duration,
                 LocalDate date, Employee manager, Vector<Pair> roles, HashMap<Integer,Integer> amountOfRoles) {
        this.ID = ID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.date = date;
        this.manager = manager;
        this.roles = roles;
        this.amountOfRoles = amountOfRoles;
    }

    public int getID(){
        return ID;
    }

    public String getStartTime() {
        return startTime.toString();
    }

    public String getEndTime() {
        return endTime.toString();
    }

    public int getDuration() {
        return duration;
    }

    public String getDate() {
        return date.toString();
    }

    public Employee getManager() {
        return manager;
    }

    public Vector<Pair> getRoles() {
        return roles;
    }

    public HashMap<Integer,Integer> getAmountOfRoles(){
        return amountOfRoles;
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