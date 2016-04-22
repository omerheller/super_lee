package PL;

import BL.*;
import BackEnd.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by niv on 4/19/2016.
 */
public class ShiftMenu {
    private static IBL bl_impl = new BL_IMPL();

    private static Scanner sc = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private static HashMap<Integer,Role> rolesDictionary = new HashMap<Integer, Role>();


    public static void run() {
        boolean switchCase = false;

        //fill in the rolesDicationary
        if(bl_impl.getRoles().size()>0) {
            for (Role r : bl_impl.getRoles()) {
                rolesDictionary.put(r.getID(), r);
                //lineIndex++;
            }
        }

        System.out.println("Welcome to Shift menu");
        System.out.println("1. Add Shift");
        System.out.println("2. Edit/Delete Shift");
        System.out.println("3. Add Day");

        while(!switchCase) {
            int i = sc.nextInt();
            switch (i) {
                case 1:
                    addShift();
                    switchCase=true;
                    break;
                case 2:
                    editShift();
                    switchCase=true;
                    break;
                case 3:
                    addDay();
                    switchCase=true;
                    break;
                default:
                    System.out.println("Try again..");
                    break;
            }
        }
    }

    private static void addShift(){
        int duration, day, roleChosen, numOfEmployees=0, counter=0, z, idOfEmpChosen=0;
        Employee empChosen=null, manager=null;
        Vector<Employee> availableEmployees= new Vector<Employee>();
        Vector<Employee> availableEmployeesBasedOnRole= new Vector<Employee>();
        int[][] shift = new int[1][2]; //based on date and starttime, shows which shift in the availability array of each employee
        LocalTime startTime, endTime;
        LocalDate date;
        boolean finishedRoles=false;
        Vector<Pair>roles = new Vector<Pair>();
        Role r = null;
        HashMap<Integer,Integer> amountOfRoles = new HashMap<Integer,Integer>();

        System.out.println("Enter start time: (HH:mm)");
        startTime = LocalTime.parse(sc.next(), timeFormatter);

        System.out.println("Enter end time: (HH:mm)");
        endTime = LocalTime.parse(sc.next(), timeFormatter);

        //calculate duration
        duration=0;
        //


        System.out.println("Enter date: (dd/MM/yyyy)");
        date = LocalDate.parse(sc.next(), dateFormatter);

        //get day according to calendar date
        day = date.getDayOfWeek().getValue();

        /*get specific shift in the 2-D array*/
        //1: check if morning shift/evening shift
        if(startTime.getHour()<12){
            //morning shift
            shift[0][0]=0;
        }
        else{
            //evening shift
            shift[0][0]=1;
        }

        //2: get day in the week: (enum: 1 (Monday) to 7 (Sunday))
        if(day==1){
            //fix monday=1
            shift[0][1] = day;
        }
        else if(day==7){
            //fix sunday=0
            shift[0][1] = 0;
        }
        else{
            //no need to fix rest of days
            shift[0][1] = day;
        }

        //fill in vector of available employees for shift
        availableEmployees = bl_impl.getAvailableEmployees(shift);

        //START GET MANAGER
        System.out.println("Choose manager of shift (Insert manager's ID):");

        /*list all managers in store that are available on this day and this shift*/
        for(Employee emp : availableEmployees) {
            for (Role empRole : emp.getRoles()) {
                if (empRole.getID() == 1) {
                    availableEmployeesBasedOnRole.add(emp);
                }
            }
        }

        for(Employee emp: availableEmployeesBasedOnRole){
            System.out.println("ID: "+ emp.getId() +" Name: " + emp.getFirstName() + " " + emp.getLastName());
        }

        idOfEmpChosen = sc.nextInt();
        if(idOfEmpChosen ==0){ //if no manager chosen, then don't create shift!!
            System.out.println("No manager chosen, cancelling shift...");
            return;
        }
        else{
            manager = bl_impl.getEmployee(idOfEmpChosen);
            //remove manager from avail employees
            availableEmployees.remove(manager);
        }

        for(int f=0;availableEmployeesBasedOnRole.size()>0;f++){
            availableEmployeesBasedOnRole.remove(0);
        }
        //END GET MANAGER


        //START INSERT ROLES (as PAIR(roleID, empID)) INTO SHIFT
        while(!finishedRoles) {
            System.out.println("Choose a role, if done press 0:");
            /*list roles*/
            for (z = 1; z <= rolesDictionary.size(); z++) {
                System.out.println(z + ": " + rolesDictionary.get(z).getName());
            }
            roleChosen = sc.nextInt();

            if (roleChosen == 0) {
                finishedRoles = true;
            }
            else if (roleChosen > 0 && roleChosen <= rolesDictionary.size()) {
                r = rolesDictionary.get(roleChosen);
                if (r != null) {
                    System.out.println(r.getName() + " added.");
                }
                else {
                    System.out.println("ROLE IS NULLLLLLLLLLL");
                }
            }
            else {
                System.out.println("Try again..");
            }

            if (!finishedRoles) {
                //get available employees based on ROLE!!!!!!!!
                for (Employee emp : availableEmployees) {
                    for (Role empRole : emp.getRoles()) {
                        if (empRole.getID() == r.getID()) {
                            availableEmployeesBasedOnRole.add(emp);
                        }
                    }
                }


                System.out.println("How many employees are needed for this role?");
                numOfEmployees = sc.nextInt();
                /*insert into the hashmap by roleID*/
                if (r != null) {
                    amountOfRoles.put(r.getID(), numOfEmployees);
                }
                else {
                    System.out.println("ROLE IS NULL: add shift menu");
                }


                /**show employees based on the role id and availability for this day
                 *if press 0 then finished.
                 *to check if shift is understaffed, go through vector roles and see if
                 * (number of pairs with role R) == (amountOfRoles.get(r.getID))  ==> if false then understaffed!
                 */
                boolean finished = false;
                while (counter < numOfEmployees && !finished) {
                    System.out.println("Choose employee (based on ID) to fill in the role chosen, if none available press 0:");

                    for (Employee emp : availableEmployeesBasedOnRole) {
                        System.out.println("ID: " + emp.getId() + " Name: " + emp.getFirstName() + " " + emp.getLastName());
                    }

                    idOfEmpChosen = sc.nextInt();
                    boolean found = false;
                    Employee removeEmp=null;
                    if (idOfEmpChosen != 0) {
                        for (Employee emp : availableEmployeesBasedOnRole) {
                            //have to check by ID and can't check by object because the DAL creates a new
                            //instance of Employee when returning getEmployee(int id)
                            if (emp.getId() == idOfEmpChosen) {
                                //insert into pair
                                roles.add(new Pair(r, emp));
                                //remove emp from avail list
                                removeEmp = emp;
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("Wrong ID number, try again:");
                            //take away from counter to reverse the addition in the end of loop
                            counter--;
                        }
                        else{
                            availableEmployeesBasedOnRole.remove(removeEmp);
                        }
                    }
                    else {
                        finished = true;
                    }

                    counter++;
                }
            }
        }

        //END INSERT ROLES (as PAIR(roleID, empID)) INTO SHIFT

        bl_impl.insertShift(startTime, endTime, duration, date, manager, roles, amountOfRoles);

    }

    private static void editShift(){




    }

    private static void addDay(){



    }









}
