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
            }
        }

        System.out.println("Welcome to Shift menu");
        System.out.println("1. Add Shift");
        System.out.println("2. Edit/Delete Shift");
        System.out.println("3. Display shift");
        System.out.println("4. Display week");

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
                    displayShifts();
                    switchCase=true;
                    break;
                case 4:
                    displayWeek();
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
        Employee removeEmp=null;


        System.out.println("Enter start time: (HH:mm)");
        startTime = LocalTime.parse(sc.next(), timeFormatter);

        System.out.println("Enter end time: (HH:mm)");
        endTime = LocalTime.parse(sc.next(), timeFormatter);

        //calculate duration
        duration=endTime.getHour()-startTime.getHour();


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
            //set manager
            manager = bl_impl.getEmployee(idOfEmpChosen);
        }

        //remove manager from avail employees
        int empCounter=0; //use counter inorder to know which index in the vector to remove
        /*for(Employee emp : availableEmployees){
            if(emp.getId() == idOfEmpChosen)
                availableEmployees.remove(empCounter);
            empCounter++;
        }*/

        //remove all the managers from the available employees vector
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

                boolean finished = false;
                System.out.println("How many employees are needed for this role?");
                numOfEmployees = sc.nextInt();
                /*insert into the hashmap by roleID*/
                if (r != null) {
                    amountOfRoles.put(r.getID(), numOfEmployees);
                }
                else {
                    System.out.println("ROLE IS NULL: add shift menu");
                    finished = true;
                }


                /**show employees based on the role id and availability for this day
                 *if press 0 then finished.
                 *to check if shift is understaffed, go through vector roles and see if
                 * (number of pairs with role R) == (amountOfRoles.get(r.getID))  ==> if false then understaffed!
                 */
                while (counter < numOfEmployees && !finished) {
                    System.out.println("Choose employee (based on ID) to fill in the role chosen, if none available press 0:");

                    for (Employee emp : availableEmployeesBasedOnRole) {
                        System.out.println("ID: " + emp.getId() + " Name: " + emp.getFirstName() + " " + emp.getLastName());
                    }

                    idOfEmpChosen = sc.nextInt();
                    boolean found = false;
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
                            availableEmployees.remove(removeEmp);
                        }
                    }
                    else {
                        finished = true;
                    }

                    counter++;
                }
                counter=0;
            }
        }

        //END INSERT ROLES (as PAIR(roleID, empID)) INTO SHIFT

        boolean result = bl_impl.insertShift(startTime, endTime, duration, date, manager, roles, amountOfRoles);
        if(result){
            System.out.println("Shift added successfully!");
        }
        else{
            System.out.println("Failed to add shift");
        }


        /*insert shift into day*/
        //check if day already exists
        Day newDay = bl_impl.getDay(date);
        if(newDay==null) { //day doesn't exist, which means that this is the first shift created for this date so we will create the day
            if (startTime.getHour() < 12) {
                //morning shift
                newDay = new Day(bl_impl.getShift(date, startTime), null, date);
                result = bl_impl.insertDay(newDay);
            }
            else {
                //evening shift
                newDay = new Day(null, bl_impl.getShift(date, startTime), date);
                result = bl_impl.insertDay(newDay);
            }
        }
        //day exists so we will edit it based on shift created now
        else{
            if (startTime.getHour() < 12) {
                //morning shift
                newDay.setMorningShift(bl_impl.getShift(date, startTime));
                result = bl_impl.updateDay(newDay);
            }
            else {
                //evening shift
                newDay.setEveningShift(bl_impl.getShift(date, startTime));
                result = bl_impl.updateDay(newDay);
            }
        }

        if(result){
            System.out.println("Day added successfully!");
        }
        else{
            System.out.println("Failed to add day");
        }
    }

    private static void editShift(){
        Employee empChosen=null, manager=null;
        Vector<Employee> availableEmployees= new Vector<Employee>();
        Vector<Employee> availableEmployeesBasedOnRole= new Vector<Employee>();
        LocalDate date;
        LocalTime startTime, endTime;
        int duration;
        Vector<Pair> shiftRoles=null;
        HashMap<Integer, Integer> shiftAmountOfRoles=null;
        int[][] shiftAvail = new int[1][2];



        int idOfEmpChosen;
        System.out.println("Enter Date of Shift(dd/mm/yyyy)");
        while(true){
            try{
                date = LocalDate.parse(sc.next(),dateFormatter);
                break;
            }
            catch(Exception e){
                System.out.println(MainMenu.ANSI_BOLD+MainMenu.ANSI_RED+"BAD INPUT"+MainMenu.ANSI_RESET);
                System.out.println("Please try again.");
            }
        }
        System.out.println("Enter Start Time of Shift(HH:mm)");
        while(true){
            try{
                startTime = LocalTime.parse(sc.next(),timeFormatter);
                break;
            }
            catch(Exception e){
                System.out.println(MainMenu.ANSI_BOLD+MainMenu.ANSI_RED+"BAD INPUT"+MainMenu.ANSI_RESET);
                System.out.println("Please try again.");
            }
        }

        Shift shift = bl_impl.getShift(date,startTime);
        if(shift==null){
            System.out.println("Shift with given attributes was not found in db");
        }
        else{
            //get endTime
            endTime = LocalTime.parse(shift.getEndTime(), timeFormatter);

            //calculate duration
            duration=endTime.getHour()-startTime.getHour();

            //change employer
            System.out.println("Please choose new manager, to keep the same one press 0:");

            //get day according to calendar date
            int day = date.getDayOfWeek().getValue();

            /*get specific shift in the 2-D array*/
            //1: check if morning shift/evening shift
            if(startTime.getHour()<12){
                //morning shift
                shiftAvail[0][0]=0;
            }
            else{
                //evening shift
                shiftAvail[0][0]=1;
            }

            //2: get day in the week: (enum: 1 (Monday) to 7 (Sunday))
            if(day==1){
                //fix monday=1
                shiftAvail[0][1] = day;
            }
            else if(day==7){
                //fix sunday=0
                shiftAvail[0][1] = 0;
            }
            else{
                //no need to fix rest of days
                shiftAvail[0][1] = day;
            }

            //fill in vector of available employees for shift
            availableEmployees = bl_impl.getAvailableEmployees(shiftAvail);
            /*list all managers in store that are available on this day and this shift*/
            for(Employee emp : availableEmployees) {
                for (Role empRole : emp.getRoles()) {
                    if (empRole.getID() == 1) {
                        availableEmployeesBasedOnRole.add(emp);
                    }
                }
            }

            for(Employee emp: availableEmployeesBasedOnRole){
                if(emp.getId()!=shift.getManager().getId())
                System.out.println("ID: "+ emp.getId() +" Name: " + emp.getFirstName() + " " + emp.getLastName());
            }

            idOfEmpChosen = sc.nextInt();
            if(idOfEmpChosen!=0){ //chose new manager
                shift.setManager(bl_impl.getEmployee(idOfEmpChosen));
            }

            /*remove manager from avail employees
            int empCounter=0; //use counter inorder to know which index in the vector to remove
            for(Employee emp : availableEmployees){
                if(emp.getId() == idOfEmpChosen)
                    availableEmployees.remove(empCounter);
                empCounter++;
            }

            //remove all the managers from the available employees vector
            for(int f=0;availableEmployeesBasedOnRole.size()>0;f++){
                availableEmployeesBasedOnRole.remove(0);
            }*/

            //END GET MANAGER

            //change roles
            System.out.println("Roles in the shifts:");
            shiftAmountOfRoles = shift.getAmountOfRoles();
            for(Integer shiftID : shiftAmountOfRoles.values()){
                System.out.println("Shift: "+bl_impl.getRole(shiftID).getName()+" Amount: " +shiftAmountOfRoles.get(shiftID));
            }
            //change amount of roles







            boolean result = bl_impl.updateShift(shift.getID(), startTime, endTime, duration, date,shift.getManager(), shift.getRoles(), shift.getAmountOfRoles());
            if(result){
                System.out.println("Shift updated successfuly!");
            }
            else{
                System.out.println("Failed to update shift");
            }
        }
    }

    private static void displayWeek(){
        System.out.println("Choose a Week to display(dd/mm/yyyy)");
        LocalDate date;
        while(true){
            try{
                date = LocalDate.parse(sc.next(),dateFormatter);
                break;
            }
            catch(Exception e){
                System.out.println(MainMenu.ANSI_BOLD+MainMenu.ANSI_RED+"BAD INPUT"+MainMenu.ANSI_RESET);
                System.out.println("Please try again.");
            }
        }
        for(int i=0;i<7;i++) {
            Day day = bl_impl.getDay(date);
            System.out.println(MainMenu.ANSI_BLUEBG+"\t\t\t\t\t\t\t\t\t\t  "+MainMenu.ANSI_RESET);
            if (day != null) {
                day.print();
            } else {
                System.out.print(MainMenu.ANSI_REDBG+"\t\t\t\t"+MainMenu.ANSI_RESET);
                System.out.print(MainMenu.ANSI_BOLD+" "+date.getDayOfWeek()+" "+MainMenu.ANSI_RESET);
                System.out.println(MainMenu.ANSI_REDBG+"\t\t\t\t  "+MainMenu.ANSI_RESET);
                System.out.println("Day Not Found For " + date.format(dateFormatter));
            }
            System.out.println(MainMenu.ANSI_BLUEBG+"\t\t\t\t\t\t\t\t\t\t  "+MainMenu.ANSI_RESET);
            System.out.println("");
            date = date.plusDays(1);
        }

    }


    private static void displayShifts(){
        System.out.println("Choose a Date to display(dd/mm/yyyy)");
        LocalDate date;
        while(true){
            try{
                date = LocalDate.parse(sc.next(),dateFormatter);
                break;
            }
            catch(Exception e){
                System.out.println(MainMenu.ANSI_BOLD+MainMenu.ANSI_RED+"BAD INPUT"+MainMenu.ANSI_RESET);
                System.out.println("Please try again.");
            }
        }
        Day day = bl_impl.getDay(date);
        if (day!=null){
            day.print();
        }
        else{
            System.out.println("No Shifts Found For "+date.format(dateFormatter));
        }

    }
}




