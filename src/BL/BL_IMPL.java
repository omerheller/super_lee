package BL;

import BackEnd.Day;
import BackEnd.Employee;
import BackEnd.Role;
import BackEnd.Shift;
import DAL.IDAL;
import DAL.SQLiteDAL;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
    public boolean insertShift() {
        return false;
    }

    @Override
    public boolean updateShift() {
        return false;
    }

    @Override
    public boolean deleteShift(Shift s) {
        return false;
    }

    @Override
    public boolean insertDay() {
        return false;
    }

    @Override
    public boolean updateDay() {
        return false;
    }

    @Override
    public boolean deleteDay(Day d) {
        return false;
    }

    @Override
    public boolean insertRole(String name) {
        //return SQLDAL.addRole(name);
        return false;
    }

    @Override
    public boolean updateRole(int id, String name) {
        return false;
    }

    @Override
    public boolean deleteRole(Role r) {
        return false;
    }

    @Override
    public Day getDay(Date d) {
        return null;
    }

    @Override
    public Shift getShift(Date d, Time startTime) {
        return null;
    }

    @Override
    public Employee getEmployee(int id) {
        return null;
    }

    @Override
    public Role getRole(int id) {
        return null;
    }

    @Override
    public Vector<Role> getRoles() {
        return null;
    }

    @Override
    public Vector<Employee> getEmployees() {
        return null;
    }

    @Override
    public Vector<Employee> getAvailableEmployees(int[][] avail) {
        return null;
    }


}
