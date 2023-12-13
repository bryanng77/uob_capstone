package com.uob.testingout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginPage {
    private static final Logger LOGGER = Logger.getLogger(LoginPage.class.getName());
    static Scanner scan = new Scanner(System.in);

    public static void landingScreen() throws SQLException {

        System.out.println("Welcome to Bank Interface!");
        System.out.println("==========================");
        System.out.println("Please select your User Portal: ");

        System.out.println("1. System Admin Portal");
        System.out.println("2. Bank Teller Portal");
        System.out.println("3. Exit Login Page");

        System.out.println("=========================");
        System.out.print("Enter your input here (1/2/3): ");
        int portalinput = scan.nextInt();
        scan.nextLine();

        switch (portalinput) {
            case 1:
                // System Admin Portal
                systemAdminPortal();
                LOGGER.info("Selected systemAdminPortal");

                break;
            case 2:
                // Bank Teller Portal
                bankTellerPortal();
                LOGGER.info("Selected bankTellerPortal");

            case 3:
                System.exit(0);
                LOGGER.info("User Exited the System");

        }

    }

    // System Admin Portal Method
    public static void systemAdminPortal() throws SQLException {
        LOGGER.info("Entered systemAdminPortal");

        int attempts = 0; // number of attempts upon start of program
        int maximum_attempts = 3; // maximum number of attempts

        int maxLengthOfUsername = 20;
        int maxLengthOfPassword = 20;

        String sUsername = "";
        String sPassword = "";

        // Declaring the scanner object

        while (attempts < maximum_attempts) {

            boolean correctSysUsername = false; // Initialize to false

            while (!correctSysUsername) {
                LOGGER.log(Level.INFO, "Entering System Admin Username into the page");
                System.out.print("Please enter System Admin Username: ");
                sUsername = scan.nextLine();

                if (sUsername.length() > maxLengthOfUsername) {
                    System.out.println("System Admin Username should not exceed 20 Characters");
                    LOGGER.log(Level.WARNING, "System Admin Username exceeded 20 characters. User to try again.");
                } else if (sUsername.isEmpty()) {
                    System.out.println("Error: Empty Field");
                    LOGGER.log(Level.WARNING, "System Admin Username was an Empty Input. User to try again.");
                } else {
                    correctSysUsername = true;
                }
            }

            boolean correctSysPassword = false; // Initialize to false

            while (!correctSysPassword) {
                LOGGER.log(Level.INFO, "Entering System Admin Password into the page");
                System.out.print("Please enter System Admin password: ");
                sPassword = scan.nextLine();

                if (sPassword.length() > maxLengthOfPassword) {
                    System.out.println("System Admin Password should not exceed 20 Characters");
                    LOGGER.log(Level.WARNING, "System Admin Password exceeded 20 characters. User to try again.");
                } else if (sPassword.isEmpty()) {
                    System.out.println("Error: Empty Field");
                    LOGGER.log(Level.WARNING, "System Admin Password was an Empty Input. User to try again.");
                } else {
                    correctSysPassword = true;
                }
            }

            // Testing MySQL database connectivity

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/uob_capstone", "root",
                    "bryanng77");

            LOGGER.log(Level.INFO, "Database Successfully Connected to check if Username and Password Exists.");

            String sqlStatement = "SELECT * FROM SystemAdmin WHERE username = ? AND password = ?;";

            // Creating a PreparedStatement object in Java for executing a SQL query
            PreparedStatement stmt = con.prepareStatement(sqlStatement);

            stmt.setString(1, sUsername);
            stmt.setString(2, sPassword);

            // Executing the SQL statement
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int id = result.getInt(1);
                String name = result.getString(2);
                String username = result.getString(3);
                String password = result.getString(4);

                SystemAdmin s1 = new SystemAdmin(id, name, username, password);

                System.out.println("=========================================================");
                System.out.println("Welcome " + name + " to the System Admin Menu");
                System.out.println("==========================================");

                System.out.println("Login successful!"); // TO DO: SUCCESSFUL LOG INFO
                System.out.println("=====================");

                LOGGER.log(Level.INFO, "Login into System Admin Portal Successful.");

                s1.runSystemAdmin(con);

                break;

            } else {
                System.out.println("User does not exist or incorrect credentials. Please try again.");
                System.out.println("===============================================================");
                attempts++; // increasing the user attempt by 1
                LOGGER.log(Level.WARNING, "User entered credentials that did not exist or was incorrect. Has "
                        + (maximum_attempts - attempts) + " remaining.");
                System.out.println("You have " + (maximum_attempts - attempts) + " remaining.");

            }

            con.close();

        }

        // This runs when we're out of the while loop
        if (attempts == maximum_attempts) {
            System.out.println("Maximum login attempts reached. Exiting...");
            LOGGER.log(Level.WARNING,
                    "User reached the maximum login attempts. Program exits upon maximum attempts for security purposes.");

        }
    }

    // Bank Teller Portal Menu
    public static void bankTellerPortal() {
        LOGGER.info("Entered bankTellerPortal");
        int attempts = 0; // number of attempts upon start of program
        int maximum_attempts = 3; // maximum number of attempts

        int maxLengthOfUsername = 20;
        int maxLengthOfPassword = 20;

        String tUsername = "";
        String tPassword = "";

        // Declaring the scanner object

        while (attempts < maximum_attempts) {

            boolean correctUsername = false;

            while (!correctUsername) {
                LOGGER.log(Level.INFO, "Entering Bank Teller Username into the page");
                System.out.print("Please enter the Teller's Username: ");
                tUsername = scan.nextLine();

                if (tUsername.length() > maxLengthOfUsername) {
                    System.out.println("Error: Please limit Teller Username to 20 characters.");
                    LOGGER.log(Level.WARNING, "Bank Teller Username exceeded 20 characters. User to try again.");
                    continue;
                } else if (tUsername.isEmpty()) {
                    System.out.println("Error: Empty Field");
                    LOGGER.log(Level.WARNING, "Bank Teller Username was an Empty Input. User to try again.");

                    continue;
                } else {
                    correctUsername = true;
                }
            }

            boolean correctPassword = false;

            while (!correctPassword) {
                LOGGER.log(Level.INFO, "Entering Bank Teller Password into the page");
                System.out.print("Please enter the Teller's Password: ");
                tPassword = scan.nextLine();

                if (tPassword.length() > maxLengthOfPassword) {
                    System.out.println("Error: Please limit Teller Password to 20 characters.");
                    LOGGER.log(Level.WARNING, "Bank Teller Password exceeded 20 characters. User to try again.");
                    continue;
                } else if (tPassword.isEmpty()) {
                    System.out.println("Error: Empty Field");
                    LOGGER.log(Level.WARNING, "Bank Teller Password was an Empty Input. User to try again.");
                    continue;
                } else {
                    correctPassword = true;
                }
            }

            // Testing MySQL database connectivity
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/uob_capstone", "root",
                        "bryanng77");

                LOGGER.log(Level.INFO, "Database Successfully Connected to check if Username and Password Exists.");

                String sqlStatement = ("SELECT * FROM teller WHERE username = ? AND password = ?; ");

                // The line PreparedStatement stmt = con.prepareStatement(sqlStatement); is
                // creating a PreparedStatement object in Java for executing a SQL query
                PreparedStatement stmt = con.prepareStatement(sqlStatement);

                stmt.setString(1, tUsername);
                stmt.setString(2, tPassword);

                // The line int results = stmt.executeUpdate(); is executing the SQL statement
                ResultSet result = stmt.executeQuery();

                if (result.next()) { // in relation to the SQL Teller Table

                    int empID = result.getInt(1);
                    String fname = result.getString(2);
                    String username = result.getString(3);
                    String password = result.getString(4);

                    BankTeller b1 = new BankTeller(empID, fname, username, password);

                    System.out.println("======================================");
                    System.out.println("Welcome " + fname + " to the Bank Teller Menu."); // TO DO: SUCCESSFUL LOG INFO
                    System.out.println("======================================");

                    System.out.println("Login successful!");
                    System.out.println("=====================");

                    LOGGER.log(Level.INFO, "Login into Bank Teller Portal Successful.");

                    b1.runBankTeller(con);

                    break;

                } else {
                    System.out.println("User does not exist or incorrect credentials. Please try again."); // TO DO:
                                                                                                           // FAIL LOG
                                                                                                           // ERROR
                    attempts++; // increasing the user attempt by 1
                    LOGGER.log(Level.WARNING, "User entered credentials that did not exist or was incorrect. Has "
                            + (maximum_attempts - attempts) + " remaining.");
                    System.out.println("You have " + (maximum_attempts - attempts) + " remaining.");

                }

                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

        // This runs when we're out of the while loop
        System.out.println("Maximum login attempts reached. Exiting...");
        System.out.println("============================================");
        LOGGER.log(Level.WARNING,
                "User reached the maximum login attempts. Program exits upon maximum attempts for security purposes.");

    }

}
