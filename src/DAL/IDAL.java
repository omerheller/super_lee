package DAL;

import BackEnd.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * Created by omer on 18/04/16.
 */
public interface IDAL {
    public boolean insert(Employee emp);
    public boolean delete(Employee emp);
    public boolean update(Employee emp);
    public boolean insert(Shift shft);
    public boolean delete(Shift shft);
    public boolean update(Shift sft);
    public boolean insert(Day day);
    public boolean delete(Day day);
    public boolean update(Day day);
    public boolean addRole(int roleID, int empID);
    Day getDay(LocalDateTime d);
    Shift getShift(int id);
    Employee getEmployee(int id);
    Role getRole(int id);
    Vector<Role> getRoles();
    Vector<Employee> getEmployees();
}
