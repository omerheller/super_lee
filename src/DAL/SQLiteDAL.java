package DAL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.ListIterator;


import BackEnd.*;
import org.omg.Messaging.SyncScopeHelper;
import org.sqlite.SQLiteDataSource;

public class SQLiteDAL implements IDAL{

    private SQLiteDataSource dataSource;
    private Connection db;
    private Statement stat;
    public SQLiteDAL(){
        dataSource = new SQLiteDataSource();
        connected();
        try {
            db = dataSource.getConnection();

        }
        catch (Exception e){
        }
    }


    public boolean addRole(int roleID, int empID){
        try{
            String sql = "INSERT INTO RolesOfEmployees " +
                    "VALUES ("+roleID+","+empID+")";
            stat = db.createStatement();
            int rows = stat.executeUpdate(sql);
            stat.close();
            return rows==1;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }



    @Override
    public boolean insert(Employee emp) {
        try {
            //insert new emp
            String sql = "INSERT INTO Employees VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preStat = db.prepareStatement(sql);
            preStat.setInt(1,emp.getId());
            preStat.setString(2,emp.getFirstName());
            preStat.setString(3,emp.getLastName());
            preStat.setString(4,emp.getContract());
            preStat.setString(5,emp.getDateOfHire().toString());
            preStat.setString(6,emp.getBankAcct());
            preStat.setInt(7,0);
            preStat.executeUpdate();
            preStat.close();
            for(Role r: emp.getRoles()){
                addRole(r.getID(), emp.getId());
            }
            employeeAvailability(emp);
        }
        catch(Exception e){
            System.out.print("insert employee");
            System.out.print(e);
            return false;
        }
        return true;
    }

    private boolean employeeAvailability(Employee emp){
        try {
            String sql = "INSERT INTO EmployeeAvailability VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
            PreparedStatement preparedStatement =
                    db.prepareStatement(sql);
            preparedStatement.setInt(1, emp.getId());
            int counter=2;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 2; j++) {
                    preparedStatement.setInt(counter, emp.getAvailability()[j][i]);
                    counter++;
                }
            }
            int rows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rows==1;
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(Employee emp) {
        String sql = "UPDATE Employees\n" +
                    "SET Deleted = 1\n"+
                    "WHERE ID = "+emp.getId()+";";
        try {
            stat = db.createStatement();
            int rows = stat.executeUpdate(sql);
            stat.close();
            return rows>0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean update(Employee emp) {
        String sql = "UPDATE Employees " +
                "SET firstName=? , lastName=? , contract = ? , date_of_hire = ? " +
                "WHERE ID=?";
        try{
            PreparedStatement preparedStatement =
                    db.prepareStatement(sql);

            preparedStatement.setString(1, emp.getFirstName());
            preparedStatement.setString(2, emp.getLastName());
            preparedStatement.setString(3, emp.getContract());
            preparedStatement.setString(4, emp.getDateOfHire().toString());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            stat = db.createStatement();
            stat.executeUpdate("DELETE FROM EmployeeAvailability WHERE ID = "+emp.getId());
            stat.close();
            employeeAvailability(emp);
            for(Role role: emp.getRoles()){
                if (!roleExists(emp.getId(),role,"rolesOfEmployees")){
                    addRole(role.getID(),emp.getId());
                }
            }
            return (rowsAffected==1);

        } catch(SQLException e){
            return false;
        }

    }

    private boolean roleExists(int ID,Role role,String table) throws SQLException{

        String sql = "SELECT COUNT(*) FROM "+table+
                " WHERE employeeID=?,roleID=?";
        PreparedStatement preStat = db.prepareStatement(sql);
        preStat.setInt(1,ID);
        preStat.setInt(2,role.getID());
        ResultSet set = preStat.executeQuery();
        int count = set.getInt(1);
        set.close();
        preStat.close();
        return count>0;
    }

    private void insertRolesOfShifts(Shift shift) throws SQLException{
        String sql = "INSERT INTO RolesOfShifts VALUES (?,?,?)";
        PreparedStatement preStat = db.prepareStatement(sql);
        for(Pair p: shift.getRoles()){
            if(!(roleExists(shift.getID(),p.getRole(),"RolesOfShifts"))){
                preStat.setInt(1,p.getRole().getID());
                preStat.setInt(2,shift.getID());
                preStat.setInt(3,shift.getAmountOfRoles().get(p.getRole().getID()));
                preStat.executeUpdate();
                preStat.clearParameters();
            }
        }
        preStat.close();
    }
    private void insertEmployeesOfShifts(Shift shift) throws SQLException{
        String sql = "INSERT INTO EmployeesInShifts VALUES (?,?)";
        PreparedStatement preStat = db.prepareStatement(sql);
        for(Pair p: shift.getRoles()){
            if(p.getEmployee()!=null){
                preStat.setInt(1,shift.getID());
                preStat.setInt(2,p.getEmployee().getId());
                preStat.executeUpdate();
                preStat.clearParameters();
            }
        }
        preStat.close();
    }
    @Override
    public boolean insert(Shift shift) {
        try {
            String sql = "INSERT INTO Shifts VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preStat = db.prepareStatement(sql);
            preStat.setInt(1, shift.getID());
            preStat.setString(2, shift.getStartTime());
            preStat.setString(3, shift.getEndTime());
            preStat.setInt(4, shift.getDuration());
            preStat.setString(5, shift.getDate());
            preStat.setInt(6, shift.getManager().getId());
            preStat.setInt(7, 0);
            preStat.executeUpdate();
            preStat.close();
            insertRolesOfShifts(shift);
            insertEmployeesOfShifts(shift);
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(Shift shift) {
        String sql = "UPDATE Shifts\n" +
                "SET Deleted = 1\n"+
                "WHERE ID = "+shift.getID()+";";
        try {
            Statement stat = db.createStatement();
            int rows = stat.executeUpdate(sql);
            return rows>0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean update(Shift shift) {
        String sql = "UPDATE Shifts " +
                "SET Date=? , Duration=? , End_Time= ? , Start_Time=?, ManagerID=?" +
                "WHERE ID=?";
        try {
            PreparedStatement preStat = db.prepareStatement(sql);
            preStat.setInt(6, shift.getID());
            preStat.setString(4, shift.getStartTime());
            preStat.setString(3, shift.getEndTime());
            preStat.setInt(2, shift.getDuration());
            preStat.setString(1, shift.getDate());
            preStat.setInt(5, shift.getManager().getId());
            preStat.executeUpdate();
            stat = db.createStatement();
            stat.executeUpdate("DELETE FROM RolesOfShifts" +
                               "WHERE ShiftID="+shift.getID());
            stat.close();
            insertRolesOfShifts(shift);
            stat = db.createStatement()
            stat.executeUpdate("DELETE FROM EmployeesInShifts" +
                    "WHERE ShiftID="+shift.getID());
            stat.close();
            insertEmployeesOfShifts(shift);
            return true;
        }
        catch(SQLException e){
            return false;
        }
    }

    @Override
    public boolean insert(Day day) {
        try{
            String sql = "INSERT INTO Days VALUES (?,?,?)";
            PreparedStatement preStat = db.prepareStatement(sql);
            preStat.setString(1,day.getDate());
            preStat.setInt(2,day.getMorningShift().getID());
            preStat.setInt(3,day.getEveningShift().getID());
            int rows = preStat.executeUpdate();
            preStat.close();
            return rows==1;
        }
        catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(Day day) {
        try{
            stat = db.createStatement();
            int rows = stat.executeUpdate("DELETE FROM Days WHERE Date="+day.getDate());
            stat.close();
            return rows==1;
        }
        catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(Day day) {
        try {
            String sql = "UPDATE Days " +
                    "SET Morning_Shift=?, Evening_Shift=? " +
                    "WHERE Date=?";
            PreparedStatement preStat = db.prepareStatement(sql);
            preStat.setInt(1,day.getMorningShift().getID());
            preStat.setInt(2,day.getEveningShift().getID());
            preStat.setString(3,day.getDate());
            int rows = preStat.executeUpdate();
            preStat.close();
            return rows==1;
        }
        catch (SQLException e){
            return false;
        }
    }

    /**
     * used to get the next shiftID
     * @return the shift id or -1 if there was an error
     */
    public int shiftID(){
        try {
            stat = db.createStatement();
            String sql = "SELECT COUNT(*) FROM Shifts";
            ResultSet set = stat.executeQuery(sql);
            int count = set.getInt(1);
            set.close();
            stat.close();
            return count;
        }catch (SQLException e){
            return -1;
        }
    };

    /**
     * used to get the next RoleID
     * @return the Role id or -1 if there was an error
     */
    public int roleID(){
        try {
            stat = db.createStatement();
            String sql = "SELECT COUNT(*) FROM Roles";
            ResultSet set = stat.executeQuery(sql);
            int count = set.getInt(1);
            set.close();
            stat.close();
            return count;
        }catch (SQLException e){
            System.out.println(e);
            return -1;
        }
    };

    private boolean connected() {
        dataSource.setUrl("jdbc:sqlite:/home/omer/IdeaProjects/super_lee/superlee");
        return true;

    }
    public List<Role> getRoles(){
        List<Role> list = new Vector<Role>();
        try {

            ResultSet set = getListByID("Roles", null, null);
            //set.first();
            while(set.next()){
                Role role = new Role(set.getInt(1),set.getString(2));
                //System.out.println(role.getID()+" "+role.getName());
                list.add(role);
            }
            set.close();
            stat.close();

        }
        catch (SQLException e){
            System.out.println(e);
        }
        return list;
    }

    private ResultSet getListByID(String table,String attribute,String value){
        String sql = "SELECT * FROM "+table+" ";
        if (attribute!=null)
                    sql += "WHERE "+attribute+"="+value;
        try {
            stat = db.createStatement();
            ResultSet set = stat.executeQuery(sql);
            return set;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }
}
