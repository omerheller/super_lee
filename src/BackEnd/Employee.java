package BackEnd;

import java.util.Date;
import java.util.Vector;

/**
 * Created by omer on 18/04/16.
 */
public class Employee {



    private String firstName;
    private String lastName;
    private int id;
    private Vector<Role> roles;
    private Date dateOfHire;
    private String contract;
    private int[][]  availability = new int[2][7];


    public Employee(String firstName, String lastName, int id, Vector<Role> roles, Date dateOfHire, String contract, int[][] ava){

        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.roles = roles;
        this.dateOfHire = dateOfHire;
        this.contract = contract;
        availability = ava;
    }

    public int[][] getAvailability() {
        return availability;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public Vector<Role> getRoles() {
        return roles;
    }

    public String getDateOfHire() {
        return dateOfHire.toString();
    }

    public String getContract() {
        return contract;
    }

    public void addRole(Role role){
        if(roles==null){
            roles = new Vector<Role>();
            roles.add(role);
        }
        else
            roles.add(role);
    }

}
