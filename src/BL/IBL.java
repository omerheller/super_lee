package BL;

import BackEnd.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
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
    boolean updateRole(int id, String name);
    boolean deleteRole(Role r);
    Day getDay(Date d);
    Shift getShift(Date d, Time startTime);
    Employee getEmployee(int id);
    Role getRole(int id);
    Vector<Role> getRoles();
    Vector<Employee> getEmployees();


    /**here i will send in the arguements a shift
    * it will be an array[1][2] where avail[0][0] is 0 or 1
    * and avail[0][1] is 0-6..
    * Meaning: that if avail[0][0] = 1 and avail[0][1] = 5
    * the shift being sent is [1][5] = Friday evening.
    * As a result i need you to return a vector of employees that are available on this shift,
    * not their IDs i need the entire instance of employee itself so i can list their names, etc..
    */
    Vector<Employee> getAvailableEmployees(int[][] avail);

}
