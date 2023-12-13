package com.uob.testingout;

import java.util.Scanner;

public class TellerFunction {

  
    public void runTellerFunction() {
        System.out.println("Welcome to Teller Function Menu");
        System.out.println("======================");
        System.out.println("1. View All Accounts");
        System.out.println("2. Create Account");
        System.out.println("3. Delete Account");
        System.out.println("4. Deposit Money");
        System.out.println("5. Withdraw Money");
        System.out.println("0. Exit Application");
        System.out.println("Please enter a input : ");
        Scanner scan = new Scanner(System.in);
        int choice = 0;

        do {

            choice = Integer.parseInt(scan.nextLine());
            System.out.println("This is my choice: " + choice);

            switch (choice) {
                case 1:
                    System.out.println("View All Accounts ");
                    break;
                case 2:
                    System.out.println("Create Account ");
                    break;
                case 3:
                    System.out.println("Delete Account ");
                    break;
                case 4:
                    System.out.println("Deposit Money");
                    break;
                case 5:
                    System.out.println("Withdraw Money");
                    break;
                case 0:
                    System.out.println("Exit Application");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

        } while (choice < 1 || choice > 5);

    }

}

   


