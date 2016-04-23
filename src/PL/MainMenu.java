package PL;

import BackEnd.*;
import DAL.*;

import java.util.Date;
import java.util.Scanner;

/**
 * Created by omer on 18/04/16.
 */
public class MainMenu {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean switchCase = false;

        System.out.println("Super-Lee Management Application");
        int i= 1;
        while(i!=0) {
            System.out.println("Please choose desired menu:");
            System.out.println("1. Employee Menu");
            System.out.println("2. Shifts Menu");
            System.out.println("0 To Exit");
            i = sc.nextInt();
            switch (i) {
                case 0:
                    System.out.println("Exiting..");
                    break;
                case 1:
                    EmployeeMenu.run();
                    break;
                case 2:
                    ShiftMenu.run();
                    break;
                default:
                    System.out.println("Try again..");
                    break;
            }
        }
    }
}
