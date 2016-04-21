package PL;

import BL.*;
import BackEnd.*;

import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.*;

/**
 * Created by omer on 18/04/16.
 */
public class EmployeeMenu {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    private static IBL bl_impl = new BL_IMPL();
    private static HashMap<Integer,Role> rolesDictionary = new HashMap<Integer, Role>();
    private static Scanner sc = new Scanner(System.in);
    private static String[][] days = new String[2][7];

    public static void run(){
        int lineIndex =1;
        Scanner sc = new Scanner(System.in);
        boolean switchCase = false;

        //fill in the rolesDicationary
        if(bl_impl.getRoles()!=null) {
            for (Role r : bl_impl.getRoles()) {
                rolesDictionary.put(lineIndex, r);
                lineIndex++;
            }
        }

        /*initialize days strings*/
        days[0][0]="Sunday Morning:"; days[1][0]="Sunday Evening:";
        days[0][1]="Monday Morning:"; days[1][1]="Monday Evening:";
        days[0][2]="Tuesday Morning:"; days[1][2]="Tuesday Evening:";
        days[0][3]="Wednesday Morning:"; days[1][3]="Wednesday Evening:";
        days[0][4]="Thursday Morning:"; days[1][4]="Thursday Evening:";
        days[0][5]="Friday Morning:"; days[1][5]="Friday Evening:";
        days[0][6]="Saturday Morning:"; days[1][6]="Saturday Evening:";

        System.out.println("Welcome to Employee/Role menu");
        System.out.println("1. Add Employee");
        System.out.println("2. Edit/Delete Employee");
        System.out.println("3. Add Role");
        System.out.println("4. Edit/Delete Role");

        while(!switchCase) {
            int i = sc.nextInt();
            switch (i) {
                case 1:
                    addEmployee();
                    switchCase=true;
                    break;
                case 2:
                    editEmployee();
                    switchCase=true;
                    break;
                case 3:
                    addRole();
                    switchCase=true;
                    break;
                case 4:
                    editRole();
                    switchCase=true;
                    break;
                case 5:
                    //add return to MainMenu menu function
                    switchCase=true;
                    break;
                default:
                    System.out.println("Try again..");
                    break;
            }
        }
    }

    private static void addEmployee(){
        boolean switchCase=false, finishedRoles = false;
        int[][] avail = new int[2][7];
        Vector<Role> roles = new Vector<Role>();
        String fName, lName, bankAcct, contract;
        int ID, i=0, x=0, y=0, z=1, roleChosen; //use x,y for the availabilty array.. use z for rolesDictionary movement

        System.out.println("Insert Employee's ID:");
        ID = sc.nextInt();

        System.out.println("Insert Employee's first name:");
        fName = sc.next();

        System.out.println("Insert Employee's last name:");
        lName = sc.next();

        System.out.println("Insert Employee's date of hire (dd/MM/yyyy):");
        LocalDate date = LocalDate.parse(sc.next(), formatter);

        System.out.println("Insert Employee's contract:");
        contract = sc.next();

        System.out.println("Insert Employee's bank account:");
        bankAcct= sc.next();

        //roles
        while(!finishedRoles){
            System.out.println("Choose a role to add, Press 0 if finished:");

            //print the roles available
            for(z=1;z<=rolesDictionary.size();z++){
                System.out.println(z+ ": " + rolesDictionary.get(z).getName());
            }
            roleChosen = sc.nextInt();

            if (roleChosen==0){
                finishedRoles = true;
            }
            else if(roleChosen>0 && roleChosen<=rolesDictionary.size()){
                roles.add(rolesDictionary.get(roleChosen));
                System.out.println(rolesDictionary.get(roleChosen).getName() + " added." );
            }
            else{
                System.out.println("Try again..");
            }
        }

        //avail
        System.out.println("Now insert employee's availability for each shift (1=Available, 0=Not available):");
        for(x=0;x<7;x++) {
            for (y=0; y < 2; y++) {
                switchCase=false;
                System.out.println(days[y][x]);
                while (!switchCase) {
                    i = sc.nextInt();
                    switch(i){
                        case 0:
                            avail[y][x] = 0;
                            switchCase = true;
                            break;
                        case 1:
                            avail[y][x] = 1;
                            switchCase = true;
                            break;
                        default:
                            System.out.println("Try again..");
                            break;
                    }
                }
            }
        }

        bl_impl.insertEmployee(fName, lName, ID, roles, date, contract, bankAcct, avail);
    }

    private static void editEmployee(){
        boolean switchCase=false, finishedRoles = false;
        int[][] avail = new int[2][7];
        Vector<Role> roles = new Vector<Role>();
        String fName, lName, bankAcct, contract;
        int ID, i=0, x=0, y=0, z=1, roleChosen, empID; //use x,y for the availabilty array.. use z for rolesDictionary movement

        System.out.println("Please insert employee's ID:");
        empID = sc.nextInt();

        Employee emp = bl_impl.getEmployee(empID);


        //check to make sure emp is not null
        while(emp==null){
            System.out.println("No employee exists with that ID, try again:");
            empID = sc.nextInt();
            emp = bl_impl.getEmployee(empID);
        }

        System.out.println("What would you like to update?");
        System.out.println("1: Personal Information");
        System.out.println("2: Employee availability");
        System.out.println("3: Remove employee's roles");
        System.out.println("4: Add to employee's roles");
        System.out.println("5: Delete employee");


        while(!switchCase) {
            i = sc.nextInt();
            switch (i) {
                case 1:
                    System.out.println("Insert Employee's first name:");
                    fName = sc.next();
                    if(fName==""){
                        fName = emp.getFirstName();
                    }

                    System.out.println("Insert Employee's last name:");
                    lName = sc.next();
                    if(lName==""){
                        lName = emp.getLastName();
                    }

                    System.out.println("Insert Employee's date of hire (dd/MM/yyyy):");
                    String inputDate = sc.next();
                    LocalDateTime date;
                    if(inputDate==""){
                        date = LocalDateTime.parse(emp.getDateOfHire(), formatter);
                    }
                    else{
                        date = LocalDateTime.parse(sc.next(), formatter);
                    }

                    System.out.println("Insert Employee's contract:");
                    contract = sc.next();
                    if(contract==""){
                        contract = emp.getContract();
                    }

                    System.out.println("Insert Employee's bank account:");
                    bankAcct= sc.next();
                    if(bankAcct==""){
                        bankAcct = emp.getBankAcct();
                    }

                    bl_impl.updateEmployee(fName, lName, emp.getId(), emp.getRoles(), date, contract, bankAcct, emp.getAvailability());
                    switchCase = true;
                    break;
                case 2:
                    System.out.println("Now insert employee's availability for each shift (1=Available, 0=Not available):");
                    for(x=0;x<7;x++) {
                        for (y=0; y < 2; y++) {
                            switchCase=false;
                            System.out.println(days[y][x]);
                            while (!switchCase) {
                                i = sc.nextInt();
                                switch(i){
                                    case 0:
                                        avail[y][x] = 0;
                                        switchCase = true;
                                        break;
                                    case 1:
                                        avail[y][x] = 1;
                                        switchCase = true;
                                        break;
                                    default:
                                        System.out.println("Try again..");
                                        break;
                                }
                            }
                        }
                    }

                    bl_impl.updateEmployee(emp.getFirstName(), emp.getLastName(), emp.getId(), emp.getRoles(), LocalDateTime.parse(emp.getDateOfHire(), formatter), emp.getContract(), emp.getBankAcct(), avail);
                    switchCase = true;
                    break;
                case 3:
                    while(!finishedRoles){
                        System.out.println("Choose a role to remove, Press 0 if finished:");

                        //print the roles in the employee's roles vector
                        for(z=1;z<=emp.getRoles().size();z++){
                            System.out.println(z+ ": " + emp.getRoles().get(z).getName());
                        }
                        roleChosen = sc.nextInt();

                        if (roleChosen==0){
                            finishedRoles = true;
                        }
                        else if(roleChosen>0 && roleChosen<=rolesDictionary.size()){ //make sure choice is valid
                            //remove from the employee's roles vector
                            emp.getRoles().remove(roleChosen);
                            System.out.println(rolesDictionary.get(roleChosen).getName() + " removed." );
                        }
                        else{
                            System.out.println("Try again..");
                        }
                    }

                    bl_impl.updateEmployee(emp.getFirstName(), emp.getLastName(), emp.getId(), emp.getRoles(), LocalDateTime.parse(emp.getDateOfHire(), formatter), emp.getContract(), emp.getBankAcct(), emp.getAvailability());
                    switchCase = true;
                    break;
                case 4:
                    while(!finishedRoles){
                        System.out.println("Choose a role to add, Press 0 if finished:");

                        //print the roles available
                        for(z=1;z<=rolesDictionary.size();z++){
                            System.out.println(z+ ": " + rolesDictionary.get(z).getName());
                        }
                        roleChosen = sc.nextInt();

                        if (roleChosen==0){
                            finishedRoles = true;
                        }
                        else if(roleChosen>0 && roleChosen<=rolesDictionary.size()){ //make sure choice is valid
                            //if emp doesn't have the chosen role add to his roles vector
                            if(!emp.getRoles().contains(rolesDictionary.get(roleChosen))){
                                emp.getRoles().add(rolesDictionary.get(roleChosen));
                                System.out.println(rolesDictionary.get(roleChosen).getName() + " added." );
                            }
                            else{
                                System.out.println("Employee already has the chosen role.");
                            }
                        }
                        else{
                            System.out.println("Try again..");
                        }
                    }

                    bl_impl.updateEmployee(emp.getFirstName(), emp.getLastName(), emp.getId(), emp.getRoles(), LocalDateTime.parse(emp.getDateOfHire(), formatter), emp.getContract(), emp.getBankAcct(), emp.getAvailability());
                    switchCase = true;
                    break;
                case 5:
                    bl_impl.deleteEmployee(emp);
                    switchCase = true;
                    break;
                default:
                    System.out.println("Try again..");
                    break;
            }
        }

    }

    private static void addRole(){
        String name;
        System.out.println("Please insert role name:");
        name = sc.next();
        bl_impl.insertRole(name);
    }

    private static void editRole(){
        int roleChosen, delete, z;
        String newName;
        System.out.println("Choose role:");

        //print the roles available
        for(z=1;z<=rolesDictionary.size();z++){
            System.out.println(z+ ": " + rolesDictionary.get(z).getName());
        }
        //chose which role from the list
        roleChosen = sc.nextInt();

        if(roleChosen>0 && roleChosen<=rolesDictionary.size()){   //make sure choice is valid
            System.out.println("To delete role press 1, to edit press 0:");
            delete = sc.nextInt();

            switch(delete){
                case 0:
                    //edit role
                    System.out.println("Insert new name:");
                    newName = sc.next();
                    bl_impl.updateRole(rolesDictionary.get(roleChosen).getID(), newName);
                case 1:
                    //delete role
                    bl_impl.deleteRole(rolesDictionary.get(roleChosen));
            }
        }
        else {
            System.out.println("Try again..");
        }
    }
}
