package PL;

import BL.*;
import BackEnd.*;

import java.time.LocalDateTime;
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


    public static void run() {
        boolean switchCase = false;

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
        /*public Shift(int ID, Date startTime, java.util.Date endTime, int duration,
        Date date, Employee manager, Vector<Pair> roles, HashMap<Integer,Integer> amountOfRoles)
        */
        int duration, day;
        Vector<Employee> availableEmployees= new Vector<Employee>();
        int[][] shift = new int[1][2]; //based on date and starttime, shows which shift in the availability array of each employee
        LocalDateTime startTime, endTime, date;

        System.out.println("Enter start time: (HH:mm)");
        startTime = LocalDateTime.parse(sc.next(), timeFormatter);

        System.out.println("Enter end time: (HH:mm)");
        endTime = LocalDateTime.parse(sc.next(), timeFormatter);

        //calculate duration
        //
        //


        System.out.println("Enter date: (dd/MM/yyyy)");
        date = LocalDateTime.parse(sc.next(), dateFormatter);

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


        System.out.println("Choose manager of shift:");
        /*list all managers in store that are available on this day and this shift*/




        System.out.println("Choose which roles are needed:");
        /*list roles*/



        System.out.println("How many employees are needed for this role?");
        /*insert into the hashmap by roleID*/



        System.out.println("Choose employee to fill in the role chosen, if none available press 0:");
        /*show employees based on the role id and availability for this day*/
        //if press 0 then insert null into the pair (role id, null) -> meaning no employee available for this role.
        //to check if shift is understaffed, go through vector roles and see if there is null in the emp field.
        //if emp=null don't insert into the table in SQLITE (can't because P.K)












    }

    private static void editShift(){




    }

    private static void addDay(){



    }









}
