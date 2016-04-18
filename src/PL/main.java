package PL;

import BackEnd.*;
import DAL.*;

import java.util.Date;
import java.util.Scanner;

/**
 * Created by omer on 18/04/16.
 */
public class main {

    public static void main(String[] args){
        IDAL SQLite = new SQLiteDAL();
        Scanner sc = new Scanner(System.in);

        System.out.println("Super-Lee");
        System.out.println("1. Employee");
        System.out.println("2. Shifts");

        if(sc.nextInt()==1){
            EmployeeMenu.Run();
        }
        else{
            ShiftsMenu.Run();
        }


    }
}
