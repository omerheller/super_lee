package DAL;
import java.rmi.server.ExportException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import BackEnd.*;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

public class SQLiteDAL implements IDAL{

    private SQLiteDataSource dataSource;
    private Connection db;
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
            db.createStatement().execute("INSERT INTO rolesOfEmployees VALUES (" + roleID + "," + empID +");");
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
            db.createStatement().execute("INSERT INTO Employees VALUES (" + emp.getId() + ","
                                + "'"+emp.getFirstName()+"'"+ ","
                                + "'"+emp.getLastName()+"'"+","
                                + null
                                +"'"+emp.getDateOfHire().toString()+"'"+","
                                +"'"+emp.getContract()+"'"+ "," + 0 + ");");
            for(Role r: emp.getRoles()){
                addRole(r.getID(), emp.getId());
            }

            int[][] empAva = emp.getAvailability();

            //insert into empAvailability
            db.createStatement().execute("INSERT INTO EmployeeAvailability VALUES (" + empAva[0][0] + "," + empAva[0][1] +"," +
                    empAva[0][2] +"," + empAva[0][3] +"," + empAva[0][4] +"," + empAva[0][5] +"," + empAva[0][6] +"," + empAva[1][0] +"," +
                    empAva[1][1] +"," + empAva[1][2] +"," + empAva[1][3] +"," + empAva[1][4] +"," + empAva[1][5] +"," + empAva[1][6] +");");

        }
        catch(Exception e){
            System.out.print(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Employee emp) {
        return false;
    }

    @Override
    public boolean update(Employee emp) {
        return false;
    }

    @Override
    public boolean insert(Shift shft) {
        return false;
    }

    @Override
    public boolean delete(Shift shft) {
        return false;
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


    private boolean connected() {
        //boolean initialize = SQLiteJDBCLoader.initialize();
        dataSource.setUrl("jdbc:sqlite:/home/omer/IdeaProjects/super_lee/superlee");
       /* int i = 0;
        try {
            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("select * from \"Employee\"");
            while (executeQuery.next()) {
                i++;
                System.out.println("out: " + executeQuery.getMetaData().getColumnLabel(i));
                System.out.println("out: " + executeQuery.getMetaData().toString());

            }


        } catch (SQLException ex) {
            System.out.println("Exception");
        }*/

        return true;

    }
}
