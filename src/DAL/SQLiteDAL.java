package DAL;
import java.rmi.server.ExportException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


import BackEnd.*;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

public class SQLiteDAL implements IDAL{

    private SQLiteDataSource dataSource;
    private Connection db;
    private Statement stat;
    public SQLiteDAL(){
        dataSource = new SQLiteDataSource();
        connected();
        try {
            db = dataSource.getConnection();
            stat = db.createStatement();
        }
        catch (Exception e){
        }
    }


    public boolean addRole(int roleID, int empID){
        try{
            stat.execute("INSERT INTO rolesOfEmployees VALUES (" + empID + "," + roleID +");");
        }
        catch(Exception e){
            return false;
        }

        return true;
    }



    @Override
    public boolean insert(Employee emp) {
        try {
            //insert new emp
            stat.execute("INSERT INTO Employees VALUES (" + emp.getId() + ","
                                + "'"+emp.getFirstName()+"'"+ ","
                                + "'"+emp.getLastName()+"'"+","
                                + null
                                +"'"+emp.getDateOfHire().toString()+"'"+","
                                +"'"+emp.getContract()+"'"+ "," + 0 + ");");
            for(Role r: emp.getRoles()){
                addRole(r.getID(), emp.getId());
            }


        }
        catch(Exception e){
            System.out.print(e);
            return false;
        }
        return true;
    }

    private boolean employeeAvilability(Employee emp){
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
            return preparedStatement.executeUpdate()==1;
        }
        catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(Employee emp) {
        String sql = "UPDATE Employees\n" +
                    "SET Deleted = 1\n"+
                    "WHERE ID = "+emp.getId()+";";
        try {
            return stat.executeUpdate(sql)>0;

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
            preparedStatement.setString(2, emp.getLastName();
            preparedStatement.setString(3, emp.getContract();
            preparedStatement.setString(4, emp.getDateOfHire());

            int rowsAffected = preparedStatement.executeUpdate();
            stat.executeUpdate("DELETE FROM EmployeeAvailability WHERE ID = "+emp.getId());
            employeeAvilability(emp);
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
        return set.getInt(1)>0;
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
            sql = "INSERT INTO RolesOfShifts VALUES (?,?,?)";
            preStat = db.prepareStatement(sql);
            for(Pair p: shift.getRoles()){
                if(!(roleExists(shift.getID(),p.getRole(),"RolesOfShifts"))){
                    preStat.setInt(1,p.getRole().getID());
                    preStat.setInt(2,shift.getID());
                    preStat.setInt(3,shift.getAmountOfRoles().get(p.getRole().getID()));
                    preStat.executeUpdate();
                }
            }
            sql = "INSERT INTO EmployeesInShifts VALUES (?,?)";
            preStat = db.prepareStatement(sql);
            for(Pair p: shift.getRoles()){
                if(p.getEmployee()!=null){
                    preStat.setInt(1,shift.getID());
                    preStat.setInt(2,p.getEmployee().getId());
                }
            }
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
            return stat.executeUpdate(sql)>0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean update(Shift sft) {
        return false;
    }

    @Override
    public boolean insert(Day day) {
        return false;
    }

    @Override
    public boolean delete(Day day) {
        return false;
    }

    @Override
    public boolean update(Day day) {
        return false;
    }

    /**
     * used to get the next shiftID
     * @return the shift id or -1 if there was an error
     */
    public int shiftID(){
        try {
            String sql = "SELECT COUNT(*) FROM Shifts";
            ResultSet set = stat.executeQuery(sql);
            return set.getInt(1);
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
            String sql = "SELECT COUNT(*) FROM Roles";
            ResultSet set = stat.executeQuery(sql);
            return set.getInt(1);
        }catch (SQLException e){
            return -1;
        }
    };

    private boolean connected() {
        dataSource.setUrl("jdbc:sqlite:/home/omer/IdeaProjects/super_lee/superlee");
        return true;

    }
}
