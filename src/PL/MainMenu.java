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
        System.out.println("Please choose desired menu:");
        System.out.println("1. Employee Menu");
        System.out.println("2. Shifts Menu");

        while(!switchCase) {
            int i = sc.nextInt();
            switch (i) {
                case 1:
                    EmployeeMenu.run();
                    switchCase=true;
                    break;
                case 2:
                    //ShiftMenu.run();
                    switchCase=true;
                    break;
                default:
                    System.out.println("Try again..");
                    break;
            }
        }
    }
}
