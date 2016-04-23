package BL;

import BackEnd.*;
import DAL.IDAL;
import DAL.SQLiteDAL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by matan on 4/19/2016.
 */
public class BL_IMPL implements IBL {
    private static IDAL SQLDAL = new SQLiteDAL();

    @Override
    public boolean insertEmployee(String firstName, String lastName, int id, Vector<Role> roles, LocalDate dateOfHire, String contract, String bankAcct, int[][] ava) {
        /*check validity*/

        //create emp
        Employee emp = new Employee(firstName, lastName, id, roles, dateOfHire, contract, bankAcct, ava);

        //insert into database
        return SQLDAL.insert(emp);
    }

    @Override
    public boolean updateEmployee(String firstName, String lastName, int id, Vector<Role> roles, LocalDate dateOfHire, String contract, String bankAcct, int[][] ava) {
        Employee emp = new Employee(firstName, lastName, id, roles, dateOfHire, contract, bankAcct, ava);
        return SQLDAL.update(emp);
    }

    @Override
    public boolean deleteEmployee(Employee emp) {
        return SQLDAL.delete(emp);
    }

    @Override
    public boolean insertShift(LocalTime startTime, LocalTime endTime, int duration, LocalDate date, Employee manager, Vector<Pair> roles, HashMap<Integer,Integer> amountOfRoles){
        /*check that there is no shift in this date!*/
        boolean valid = true;
        if(!valid){
            System.out.println("Shift already added for this date");
            return false;
        }
        else{
            Shift newShift = new Shift(SQLDAL.shiftID(), startTime, endTime, duration, date, manager, roles, amountOfRoles);
            return SQLDAL.insert(newShift);
        }
    }

    @Override
    public boolean updateShift(LocalTime startTime, LocalTime endTime, int duration, LocalDate date, Employee manager, Vector<Pair> roles, HashMap<Integer, Integer> amountOfRoles) {
        return false;
    }

    @Override
    public boolean deleteShift(Shift s) {
        return SQLDAL.delete(s);
    }

    @Override
    public boolean insertDay(Day d) {
        return false;
    }

    @Override
    public boolean updateDay(Day d) {
        return false;
    }

    @Override
    public boolean deleteDay(Day d) {
        return SQLDAL.delete(d);
    }

    @Override
    public boolean insertRole(String name) {
        //check valid name

        //return SQLDAL.addRole(name);
        return false;
    }

    @Override
    public boolean updateRole(int id, String name) {
        Role updateRole = new Role(id, name);
        //return SQLDAL.updateRole(updateRole);
        return false;
    }

    @Override
    public boolean deleteRole(Role r) {
        //return SQLDAL.deleteRole(name);
        return false;
    }

    @Override
    public Day getDay(LocalDate d) {
        return SQLDAL.getDay(d);
    }

    @Override
    public Shift getShift(int id) {
        return SQLDAL.getShift(id);
    }

    @Override
    public Shift getShift(LocalDate d, LocalTime startTime) {
        return null;
    }

    @Override
    public Employee getEmployee(int id) {
        return SQLDAL.getEmployee(id);
    }

    @Override
    public Role getRole(int id) {
        return SQLDAL.getRole(id);
    }

    @Override
    public Vector<Role> getRoles() {
        return SQLDAL.getRoles();
    }

    @Override
    public Vector<Employee> getEmployees() {
        return SQLDAL.getEmployees();
    }

    @Override
    public Vector<Employee> getAvailableEmployees(int[][] avail) {
        return SQLDAL.getAvailableEmployees(avail);
    }


}
