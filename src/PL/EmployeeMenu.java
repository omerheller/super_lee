package PL;

import java.util.Scanner;

/**
 * Created by omer on 18/04/16.
 */
public class EmployeeMenu {


    public static void run(){
        Scanner sc = new Scanner(System.in);


        System.out.println("Welcome to Employee/Role menu");
        System.out.println("1. Add Employee");
        System.out.println("2. Edit/Delete Employee");
        System.out.println("3. Add Role");
        System.out.println("4. Edit/Delete Role");


        int i = sc.nextInt();

        switch (i) {
            case 1:
                addEmployee();
                break;
            case 2:
                editEmployee();
                break;
            case 3:
                addRole();
                break;
            case 4:
                editRole();
                break;
            default:
                System.out.println("Try again..");
        }
    }

    private void addEmployee(){



    }



}
