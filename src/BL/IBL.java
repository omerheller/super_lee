package BL;

import BackEnd.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by matan on 4/19/2016.
 */
public interface IBL {
    boolean insertEmployee(String firstName, String lastName, int id, Vector<Role> roles, LocalDate dateOfHire, String contract, String bankAcct, int[][] ava);
    boolean updateEmployee(String firstName, String lastName, int id, Vector<Role> roles, LocalDate dateOfHire, String contract, String bankAcct, int[][] ava);
    boolean deleteEmployee(Employee emp);
    boolean insertShift();
    boolean updateShift();
    boolean deleteShift(Shift s);
    boolean insertDay();
    boolean updateDay();
    boolean deleteDay(Day d);
    boolean insertRole(String name);
    boolean updateRole(String name);
    boolean deleteRole(Role r);
    Day getDay(Date d);
    Shift getShift(Date d, Time startTime);
    Employee getEmployee(int id);
    Role getRole(int id);
    List<Role> getRoles();
    List<Employee> getEmployees();

}
