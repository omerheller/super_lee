package DAL;

import BackEnd.*;


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
}
